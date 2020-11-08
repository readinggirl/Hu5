package hausuebung5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Please enter here an answer to task four between the tags:
 * <answerTask4>
 *    Hier sollte die Antwort auf die Aufgabe 4 stehen.
 * </answerTask4>
 */
public class SudokuSolver implements ISodukoSolver {

    public SudokuSolver() {
        //initialize if necessary
    }

    public long benchmark(int[][] rawSudoku) {
        long diff = 0;
        for (int i = 0; i < 10; i++) {
            long startTime = System.currentTimeMillis();
            solveSudoku(rawSudoku);
            long endTime = System.currentTimeMillis();
            diff = endTime - startTime;
            System.out.println("Total execution time: " + (diff) + "ms");
        }
        return diff;
    }

    @Override
    public final int[][] readSudoku(File file) {
        // implement this method
        int[][] sudoku = new int[9][9];
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                for (int i = 0; i < 9; i++) {
                    String[] val = line.split(";");
                    for (int j = 0; j < 9; j++) {
                        sudoku[i][j] = Integer.parseInt(val[j]);
                    }
                    line = br.readLine();
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sudoku;
    }

    @Override
    public boolean checkSudoku(int[][] rawSudoku) {
        // row checker
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 8; col++) {
                for (int col2 = col + 1; col2 < 9; col2++) {
                    if (rawSudoku[row][col] == rawSudoku[row][col2]) {
                        return false;
                    }
                }
            }
        }

        // column checker
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 8; row++) {
                for (int row2 = row + 1; row2 < 9; row2++) {
                    if (rawSudoku[row][col] == rawSudoku[row2][col]) {
                        return false;
                    }
                }
            }
        }

        // grid checker
        for (int row = 0; row < 9; row += 3) {
            for (int col = 0; col < 9; col += 3) // row, col is start of the 3 by 3 grid
            {
                for (int pos = 0; pos < 8; pos++) {
                    for (int pos2 = pos + 1; pos2 < 9; pos2++) {
                        if (rawSudoku[row + pos % 3][col + pos / 3] == rawSudoku[row + pos2 % 3][col + pos2 / 3]) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public int[][] solveSudoku(int[][] rawSudoku) {
        solveSuduko(rawSudoku, 0, 0);
        return rawSudoku;
    }

    @Override
    public int[][] solveSudokuParallel(int[][] rawSudoku) {
        // implement this method
        return new int[0][0]; // delete this line!
    }

    // add helper methods here if necessary
    static boolean solveSuduko(int grid[][], int row, int col) {
        final int N = 9;
        /*if we have reached the 8th
		row and 9th column (0
		indexed matrix) ,
		we are returning true to avoid further
		backtracking	 */
        if (row == N - 1 && col == N) {
            return true;
        }

        // Check if column value becomes 9 ,
        // we move to next row
        // and column start from 0
        if (col == N) {
            row++;
            col = 0;
        }

        // Check if the current position
        // of the grid already
        // contains value >0, we iterate
        // for next column
        if (grid[row][col] != 0) {
            return solveSuduko(grid, row, col + 1);
        }

        for (int num = 1; num < 10; num++) {

            // Check if it is safe to place
            // the num (1-9) in the
            // given row ,col ->we move to next column
            if (isSafe(grid, row, col, num)) {

                /* assigning the num in the current
				(row,col) position of the grid and
				assuming our assined num in the position
				is correct */
                grid[row][col] = num;

                // Checking for next
                // possibility with next column
                if (solveSuduko(grid, row, col + 1)) {
                    return true;
                }
            }
            /* removing the assigned num , since our
			assumption was wrong , and we go for next
			assumption with diff num value */
            grid[row][col] = 0;
        }
        return false;
    }

    static boolean isSafe(int[][] grid, int row, int col, int num) {

        // Check if we find the same num
        // in the similar row , we
        // return false
        for (int x = 0; x <= 8; x++) {
            if (grid[row][x] == num) {
                return false;
            }
        }

        // Check if we find the same num
        // in the similar column ,
        // we return false
        for (int x = 0; x <= 8; x++) {
            if (grid[x][col] == num) {
                return false;
            }
        }

        // Check if we find the same num
        // in the particular 3*3
        // matrix, we return false
        int startRow = row - row % 3, startCol
                = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }

        return true;
    }
}
