/**
 * Implements a version of the game of life for Rally's coding exercise.
 */
public class Life {

    private static final String DELIMITER = " ";
    private static final String NEWLINE = System.getProperty("line.separator");

    private int numRows = 5;  // This value is changed when a start state is provided
    private int numCols = 5;  // This value is changed when a start state is provided
    private int[][] matrix; // The game board


    /**
     * Default constructor, used mostly for testing.
     */
    public Life() {
        initMatrix();
    }

    /**
     * Creates a Life object using the string parameter to initialize the 
     * matrix game board.
     */
    public Life(String startingState) {
        initMatrix(startingState);
    }

    /**
     * The startingState is assumed to be a series of ones and zeroes
     * separated by commas.  The commas separate the rows.  The ones
     * and zeroes populate the matrix in left-to-right order.
     */
    private void initMatrix(String startingState) {
        String[] splits = startingState.split(",");
        numRows = splits.length;
        numCols = splits[0].length();  // assumes all rows have the same number of columns
        matrix = new int[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                matrix[i][j] = splits[i].charAt(j)=='1'?1:0;
            }
        }
    }


    /**
     * Creates a 5x5 matrix for testing.
     */
    private void initMatrix() {
        matrix = new int[5][5];
        matrix[0][0] = 0;
        matrix[0][1] = 1;
        matrix[0][2] = 0;
        matrix[0][3] = 0;
        matrix[0][4] = 0;

        matrix[1][0] = 1;
        matrix[1][1] = 0;
        matrix[1][2] = 0;
        matrix[1][3] = 1;
        matrix[1][4] = 1;

        matrix[2][0] = 1;
        matrix[2][1] = 1;
        matrix[2][2] = 0;
        matrix[2][3] = 0;
        matrix[2][4] = 1;

        matrix[3][0] = 0;
        matrix[3][1] = 1;
        matrix[3][2] = 0;
        matrix[3][3] = 0;
        matrix[3][4] = 0;

        matrix[4][0] = 1;
        matrix[4][1] = 0;
        matrix[4][2] = 0;
        matrix[4][3] = 0;
        matrix[4][4] = 1;
    }

    /**
     * Updates the matrix game board with the next successive state.
     * See the SoftwareEngineerCodingExercise.pdf document for a
     * description of the rules.
     */
    public void nextIteration() {
        int[][] next = new int[numRows][numCols];  // Temp variable to hold the next state while the rules are applied to the current state.

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int outcome = 2;  // The only valid outcomes are 1 or zero. If a two ends up in the matrix, then something went wrong.
                int liveCount = countLiveNeighbors(i,j);
                if (matrix[i][j] == 1) {  // Cell is currently alive
                    switch (liveCount) {
                        case 0:
                        case 1:
                            // "underpopulation"
                            outcome = 0;
                            break;
                        case 2: 
                        case 3:
                            // "survival"
                            outcome = 1;
                            break;
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            // "overcrowding"
                            outcome = 0;
                            break;
                    }
                }
                else { // Cell is currently dead
                    if (liveCount == 3) {
                        // "reproduction"
                        outcome = 1;
                    }
                    else {
                        // remains dead
                        outcome = 0;  
                    }
                }
                next[i][j] = outcome;
            }
        }

        matrix = next;
    }

    /**
     * Counts the number of cells that are alive and directly adjacent
     * to the cell specified by the row and col parameters.
     * Adjacent cells include the diagonals.
     */
    private int countLiveNeighbors(int row, int col) {
        int liveCount = 0;

        // Row above
        liveCount += isCellAlive(row-1, col-1) ? 1:0;
        liveCount += isCellAlive(row-1, col)   ? 1:0;
        liveCount += isCellAlive(row-1, col+1) ? 1:0;

        // Same row
        liveCount += isCellAlive(row, col-1)   ? 1:0;
        liveCount += isCellAlive(row, col+1)   ? 1:0;

        // Row below
        liveCount += isCellAlive(row+1, col-1) ? 1:0;
        liveCount += isCellAlive(row+1, col)   ? 1:0;
        liveCount += isCellAlive(row+1, col+1) ? 1:0;

        return liveCount;
    }

    /**
     * Returns true if the cell identified by row and col contains the 
     * value 1.  If the cell's value is zero or it is out of bounds, 
     * false is returned.
     */
    private boolean isCellAlive(int row, int col) {
        boolean bAlive = false;
        if (row < 0 
            || row >= numRows
            || col < 0 
            || col >= numCols) { // out of bounds
            bAlive = false;
        }
        else { // inbounds, check the value
            bAlive = matrix[row][col] == 1;
        }
        return bAlive;
    }

    public void printMatrix() {
        System.out.println("----------------------");
        System.out.println(prettyToString());
    }

    public String prettyToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                sb.append(matrix[i][j]);
                sb.append(DELIMITER);
            }
            sb.append(NEWLINE);
        }
        return sb.toString();
    }

    /**
     * Formats the current state to match the input format.
     * Columns are returned in left to right order.  Rows are separate by
     * commas.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                sb.append(matrix[i][j]);
            }
            if (i < (numRows-1)) {
                sb.append(',');
            }
        }
        return sb.toString();
    }

    /**
     * Does some simple logic testing.
     * These tests are not comprehensive.
     */
    private static void testGame() {
        // Test example from spec document
        String startState = "01000,10011,11001,01000,10001";
        String expectedEndState = "00000,10111,11111,01000,00000";
        testSingleIteration(startState, expectedEndState);

        // Test reproduction
        startState       = "00000,01110,00000,00000,00000";
        expectedEndState = "00100,00100,00100,00000,00000";
        testSingleIteration(startState, expectedEndState);

        // Test counting along the top edge
        startState       = "01110,00000,00000,00000,00000";
        expectedEndState = "00100,00100,00000,00000,00000";
        testSingleIteration(startState, expectedEndState);

        // Test counting along the side edge
        startState       = "00000,00001,00001,00001,00000";
        expectedEndState = "00000,00000,00011,00000,00000";
        testSingleIteration(startState, expectedEndState);

        // Test a non-square matrix
        startState       = "00000,01110,00000";
        expectedEndState = "00100,00100,00100";
        testSingleIteration(startState, expectedEndState);

        System.out.println("Tests passed");
    }

    private static void testSingleIteration(String start, String expectedEnd) {
        Life testLife = new Life(start);
        testLife.nextIteration();
        String actualEnd = testLife.toString();
        assert expectedEnd.equals(actualEnd);
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            Life theLife = new Life(args[0]);
            theLife.nextIteration();
            System.out.println(theLife);
            System.out.println(theLife.prettyToString());
        }
        else {
            Life.testGame();
        }
    }
}

