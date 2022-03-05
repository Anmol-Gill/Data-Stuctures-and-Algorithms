import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// Models an N-by-N percolation system.
public class Percolation {

    private WeightedQuickUnionUF uf1;
    private WeightedQuickUnionUF uf2;
    private boolean[][] box;
    private int counter = 0;
    private int size;
    private int x;
    private int N;

    // Create an N-by-N grid, with all sites blocked.
    public Percolation(int N) {
        this.size = N;
        this.box = new boolean[size][size];
        this.x = (size * size) + 1;

        uf1 = new WeightedQuickUnionUF(size * size + 2);
        uf2 = new WeightedQuickUnionUF(size * size + 2);

        for (int i = 0; i < size; i++) {
            uf1.union(encode(0, i), 0);
            uf2.union(encode(0, i), 0);
        }

        for (int j = 0; j < size; j++) {
            uf1.union(encode(N - 1, j), x);
        }
    }

    // Open site (row, col) if it is not open already.
    public void open(int row, int col) {
        box[row][col] = true;
        counter += 1;
        if ((row - 1) >= 0 && isOpen(row - 1, col)) {
            uf1.union(encode(row, col), encode(row - 1, col));
            uf2.union(encode(row, col), encode(row - 1, col));
        }
        if ((row + 1) < size && isOpen(row + 1, col)) {
            uf1.union(encode(row, col), encode(row + 1, col));
            uf2.union(encode(row, col), encode(row + 1, col));
        }
        if ((col - 1) >= 0 && isOpen(row, col - 1)) {
            uf1.union(encode(row, col), encode(row, col - 1));
            uf2.union(encode(row, col), encode(row, col - 1));
        }
        if ((col + 1) < size && isOpen(row, col + 1)) {
            uf1.union(encode(row, col), encode(row, col + 1));
            uf2.union(encode(row, col), encode(row, col + 1));
        }

    }

    // Is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < N) {
            throw new IndexOutOfBoundsException("Argument outside prescribed range");
        }
        return box[row][col];
    }

    // Is site (row, col) full?
    public boolean isFull(int row, int col) {
        return uf2.connected(0, encode(row, col));
    }

    // Number of open sites.
    public int numberOfOpenSites() {
        return counter;
    }

    // Does the system percolate?
    public boolean percolates() {
        return uf1.connected(0, x);
    }

    // An integer ID (1...N) for site (row, col).
    private int encode(int row, int col) {
        return size * row + col + 1;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        } else {
            StdOut.println("does not percolate");
        }

        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}
