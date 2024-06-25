package coinGame;

public class OptimalMovesAndWinnings {
	
	// The original pile of coins
	int coinList[];

	// The dynamic programming table to compute the overall optimal winnings starting from a given sub-array
	int winningsTable[][]; 
	
	// The number of original piles
	int sizeOfGame;
	
	optimalWinnings(int piles[]) { // initialise coinList and optimalWinnings
		sizeOfGame = piles.length;
		coinList = new int[sizeOfGame];
		for(int i = 0; i < sizeOfGame; i++) {
			coinList[i] = piles[i];
		}
		winningsTable = new int[sizeOfGame][sizeOfGame];	// initialise winningsTable to 0 everywhere
		
		for (int i = 0; i < sizeOfGame; i++) {
			for (int j = 0; j < sizeOfGame; j++) {
				winningsTable[i][j] = 0;
			}
		}
	}

	/*
	 * Pre-condition: coinList and winningsTable have already been initialised
	 * Post-condition: compute the winningsTable so that winningsTable[i][j] is equal to
	 * the optimal guaranteed winnings for a player which plays from piles in play given by
	 * coinList[i,j]
	 */
    void makeTable() {
		// Iterate diagonally until the top-right corner is reached
        for (int i = 0; i < sizeOfGame; i++) {
            for (int j = i; j < sizeOfGame; j++) {
				winningsTable[j - i][j] = calculateValue(j - i, j);
			}
		}
		printOutput();
	}

	/*
	 * This is a helper function that is used by "makeTable" to calculate the
	 * value of every cell in 'winningsTable'
	 */
	public int calculateValue(int i, int j) {
		// Define variables
		int W1leftSide = 0;
		int W1rightSide = 0;
		int W2leftSide = 0;
		int W2rightSide = 0;

		/*
		 * This section calculates the two halves of both W1 and W2
		 * using the given formulas and is kept as zero if the index 
		 * is out of bounds
		 */
		try {
			W1leftSide = (winningsTable[i + 2][j]);
		}
		catch (ArrayIndexOutOfBoundsException e) {}
		try {
			W1rightSide = (winningsTable[i + 1][j - 1]);
		}
		catch (ArrayIndexOutOfBoundsException e) {}
		try {
			W2leftSide = (winningsTable[i + 1][j - 1]);
		}
		catch (ArrayIndexOutOfBoundsException e) {}
		try {
			W2rightSide = (winningsTable[i][j - 2]);
		}
		catch (ArrayIndexOutOfBoundsException e) {}

		// Calculate W1 and W2
		int W1;
		int W2;
		if (W1leftSide < W1rightSide) {
			W1 = W1leftSide;
		}
		else{
			W1 = W1rightSide;
		}

		if (W2leftSide < W2rightSide) {
			W2 = W2leftSide;
		}
		else{
			W2 = W2rightSide;
		}
		
		// Calculate the optimal guaranteed winnings
		int winnings;
		if (coinList[i] + W1 > coinList[j] + W2) {
			winnings = coinList[i] + W1;
		}
		else {
			winnings = coinList[j] + W2;
		}

		return winnings;
	}

	/*
	 * This is a helper function that is only used in testing to help
	 * verify whether the given invariant holds true for each element,
	 * helping to check the correctness of the values in the table
	 */
	public int sumCoinList(int start, int end) {
		int total = 0;
		for (int i = start; i <= end; i++) {
			total += coinList[i];
		}
		return total;
	}

	/*
	 * Post-condition: returns the optimal guaranteed winnings for Player 1, starting from coinList
	 */
    int optimalGuaranteedPlayerOne() {
		/*
		 * Optimal winnings for Player 1 are built up into the value of the
		 * top-right corner of winningsTable
		 */ 
		int guaranteedWinnings = 0;
		if (coinList.length >= 1) {
			guaranteedWinnings = winningsTable[0][(winningsTable.length - 1)];
		}
        return guaranteedWinnings;
    }
	
	/*
	 * Post-condition: returns the optimal guaranteed winnings for Player 2, starting from coinList
	 */
	int optimalGuaranteedPlayerTwo() {
        /*
		 * Optimal winnings for Player 2 are the minimum of the top-right
		 * corner + 1 to the left and the top-right corner + 1 down
		 */ 
		int guaranteedWinnings = 0;
		if (coinList.length > 1) {
			guaranteedWinnings = Math.min((winningsTable[0][winningsTable.length - 2]), (winningsTable[1][winningsTable.length - 1]));
		}
        return guaranteedWinnings;
	}
	
	/*
	 * Postcondition: Returns the list of indices in coinList corresponding to the piles of
	 * coins removed by Player 1 playing optimal moves, assuming that Player 2 plays
	 * optimally. (Note that there could be several optimal plays; this method only needs to 
	 * return one of them.)
	 */
	int[] optimalPlayerOneMoves() {
		// Define variables
        int[] optimalMoves = new int[(sizeOfGame + 1) / 2];
		int i = 0;
		int j = (sizeOfGame - 1);
		
		for (int OMindex = 0; OMindex < optimalMoves.length; OMindex++) {
			int W1leftSide = 0;
			int W1rightSide = 0;
			int W2leftSide = 0;
			int W2rightSide = 0;

			/*
			* This section calculates the two halves of both W1 and W2
			* using the given formulas and is kept as zero if the index 
			* is out of bounds
			*/
			try {
				W1leftSide = (winningsTable[i + 2][j]);
			}
			catch (ArrayIndexOutOfBoundsException e) {}
			try {
				W1rightSide = (winningsTable[i + 1][j - 1]);
			}
			catch (ArrayIndexOutOfBoundsException e) {}
			try {
				W2leftSide = (winningsTable[i + 1][j - 1]);
			}
			catch (ArrayIndexOutOfBoundsException e) {}
			try {
				W2rightSide = (winningsTable[i][j - 2]);
			}
			catch (ArrayIndexOutOfBoundsException e) {}

			// Calculate W1 and W2
			int W1;
			int W2;
			if (W1leftSide < W1rightSide) {
				W1 = W1leftSide;
			}
			else{
				W1 = W1rightSide;
			}

			if (W2leftSide < W2rightSide) {
				W2 = W2leftSide;
			}
			else{
				W2 = W2rightSide;
			}
			
			// Calculate which stack to take and change index based on W1/W2
			if (coinList[i] + W1 > coinList[j] + W2) {
				optimalMoves[OMindex] = i;
				if (W1 == W1leftSide) {
					i += 2;
				}
				else {
					i++;
					j--;
				}
				
			} 
			else {
				optimalMoves[OMindex] = j;
				if (W2 == W2leftSide) {
					i++;
					j--;
				}
				else {
					j -= 2;
				}
			}
		}
		
		return optimalMoves;
	}

    /*
     * Postcondition: Returns the array of indices in coinList corresponding to the piles of
     * coins removed by Player 2 playing optimal moves, assuming that Player 1 plays
     * optimally. (Note that there could be several optimal plays; this method only needs to 
     * return one of them.)
     */
    int[] optimalPlayerTwoMoves() {
        /*
		 * Optimal moves for Player 2 are every move Player 1 did not take
		 */ 

		// Define variables
        int[] optimalMoves = new int[(sizeOfGame / 2)];
		int[] playerOneMoves = optimalPlayerOneMoves();
		int index = 0;
		boolean indexFound = false;

		// Find the indices that player 1 did not take
		for (int i = 0; i < sizeOfGame; i++) {
			indexFound = false;
			
			for (int j = 0; j < playerOneMoves.length; j++) {
				if (i == playerOneMoves[j]) {
					indexFound = true;
				}
			}
			if (indexFound == false) {
				optimalMoves[index] = i;
				index++;
			}
		}

		return optimalMoves;
    }

	/*
	 * This is a helper function that can be used to print out
	 * important information for debugging and checking correctness
	 */
	public void printOutput() {
		boolean invariantHolds = true;
		// Print the table
		for(int i = 0; i < sizeOfGame; i++){
			System.out.println();
			for(int j = 0; j < sizeOfGame; j++){
				if(winningsTable[i][j] < 10){
					System.out.print("0");
				}
				System.out.print(winningsTable[i][j] + " ");
				try {
					if (winningsTable[i][j] + Math.min(winningsTable[i+1][j], winningsTable[i][j-1]) != sumCoinList(i, j)) {
						invariantHolds = false;
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {}
			}
		}

		System.out.println();

		System.out.println("winningsTable invariant holds " + invariantHolds);
		
		// Print player 1's optimal guaranteed winnings
		System.out.println("PLAYER 1 OPTIMAL GUARANTEED WINNINGS: " + optimalGuaranteedPlayerOne());

		// Print player 2's optimal guaranteed winnings
		System.out.println("PLAYER 2 OPTIMAL GUARANTEED WINNINGS: " + optimalGuaranteedPlayerTwo());

		// Print player 1's optimal moves
		System.out.print("PLAYER 1 OPTIMAL MOVES: ");
		int[] playerOneOptimalMoves = optimalPlayerOneMoves();
		int playerOneSum = 0;
		for (int i = 0; i < playerOneOptimalMoves.length; i++) {
			System.out.print(playerOneOptimalMoves[i] + " ");
			playerOneSum += coinList[playerOneOptimalMoves[i]];
		}
		System.out.println();
		if (playerOneSum == optimalGuaranteedPlayerOne()){
			System.out.println("valid answer");
		}
		else {
			System.out.println("uh oh! something is wrong!");
		}

		// Print player 2's optimal moves
		System.out.print("PLAYER 2 OPTIMAL MOVES: ");
		int[] playerTwoOptimalMoves = optimalPlayerTwoMoves();
		int playerTwoSum = 0;
		for (int i = 0; i < playerTwoOptimalMoves.length; i++) {
			System.out.print(playerTwoOptimalMoves[i] + " ");
			playerTwoSum += coinList[playerTwoOptimalMoves[i]];
		}
		System.out.println();
		if (playerTwoSum == optimalGuaranteedPlayerTwo()){
			System.out.println("valid answer");
		}
		else {
			System.out.println("uh oh! something is wrong!");
		}
	}


///////////////////////// FOR TESTING /////////////////////////////////
	public static void main (String[] args) {
		int[] piles= {3, 7, 6, 2, 5};
		optimalWinnings game = new optimalWinnings(piles);
		game.makeTable();
	}
}

