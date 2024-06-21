// Import Java libraries
import java.io.*;
import java.util.Scanner;

public class LoadBalancer
{

    // Define variables for both functions to use
    static int[] rowSums = new int[0];
    static int[] columnSums = new int[0];
    static int idealSolution = 0;
 
    // Define Main function
    public static void main(String[] args) {

        // Initialise variables
        int proc, // number of processors
            rows, // number of rows in the matrix of the loads
            cols; // number of columns in the matrix of the loads

        int r, c, p; // Indexes for manipulating arrays
        Scanner keyboard  = new Scanner(System.in); // Scanner for reading user input

        // Ask the user to enter the number of processors
        System.out.println("Enter the number of processors that need to share the load:  ");
        proc = keyboard.nextInt();

        // Ensure that the number of processors is at least two
        if (proc < 2) {
            System.out.println("Too few processors");
            System.exit(0);
        }

        // Ask the user to enter the size of the matrix
        System.out.println("Enter the number of rows of the matrix of the loads: ");
        rows = keyboard.nextInt();
        System.out.println("Enter the number of columns of the matrix of the loads: ");
        cols = keyboard.nextInt();

        // Ensure that the number of matrix rows and columns is larger or equal to than the number of processors
        if((rows <= proc) || (cols <= proc)){
            System.out.println("Matrix too small");
            System.exit(0);
        }

        // Create matrix of loads
        int[][] M = new int[rows][cols];
        
        // Ask the user to enter the load matrix
        System.out.println("Enter the load matrix (row by row, left to right): ");
        
        // Fill matrix with all values entered by user
        for (r = 0; r < rows; r++) {
            for (c = 0; c < cols; c++) {
                M[r][c] = keyboard.nextInt();
            }
        }

        // Initialise variables used for results
        int maxLoad = 0; // The maximum load of all regions
        int[][] C = new int[4][proc]; // Array containing the sub-array coordinates for each processor
        
        /*
         * Perform initial search on array to acquire information.
         * This information includes the average load divided between processors
         * as well as the sum of every row and the sum of every column.
         */ 
        initialSearch(M, proc);

        /*
         * Define four arrays to store every processor's load when split both
         * horizontally and vertically using the two approaches.
         */
        int[] procLoadsHorizontal1 = new int[proc];
        int[] procLoadsHorizontal2 = new int[proc];
        int[] procLoadsVertical1 = new int[proc];
        int[] procLoadsVertical2 = new int[proc];

        // Initialise variables for horizontal and vertical splitting process.
        int currentProc = 0;
        boolean newProcessor = true;

        int rowsRemaining = rowSums.length - 1;
        int procsRemaining = ((proc - 1) - currentProc);
        int columnsRemaining = columnSums.length - 1;

        int[][] coordinateArrayH1 = new int[4][proc];
        int[][] coordinateArrayH2 = new int[4][proc];
        int[][] coordinateArrayV1 = new int[4][proc];
        int[][] coordinateArrayV2 = new int[4][proc];
        
        int maxHorizontalLoad1 = 0;
        int maxHorizontalLoad2 = 0;
        int maxVerticalLoad1 = 0;
        int maxVerticalLoad2 = 0;

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*
         * This loop and the next loop appear to be similar. Both are intended to assign each
         * processor even loads when split horizontally. However, the difference in this
         * loop is that it determines whether it would be more beneficial to leave the
         * current load for the next processor or take it.
         */
        for(int i = 0; i < rowSums.length; i++){

            // Set the first half of coordinates for a new processor.
            if(newProcessor == true){
                coordinateArrayH1[0][currentProc] = i;
                coordinateArrayH1[1][currentProc] = 0;
                newProcessor = false;
            }

            // Identify how many rows and processors are left to allocate.
            rowsRemaining = ((rowSums.length - 1) - i);
            procsRemaining = ((proc - 1) - currentProc);

            // Ensure all the processors are allocated.
            if(rowsRemaining == procsRemaining){
                procLoadsHorizontal1[currentProc] += rowSums[i];
                if(procLoadsHorizontal1[currentProc] > maxHorizontalLoad1){
                    maxHorizontalLoad1 = procLoadsHorizontal1[currentProc];
                }
                coordinateArrayH1[2][currentProc] = i;
                coordinateArrayH1[3][currentProc] = cols - 1;
                currentProc++;
                newProcessor = true; 
            }

            // Ensure every last element in the array is covered.
            else if(currentProc == (proc-1)){
                procLoadsHorizontal1[currentProc] += rowSums[i];
                if(procLoadsHorizontal1[currentProc] > maxHorizontalLoad1){
                    maxHorizontalLoad1 = procLoadsHorizontal1[currentProc];
                }
                coordinateArrayH1[2][currentProc] = i;
                coordinateArrayH1[3][currentProc] = cols - 1;
            }

            // If the current processor covers enough load, move on to the next processor.
            else if(procLoadsHorizontal1[currentProc] + rowSums[i] >= idealSolution){

                // Find if it's better to take the next load or not.
                int leaveLoad = Math.abs(procLoadsHorizontal1[currentProc] - idealSolution);
                int takeLoad = Math.abs(procLoadsHorizontal1[currentProc] + rowSums[i] - idealSolution);

                if((leaveLoad < takeLoad) && (procLoadsHorizontal1[currentProc] > 0)){ 
                    // Case where it is not better - let the next processor handle it.
                    i--;
                    coordinateArrayH1[2][currentProc] = i;
                    coordinateArrayH1[3][currentProc] = cols - 1;
                    currentProc++;
                    newProcessor = true; 
                }
                else{
                    // Case where it is better - let the current processor handle it.
                    procLoadsHorizontal1[currentProc] += rowSums[i];
                    if(procLoadsHorizontal1[currentProc] > maxHorizontalLoad1){
                        maxHorizontalLoad1 = procLoadsHorizontal1[currentProc];
                    }
                    coordinateArrayH1[2][currentProc] = i;
                    coordinateArrayH1[3][currentProc] = cols - 1;
                    currentProc++;
                    newProcessor = true; 
                }
            }

            // If the current processor isn't covering enough load, keep assigning loads.
            else{
                procLoadsHorizontal1[currentProc] += rowSums[i];
                if(procLoadsHorizontal1[currentProc] > maxHorizontalLoad1){
                    maxHorizontalLoad1 = procLoadsHorizontal1[currentProc];
                }
            }
        }

        // Reset the variables for further calculations.
        currentProc = 0;
        newProcessor = true;

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*
         * This loop and the previous loop appear to be similar. Both are intended to assign each
         * processor even loads when split horizontally. However, the difference in this
         * loop is that it always takes the current load if it is greater than or equal to the
         * average load.
         */
        for(int i = 0; i < rowSums.length; i++){

            // Set the first half of coordinates for a new processor.
            if(newProcessor == true){
                coordinateArrayH2[0][currentProc] = i;
                coordinateArrayH2[1][currentProc] = 0;
                newProcessor = false;
            }

            // Identify how many rows and processors are left to allocate.
            rowsRemaining = ((rowSums.length - 1) - i);
            procsRemaining = ((proc - 1) - currentProc);

            // Ensure all the processors are allocated.
            if(rowsRemaining == procsRemaining){
                procLoadsHorizontal2[currentProc] += rowSums[i];
                if(procLoadsHorizontal2[currentProc] > maxHorizontalLoad2){
                    maxHorizontalLoad2 = procLoadsHorizontal2[currentProc];
                }
                coordinateArrayH2[2][currentProc] = i;
                coordinateArrayH2[3][currentProc] = cols - 1;
                currentProc++;
                newProcessor = true; 
            }

            // Ensure every last element in the array is covered.
            else if(currentProc == (proc-1)){
                procLoadsHorizontal2[currentProc] += rowSums[i];
                if(procLoadsHorizontal2[currentProc] > maxHorizontalLoad2){
                    maxHorizontalLoad2 = procLoadsHorizontal2[currentProc];
                }
                coordinateArrayH2[2][currentProc] = i;
                coordinateArrayH2[3][currentProc] = cols - 1;
            }

            // If the current processor covers enough load, move on to the next processor.
            else if(procLoadsHorizontal2[currentProc] + rowSums[i] >= idealSolution){
                procLoadsHorizontal2[currentProc] += rowSums[i];
                if(procLoadsHorizontal2[currentProc] > maxHorizontalLoad2){
                    maxHorizontalLoad2 = procLoadsHorizontal2[currentProc];
                }
                coordinateArrayH2[2][currentProc] = i;
                coordinateArrayH2[3][currentProc] = cols - 1;
                currentProc++;
                newProcessor = true; 
                
            }

            // If the current processor isn't covering enough load, keep assigning loads.
            else{
                procLoadsHorizontal2[currentProc] += rowSums[i];
                if(procLoadsHorizontal2[currentProc] > maxHorizontalLoad2){
                    maxHorizontalLoad2 = procLoadsHorizontal2[currentProc];
                }
            }
        }

        // Use the more optimal solution
        if(maxHorizontalLoad2 < maxHorizontalLoad1){
            maxHorizontalLoad1 = maxHorizontalLoad2;
            coordinateArrayH1 = coordinateArrayH2;
        }

        // Reset the variables for further calculations.
        currentProc = 0;
        newProcessor = true;
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*
         * This loop and the next loop appear to be similar. Both are intended to assign each
         * processor even loads when split vertically. However, the difference in this
         * loop is that it determines whether it would be more beneficial to leave the
         * current load for the next processor or take it.
         */
        for(int i = 0; i < columnSums.length; i++){

            // Set the first half of coordinates for a new processor.
            if(newProcessor == true){
                coordinateArrayV1[0][currentProc] = 0;
                coordinateArrayV1[1][currentProc] = i;
                newProcessor = false;
            }

            // Identify how many rows and processors are left to allocate.
            columnsRemaining = ((columnSums.length - 1) - i);
            procsRemaining = ((proc - 1) - currentProc);

            // Ensure all the processors are allocated.
            if(columnsRemaining == procsRemaining){
                procLoadsVertical1[currentProc] += columnSums[i];
                if(procLoadsVertical1[currentProc] > maxVerticalLoad1){
                    maxVerticalLoad1 = procLoadsVertical1[currentProc];
                }
                coordinateArrayV1[2][currentProc] = rows - 1;
                coordinateArrayV1[3][currentProc] = i;
                currentProc++;
                newProcessor = true;
            }

            // Ensure every last element in the array is covered.
            else if(currentProc == (proc-1)){
                procLoadsVertical1[currentProc] += columnSums[i];
                if(procLoadsVertical1[currentProc] > maxVerticalLoad1){
                    maxVerticalLoad1 = procLoadsVertical1[currentProc];
                }
                coordinateArrayV1[2][currentProc] = rows - 1;
                coordinateArrayV1[3][currentProc] = i;
            }

            // If the current processor covers enough load, move on to the next processor.
            else if(procLoadsVertical1[currentProc] + columnSums[i] >= idealSolution){

                // Find if it's better to take the next load or not
                int leaveLoad = Math.abs(procLoadsVertical1[currentProc] - idealSolution);
                int takeLoad = Math.abs(procLoadsVertical1[currentProc] + columnSums[i] - idealSolution);

                if((leaveLoad < takeLoad) && (procLoadsVertical1[currentProc] > 0)){ 
                    // Case where it is not better - let the next processor handle it.
                    i--;
                    coordinateArrayV1[2][currentProc] = rows - 1;
                    coordinateArrayV1[3][currentProc] = i;
                    currentProc++;
                    newProcessor = true;
                }
                else{
                    // Case where it is better - let the current processor handle it.
                    procLoadsVertical1[currentProc] += columnSums[i];
                    if(procLoadsVertical1[currentProc] > maxVerticalLoad1){
                        maxVerticalLoad1 = procLoadsVertical1[currentProc];
                    }
                    coordinateArrayV1[2][currentProc] = rows - 1;
                    coordinateArrayV1[3][currentProc] = i;
                    currentProc++;
                    newProcessor = true;
                }
            }

            // If the current processor isn't covering enough load, keep assigning loads.
            else{
                procLoadsVertical1[currentProc] += columnSums[i];
                if(procLoadsVertical1[currentProc] > maxVerticalLoad1){
                    maxVerticalLoad1 = procLoadsVertical1[currentProc];
                }
            }
        }
        
        // Reset the variables for further calculations.
        currentProc = 0;
        newProcessor = true;

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*
         * This loop and the previous loop appear to be similar. Both are intended to assign each
         * processor even loads when split vertically. However, the difference in this
         * loop is that it always takes the current load if it is greater than or equal to the
         * average load.
         */
        for(int i = 0; i < columnSums.length; i++){

            // Set the first half of coordinates for a new processor.
            if(newProcessor == true){
                coordinateArrayV2[0][currentProc] = 0;
                coordinateArrayV2[1][currentProc] = i;
                newProcessor = false;
            }

            // Identify how many rows and processors are left to allocate.
            columnsRemaining = ((columnSums.length - 1) - i);
            procsRemaining = ((proc - 1) - currentProc);

            // Ensure all the processors are allocated.
            if(columnsRemaining == procsRemaining){
                procLoadsVertical2[currentProc] += columnSums[i];
                if(procLoadsVertical2[currentProc] > maxVerticalLoad2){
                    maxVerticalLoad2 = procLoadsVertical2[currentProc];
                }
                coordinateArrayV2[2][currentProc] = rows - 1;
                coordinateArrayV2[3][currentProc] = i;
                currentProc++;
                newProcessor = true;
            }

            // Ensure every last element in the array is covered.
            else if(currentProc == (proc-1)){
                procLoadsVertical2[currentProc] += columnSums[i];
                if(procLoadsVertical2[currentProc] > maxVerticalLoad2){
                    maxVerticalLoad2 = procLoadsVertical2[currentProc];
                }
                coordinateArrayV2[2][currentProc] = rows - 1;
                coordinateArrayV2[3][currentProc] = i;
            }

            // If the current processor covers enough load, move on to the next processor.
            else if(procLoadsVertical2[currentProc] + columnSums[i] >= idealSolution){
                procLoadsVertical2[currentProc] += columnSums[i];
                if(procLoadsVertical2[currentProc] > maxVerticalLoad2){
                    maxVerticalLoad2 = procLoadsVertical2[currentProc];
                }
                coordinateArrayV2[2][currentProc] = rows - 1;
                coordinateArrayV2[3][currentProc] = i;
                currentProc++;
                newProcessor = true; 
            }

            // If the current processor isn't covering enough load, keep assigning loads.
            else{
                procLoadsVertical2[currentProc] += columnSums[i];
                if(procLoadsVertical2[currentProc] > maxVerticalLoad2){
                    maxVerticalLoad2 = procLoadsVertical2[currentProc];
                }
            }
        }

        // Use the more optimal solution.
        if(maxVerticalLoad2 < maxVerticalLoad1){
            maxVerticalLoad1 = maxVerticalLoad2;
            coordinateArrayV1 = coordinateArrayV2;
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*
         * After the loads have been assigned to processors both horizontally and vertically,
         * we need to determine which way distributes the load more evenly. Here, I say that
         * it should use the horizontal distribution unless the vertical distribution has the
         * more optimal results.
         */
        // Use the more optimal solution.
        if(maxVerticalLoad1 < maxHorizontalLoad1){
            maxHorizontalLoad1 = maxVerticalLoad1;
            coordinateArrayH1 = coordinateArrayV1;
        }

        C = coordinateArrayH1;
        maxLoad = maxHorizontalLoad1;
            
        // Print all the output.
        System.out.println();
        System.out.println("The maximum load (first line) followed by ");
        System.out.println("the " + proc + " regions selected are (top row, left column, bottom row, right column): ");

        // Print the maximum processor load
        System.out.println(maxLoad);

        // Print the sub-array coordinates for each processor
        for (p = 0; p < proc; p++) {
            System.out.println(C[0][p] + " " + C[1][p] + " " + C[2][p] + " " + C[3][p]);
        }
    }

    // Perform initial search of the array
    public static void initialSearch(int[][] array, int processors){
        rowSums = new int[array.length];
        columnSums = new int[array[0].length];
        int totalLoad = 0;

        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[i].length; j++){
                rowSums[i] += array[i][j];
                columnSums[j] += array[i][j];
                totalLoad += array[i][j];
            }
        }
        idealSolution = (totalLoad / processors);
    }
}