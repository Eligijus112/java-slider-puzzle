import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.lang.reflect.Array;
import java.util.Arrays;

import static java.lang.Math.abs;

public class Board {
    // Placeholder for the current board
    private int[][] board;

    // The number of rows and columns in the board
    private int n;

    // A very high integer for the 0 element in the board
    private int highInt = 9999;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // Checking whether n is between 2 and 128
        if ((tiles.length < 2) | (tiles.length > 128)) {
            throw new IllegalArgumentException();
        }

        // Checking if the tiles object is a square
        if (tiles.length != tiles[0].length) {
            throw new IllegalArgumentException();
        }

        // Initiating the empty board object
        board = new int[tiles.length][tiles[0].length];

        // Saving the number of rows and columns of the square puzzle
        n = tiles.length;

        // Populating the board row by row
        for (int row = 0; row < tiles.length; row++) {
            System.arraycopy(tiles[row], 0, board[row], 0, tiles[0].length);
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(dimension()).append("\n");
        for (int[] row : board) {
            for (int col = 0; col < board.length; col++)
                str.append(String.format("%2d ", row[col]));
            str.append("\n");
        }
        return str.toString();
    }

    // Returns the dimension of the board
    public int dimension() {
        return n;
    }

    // The hamming distance between the current board and the goal board
    public int hamming() {
        // Flattening the board
        int[] boardFlat = new int[n * n];
        int i = 0;
        for (int[] row : board) {
            for (int col = 0; col < board.length; col++) {
                int element = row[col];
                if (element == 0) {
                    // Storing the 0 index as a very big int
                    element = highInt;
                }
                boardFlat[i] = element;
                i++;
            }
        }

        // Making a copy of the flattened board
        int[] boardFlatSorted = boardFlat.clone();

        // Sorting the board
        Arrays.sort(boardFlatSorted);

        // Counting the number of out of place elements
        int counter = 0;
        for (int index = 0; index < boardFlat.length; i++) {
            if ((boardFlat[index] != boardFlatSorted[index]) & (boardFlat[index] != highInt)) {
                counter++;
            }
            index++;
        }
        return counter;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        // Flattening the board
        int[] boardFlat = new int[n * n];
        int i = 0;
        for (int[] row : board) {
            for (int col = 0; col < board.length; col++) {
                int element = row[col];
                if (element == 0) {
                    // Storing the 0 index as a very big int
                    element = highInt;
                }
                boardFlat[i] = element;
                i++;
            }
        }

        // Sorting the board
        Arrays.sort(boardFlat);

        // Making a 2d board from the sorted board
        int[][] boardSorted = new int[n][n];
        i = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                boardSorted[row][col] = boardFlat[i];
                i++;
            }
        }

        // Calculating the distances between each element and the goal board
        int manhattanDistance = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int element = board[row][col];
                if (element == 0) {
                    element = highInt;
                }
                // Searching for the item in the sorted board
                for (int row2 = 0; row2 < n; row2++) {
                    for (int col2 = 0; col2 < n; col2++) {
                        int element2 = boardSorted[row2][col2];
                        if ((element == element2) & (element != highInt)) {
                            manhattanDistance += abs(row2 - row) + abs(col2 - col);
                            break;
                        }
                    }
                }
            }
        }
        return manhattanDistance;
    }

    // Method to check whether two boards are equal
    public boolean equals(Object y) {
        // Checking if the object classes are the same
        if (getClass() != y.getClass()) {
            return false;
        }

        // Checking the length of the board
        if (n != Array.getLength(y)) {
            return false;
        }

        // Converting the Java object to the same class (board)
        Board otherBoard = (Board) y;

        // Checking each coordinate
        return Arrays.deepEquals(board, otherBoard.board);
    }

    // Method indicating whether the board is the goal board
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // Printing the string representation of the board
        StdOut.println(initial);

        // Printing out the humming distance
        StdOut.println("Humming distance: " + initial.hamming());

        // Printing out the manhattan distance
        StdOut.println("Manhattan distance: " + initial.manhattan());

        // Checking if the uploaded board is the goal board
        StdOut.println("Is this board the goal?: " + initial.isGoal());
    }
}
