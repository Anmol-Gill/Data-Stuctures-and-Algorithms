import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.StdOut;

// Models a board in the 8-puzzle game or its generalization.
public class Board {
    ///.....
    private final int[][] tiles;
    private int N;
    private int hamming;
    private int manhattan;

    // Construct a board from an N-by-N array of tiles, where
    // tiles[i][j] = tile at row i and column j, and 0 represents the blank
    // square.
    public Board(int[][] tiles) {
        ///...
        this.tiles = tiles;
        N = this.tiles.length;
        hamming = 0;
        manhattan = 0;
        int result = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                result = (i == N - 1 && j == N - 1) ? 0 : result + 1;
                int current = tiles[i][j];
                if (result != 0 && current != result) {
                    hamming++;
                }
                if (current != 0) {
                    manhattan += Math.abs((current - 1) / N - i)
                            + Math.abs((current - 1) % N - j);
                }
            }
        }
    }

    // Tile at row i and column j.
    public int tileAt(int i, int j) {
        ///...
        return tiles[i][j];
    }

    // Size of this board.
    public int size() {
        ///...
        return N;
    }

    // Number of tiles out of place.
    public int hamming() {
        ///...
        return hamming;
    }

    // Sum of Manhattan distances between tiles and goal.
    public int manhattan() {
        ///...
        return manhattan;
    }

    // Is this board the goal board?
    public boolean isGoal() {
        ///...
        int result = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                result = (i == N - 1 && j == N - 1) ? 0 : result + 1;
                if (tiles[i][j] != result) {
                    return false;
                }
            }
        }
        return true;
    }

    // Is this board solvable?
    public boolean isSolvable() {
        ///...
        if (N % 2 != 0) {
            return inversions() % 2 == 0;
        } else {
            return ((blankPos() - 1) / N + inversions()) % 2 != 0;
        }
    }

    // Does this board equal that?
    public boolean equals(Board that) {
        ///...
        if (that == this) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (that.getClass() != this.getClass()) {
            return false;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // All neighboring boards.
    public Iterable<Board> neighbors() {
        ///...
        LinkedQueue<Board> queue = new LinkedQueue<Board>();
        int blankPos = blankPos();
        int i = (blankPos - 1) / N, j = (blankPos - 1) % N;
        if (i - 1 >= 0) {
            int[][] clone = cloneTiles();
            int temp = clone[i - 1][j];
            clone[i - 1][j] = clone[i][j];
            clone[i][j] = temp;
            queue.enqueue(new Board(clone));
        }
        if (j + 1 < N) {
            int[][] clone = cloneTiles();
            int temp = clone[i][j + 1];
            clone[i][j + 1] = clone[i][j];
            clone[i][j] = temp;
            queue.enqueue(new Board(clone));
        }
        if (i + 1 < N) {
            int[][] clone = cloneTiles();
            int temp = clone[i + 1][j];
            clone[i + 1][j] = clone[i][j];
            clone[i][j] = temp;
            queue.enqueue(new Board(clone));
        }
        if (j - 1 >= 0) {
            int[][] clone = cloneTiles();
            int temp = clone[i][j - 1];
            clone[i][j - 1] = clone[i][j];
            clone[i][j] = temp;
            queue.enqueue(new Board(clone));
        }
        return queue;
    }

    // String representation of this board.
    public String toString() {
        String s = N + "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s += String.format("%2d", tiles[i][j]);
                if (j < N - 1) {
                    s += " ";
                }
            }
            if (i < N - 1) {
                s += "\n";
            }
        }
        return s;
    }

    // Helper method that returns the position (in row-major order) of the
    // blank (zero) tile.
    private int blankPos() {
        ///...
        int position = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                position++;
                if (tiles[i][j] == 0) {
                    return position;
                }
            }
        }
        return -1;
    }

    // Helper method that returns the number of inversions.
    private int inversions() {
        ///...
        int inversion = 0;
        int tilePos = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int t1 = tiles[i][j];
                tilePos++;
                int tilePos2 = 0;
                for (int k = 0; k < N; k++) {
                    for (int l = 0; l < N; l++) {
                        int t2 = tiles[k][l];
                        tilePos2++;
                        if (t1 != 0 && t2 != 0 && t2 < t1 && tilePos2 > tilePos) {
                            inversion++;
                        }
                    }
                }
            }
        }
        return inversion;
    }

    // Helper method that clones the tiles[][] array in this board and
    // returns it.
    private int[][] cloneTiles() {
        ///...
        int[][] clone = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                clone[i][j] = tiles[i][j];
            }
        }
        return clone;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
        StdOut.println(board.isSolvable());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
    }
}
