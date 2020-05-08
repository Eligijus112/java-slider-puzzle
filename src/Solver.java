import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;


public class Solver {
    // Iterable object to store the boards outputed by the solver
    private final Queue<Board> boardSequence = new Queue<>();

    // Boolean indicating whether the board can be solved or not
    private boolean isSolvableVariable;

    // Moves needed to solve the board
    private int movesToSolve;

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvableVariable;
    }

    // Creating a node class that has the priority calculations and the compareTo method
    private static class Node implements Comparable<Node> {
        Board board;
        int move;
        Node previous;

        public Node(Board board, int move, Node previous) {
            this.board = board;
            this.move = move;
            this.previous = previous;
        }

        // Overriding the compareTo method from the Comparable class
        @Override
        public int compareTo(Node that) {
            return Integer.compare(this.getPriority(), that.getPriority());
        }

        // Priority calculation
        private int getPriority() {
            return move + board.manhattan();
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        // Creating the priority queue placeholder for the main puzzle
        MinPQ<Node> PQ = new MinPQ<>();

        // Creating a placeholder for the twin board
        MinPQ<Node> TwinPQ = new MinPQ<>();

        // Creating the initial Node object
        Node initialNode = new Node(initial, 0, null);

        // Creating the initial node with the twin
        Node initialNodeTwin = new Node(initial.twin(), 0, null);

        // Adding the initial board as a Node in to the PQ and the twinPQ
        PQ.insert(initialNode);
        TwinPQ.insert(initialNodeTwin);

        // Initiating the A* algorithm
        while (true) {
            // Getting the node with the lowest priority
            Node node = PQ.delMin();

            // Adding to the board sequence
            boardSequence.enqueue(node.board);

            // Checking if the extracted node is the goal board
            if (node.board.isGoal()) {
                // Changing the global solution variable
                isSolvableVariable = true;

                // Saving the moves needed to solve
                movesToSolve = node.move;

                // When we find the solution we break the cycle
                return;
            } else {
                node.move++;

                // Creating all the neighbors
                Iterable<Board> neighbors = node.board.neighbors();

                // Iterating through all of them and adding the ones that are not the same
                for (Board b : neighbors) {
                    if (node.previous != null && b.equals(node.previous.board)) {
                    } else {
                        PQ.insert(new Node(b, node.move, node));
                    }
                }
            }

            // The same algorithm in lockheed fashion done for the twin board
            Node nodeTwin = TwinPQ.delMin();
            if (nodeTwin.board.isGoal()) {
                // Changing the global solution variable
                isSolvableVariable = false;

                // When we find the solution we break the cycle
                return;
            } else {

                nodeTwin.move++;

                // Creating all the neighbors
                Iterable<Board> neighborsTwin = nodeTwin.board.neighbors();

                // Iterating through all of them and adding the ones that are not the same
                for (Board bt : neighborsTwin) {
                    if (nodeTwin.previous != null && bt.equals(nodeTwin.previous.board)) {
                    } else {
                        TwinPQ.insert(new Node(bt, nodeTwin.move++, nodeTwin));
                    }
                }
            }
        }
    }

    // Solution to the puzzle
    public Iterable<Board> solution() {
        if (isSolvableVariable)
            return boardSequence;
        else
            return null;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvableVariable) {
            return movesToSolve;
        } else
            return -1;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // Solving the puzzle
        Solver solved = new Solver(initial);

        // Is the board solvable?
        StdOut.println("Is the board solvable?: " + solved.isSolvable());

        // Moves it takes to solve the puzzle
        StdOut.println("Number of moves to solve?: " + solved.moves());

        // Path to solving
        for (Board board : solved.solution()) {
            StdOut.println(board.toString());
        }
    }
}
