
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/*----------------------------------------------------------------
 *  Author:        Nitin Gupta
 *  Written:       08/10/2018
 *  Last updated:  08/10/2018
 *  
 *  Perform Percolation find for a given number of times with given grid size. 
 *  Calculate different metrics (mean, standard deviation, confidence using the trial stats.  
 *
 *----------------------------------------------------------------*/
public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] trialStats;                    // Array to hold open sites for trials
    private final double meanVal;
    private final double stddevVal;
    private final double confidenceLoVal;
    private final double confidenceHiVal;
    
    /**
     *   Constructor for PercolationStats class
     *   Initialize trialStats to number of trials
     *   Run Percolation using random site locations and when percolates
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Invalid input");
        }
        
        this.trialStats = new double[trials];
        
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(1, n+1), StdRandom.uniform(1, n+1));
            }
            trialStats[i] = (double) p.numberOfOpenSites() / (double) (n*n);
        }
        meanVal = StdStats.mean(trialStats);
        stddevVal = StdStats.stddev(trialStats);
        confidenceLoVal = meanVal - ((CONFIDENCE_95 * stddevVal) / Math.sqrt(trialStats.length));
        confidenceHiVal = meanVal + ((CONFIDENCE_95 * stddevVal) / Math.sqrt(trialStats.length));
    }
    
    /**
     *   Calculate and return mean of trials 
     */
    public double mean() {
        return meanVal;
    }
    
    /**
     *   Calculate and return standard deviation of trials 
     */
    public double stddev() {
        return stddevVal;
    }
    
    /**
     *   Calculate and return low end point of 95% confidence interval of trials 
     */
    public double confidenceLo() {
        return confidenceLoVal;
    }
    
    /**
     *   Calculate and return high end point of 95% confidence interval of trials 
     */
    public double confidenceHi() {
        return confidenceHiVal;
    }

    /**
     *   Main method to run PercolationStats
     *   Accepts two parameters
     *   Parameter 1: Grid size
     *   Parameter 2: Number of trials
     *   
     *   Prints different statistics after running trials
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Grid size and number of trials required");
        }
        
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.printf("mean                    = %f", ps.mean());
        System.out.println();
        System.out.printf("stddev                  = %f", ps.stddev());
        System.out.println();
        System.out.printf("95%% confidence interval = [%f, %f]", ps.confidenceLo(), ps.confidenceHi());
    }
}
