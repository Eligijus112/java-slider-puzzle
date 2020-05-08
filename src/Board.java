import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {
    // Placeholder for the current board
    private final int[][] board;

    // The number of rows and columns in the board
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // Checking whether n is between 2 and 128
        if ((tiles.length < 2) || (tiles.length > 128)) {
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
        // Starting the counter
        int counter = 0;

        // The element index to check because the 0 element will be at the back
        int i = 1;

        // Iterating through the board and checking out of place elements
        for (int[] row : board) {
            for (int col = 0; col < board.length; col++) {
                if (row[col] != i && i != n * n) {
                    counter++;
                }
                i++;
            }
        }

        return counter;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        // Calculating the distances between each element and the goal board
        int manhattanDistance = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int element = board[row][col];
                if (element != 0) {
                    // Inferring the position of the element where it should be on a sorted board
                    // Row
                    int x = (element - 1) / n;

                    // Column
                    int y = (element - 1) % n;

                    // Adding the difference to the manhattan distance
                    manhattanDistance += Math.abs(row - x) + Math.abs(col - y);
                }
            }
        }
        return manhattanDistance;
    }

    // Method to check whether two boards are equal
    public boolean equals(Object y) {
        // Checking if the argument is null
        if (y == null) {
            return false;
        }

        // Checking if the object classes are the same
        if (getClass() != y.getClass()) {
            return false;
        }

        // Converting the Java object to the same class (board)
        Board otherBoard = (Board) y;

        // Checking the length of the board
        if (n != otherBoard.n) {
            return false;
        }

        // Checking each coordinate
        return Arrays.deepEquals(board, otherBoard.board);
    }

    // Method indicating whether the board is the goal board
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // Returns an iterable containing all possible boards moving the blank square by 1 tile
    // The total number of neighbours can be 2, 3 or 4
    public Iterable<Board> neighbors() {
        // Finding the index of the blank square
        int rowBlank = 0;
        int colBlank = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (board[row][col] == 0) {
                    rowBlank = row;
                    colBlank = col;
                    break;
                }
            }
        }

        // Creating an iterative list to hold the boards
        Stack<Board> neighbours = new Stack<>();

        // Creating the neighbours

        // Neighbour up
        if (rowBlank != 0) {
            // Creating a copy of the current board
            int[][] neighbour = copyBoard();

            // Swapping the blank element
            swap(neighbour, rowBlank, colBlank, rowBlank - 1, colBlank);

            // Appending to the neighbours iterable
            neighbours.push(new Board(neighbour));
        }

        // Neighbour down
        if (rowBlank != n - 1) {
            // Creating a copy of the current board
            int[][] neighbour = copyBoard();

            // Swapping the blank element
            swap(neighbour, rowBlank, colBlank, rowBlank + 1, colBlank);

            // Appending to the neighbours iterable
            neighbours.push(new Board(neighbour));
        }

        // Neighbour to the left
        if (colBlank != 0) {
            // Creating a copy of the current board
            int[][] neighbour = copyBoard();

            // Swapping the blank element
            swap(neighbour, rowBlank, colBlank, rowBlank, colBlank - 1);

            // Appending to the neighbours iterable
            neighbours.push(new Board(neighbour));
        }

        // Neighbour to the right
        if (colBlank != n - 1) {
            // Creating a copy of the current board
            int[][] neighbour = copyBoard();

            // Swapping the blank element
            swap(neighbour, rowBlank, colBlank, rowBlank, colBlank + 1);

            // Appending to the neighbours iterable
            neighbours.push(new Board(neighbour));
        }

        return neighbours;
    }

    // Function to swap elements in the board
    private void swap(int[][] board, int x0, int y0, int x1, int y1) {
        // Swapping the elements
        int element0 = board[x0][y0];
        board[x0][y0] = board[x1][y1];
        board[x1][y1] = element0;
    }

    // Method to copy a board
    private int[][] copyBoard() {
        int[][] boardCopy = new int[n][n];

        // Populating the copy
        for (short i = 0; i < n; i++)
            System.arraycopy(board[i], 0, boardCopy[i], 0, n);

        return boardCopy;
    }

    // Twin method
    public Board twin() {
        // Creating a copy
        int[][] twinBoard = copyBoard();

        // Starting from the first row and column
        int row;
        int col = 0;

        // Iterating through the board to obtain the first index that fits
        for (row = 0; row < n; row++) {
            for (col = 0; col < n - 1; col++) {
                // Stopping if the conditions are met
                if (twinBoard[row][col] != 0 && twinBoard[row][col + 1] != 0) {
                    break;
                }
            }
            // Stopping if the conditions are met
            if (col < n - 1) {
                break;
            }
        }

        // Extracting the element to swap
        int element1 = twinBoard[row][col];
        int element2 = twinBoard[row][col + 1];

        twinBoard[row][col + 1] = element1;
        twinBoard[row][col] = element2;

        return new Board(twinBoard);
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

        // Creating the neighbours
        Iterable<Board> neighbours = initial.neighbors();

        // Neighbours
        for (Board neighbour : neighbours) {
            StdOut.println("Neighbour: " + neighbour.toString());
        }

        // Twin board
        Board twin = initial.twin();
        StdOut.println("A twin board: " + twin.toString());
    }
}
