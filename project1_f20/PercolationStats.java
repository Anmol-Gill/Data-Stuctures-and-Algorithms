import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {

    private int N;
    private int T;
    private double[] result;
    private Percolation percolation;

    // Perform T independent experiments (Monte Carlo simulations) on an
    // N-by-N grid.
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Both N and T must be greater than 0.");
        }

        this.N = N;
        this.T = T;
        this.result = new double[T];

        for (int i = 0; i < T; i++) {
            this.percolation = new Percolation(N);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                percolation.open(row, col);
            }
            double x = (double) (percolation.numberOfOpenSites()) / (double) (N * N);
            result[i] = x;
        }
    }

    // Sample mean of percolation threshold.
    public double mean() {
        return StdStats.mean(result);
    }

    // Sample standard deviation of percolation threshold.
    public double stddev() {
        return StdStats.stddev(result);
    }

    // Low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        return mean() - (1.96 * stddev()) / Math.sqrt(T);
    }

    // High endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        return mean() + (1.96 * stddev()) / Math.sqrt(T);
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());
    }
}
