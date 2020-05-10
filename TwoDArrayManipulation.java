

import java.util.Scanner;


public class TwoDArrayManipulation {
    
	public static void main(String[] args) {
	    // Create a Scanner object for input
		Scanner sc = new Scanner(System.in);

		// Loop for accepting correct row and column sizes from user
		int rowSize=0;`
		int columnSize=0;

		boolean validBlockSizeInput = false;
		while(!validBlockSizeInput){
			System.out.println("Enter row size : ");
			rowSize = sc.nextInt();
			System.out.println("Enter column size : ");
			columnSize = sc.nextInt();
            if(rowSize > 2 && columnSize > 2 && rowSize <=10 && columnSize <= 10){
                validBlockSizeInput = true;
            }
            else{
                System.out.println("\n Row and column size should be >2 and <=10");
                System.out.println("Enter the details again \n");
            }
		}

		MyBlock mb = new MyBlock(rowSize, columnSize);
        
        // Build a loop to display the menu, prompt for input and process it as per requirements.
		/**
		 * A while loop is implemented which will keep executing until the user enters '4' which is the option 
		 * to exit the application 
		 */
		int choice=0;
		while(choice!=4){
			System.out.println("\n** Main Menu **");
			System.out.println("1. Add House");
			System.out.println("2. Display Block");
			System.out.println("3. Clear Block");
			System.out.println("4. Quit");

			System.out.println("\nEnter choice :");
			choice = sc.nextInt();

			/**
			 * If the user input is invalid then 'continue' will go into the next iteration of the while loop
			 */
			if(! (choice>=1 && choice<=4) ){
				System.out.println("\nInvalid choice!!\n");
				continue;
			}

			switch(choice){
				/**
				 * Take house dimensions and position from the user
				 */
				case 1:
					System.out.println("Enter row position : ");
					int rowPos = sc.nextInt();
					System.out.println("Enter column position : ");
					int colPos = sc.nextInt();
					System.out.println("Enter rows covered by house : ");
					int rows = sc.nextInt();
					System.out.println("Enter columns covered by house : ");
					int columns = sc.nextInt();
					/**
					 * If add house returns true that means the house has met all the requirements and has been added
					 */
					if(mb.addHouse(rowPos, colPos, rows, columns)){
						System.out.println("\nHouse added successfully!!");
					}
					break;
					
				case 2:
					mb.displayBlock();
					break;
	
				case 3:
					mb.clearBlock();
					break;
	
				/**
				 * Programm will exit with system.exit command  
				 */	
				case 4:
					System.out.println("\nSee you soon!! ");
					System.exit(0);
			}
		}
		sc.close();
	}

}

// MyBlock class 
class MyBlock {
	private int[][] block;
	
	// Max rows that a house can have in a block
	int maxHouseRows;
	
	// Max columns a house can have in a block
	int maxHouseColumns;
	
	// Total number of houses currently in the block
	int houseCount = 0;


	// Initialise the block so that each cell is set to the character '0' 
	public MyBlock(int maxRows, int maxColumns) {
		block = new int[maxRows][maxColumns];
		initialiseBlock(block);
		
		/** 
		 * maxHouseRows and maxHouseColumns are initialized to 2 less than total rows and columns since first and last rows and columns
		 * cannot be occupied by a house
		*/
		maxHouseRows = maxRows-2;
		maxHouseColumns = maxColumns-2;
	}
	

	// Initialise block to '0'
	private void initialiseBlock(int[][] block) {
		for(int i=0; i<block.length; i++){
			for(int j=0; j<block[0].length; j++){
				block[i][j] = 0;
			}
		}
	}


	/** 
	 * A nested for loop is used to display the land block
	*/
	public void displayBlock() {
		System.out.println("\n\n ** Land Block **");
		
		for (int i=0; i<block.length; i++) {
			for (int j = 0; j<block[0].length; j++) {
			    System.out.print(block[i][j] + " ");
			}
			System.out.println();
		}		
	}
	
	
	/** 
	 * Since clearBlock is same as initializing a block we use the same funcionality
	*/
	public void clearBlock() {
		initialiseBlock(block);
		houseCount = 0;	
	}
	
	
	// Build a house on the block
	public boolean addHouse(int rowPos, int colPos, int rows, int columns) {

		/** 
		 * We ensure that the first row, first column, last row and last column are not the starting point of the house
		 * block.length is the number of rows 
		 * block[0].length is the number of colunms
		*/
		if(rowPos<=0 || rowPos >=block.length || colPos<=0 || colPos>=block[0].length){
			System.out.println("Invalid house position !!");
			System.out.println(" ** House cannot touch the borders of the block");
			return false;
		}

		/** 
		 * The house dimensions cannot be less than 1
		*/
		if(rows<=0 || columns<=0){
			System.out.println("\n Invalid house dimensions !!");
			System.out.println(" ** House size cannot be less than 1");
			return false;
		}

		/** 
		 * House rows and columns cannot be greater than maxHouseRows and maxHouseColumns
		*/
		if(rows>maxHouseRows || columns>maxHouseColumns){
			System.out.println("Invlaid house dimensions !!");
			System.out.println(" ** House size cannot be equal to bigger than the block");
			return false;
		}

		/**
		 * This if condition checks if the house is not going outside the block.
		 * The logic used is as follows : 
		 *   If the sum of the starting row of the house and total rows is greater than total rows of the block and 
		 *   same for the columns then the last row/column of the house will end up outside the block
		 */
		if( (rowPos + rows >= block.length) || (colPos + columns >= block[0].length) ){
			System.out.println("Invalid house position !!");
			System.out.println(" ** House cannot go outside the block");
			return false;
		}


		/**
		 * We will loop over the area of the house to be created. If any entry in this area is not 0 then some 
		 * other house is already present in the area and current house location is invalid.
		 * 
		 * If the house dimensions are x rows and y columns, we will check for x+2 rows and y+2 columns to meet
		 * the condition - 'every house must be one row and one column away from every house'
		 */
		for(int i=rowPos-1; i<=rowPos+rows; i++){
			for(int j=colPos-1; j<=colPos+columns; j++){
				if(block[i][j] != 0){
					System.out.println("Invalid house location !!");
					System.out.println(" ** House cannot be placed over another house");
					return false;
				}
			}
		}


		/**
		 * If none of the above conditions are true then the house meets all the required parameters.
		 * Hence the house can be added on the block. The houseCount variable will be incremented and the 
		 * block will be filled with that number to indicate the house
		 */
		houseCount++;
		for(int i=rowPos; i<rowPos+rows; i++){
			for(int j=colPos; j<colPos+columns; j++){
				block[i][j] = houseCount;
			}
			System.out.println();
		}
		return true;

	}
		
}