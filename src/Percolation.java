
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/*----------------------------------------------------------------
 *  Author:        Nitin Gupta
 *  Written:       08/10/2018
 *  Last updated:  08/10/2018
 *  
 *  Calculates Percolation for a given grid of size n*n. 
 *  It is using Weighted Quick Union algorithm for best performance.  
 *
 *----------------------------------------------------------------*/

public class Percolation {
    private final int n;                    // n*n grid size
    private int openSites = 0;              // Number of open sites
    private final WeightedQuickUnionUF wf;  // Weighted Union find object
    private boolean[][] siteData;           // Holds if site is open (true) or blocked (false)
    
    /**
     *   Constructor for Percolation class
     *   Initialize siteData to n*n size
     *   Initialize Union Find Algorithm (n*n is top row dummy site and n*n+1 is bottom row dummy site)
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid input");
        }
        
        this.n = n;
        siteData = new boolean[n][n];       // Initialize site data
        
        //  Initialize Weighted Union find object (Extra two elements for top and bottom dummy sites)
        wf = new WeightedQuickUnionUF(n * n + 2); 
    }
    
    /**
     *   Method to mark given site location as open location.
     *   Connect neighbor open sites to prepare connected tree using Union Find algorithm
     *   
     *   Throws a IllegalArgumentException if location is invalid.
     */
    public void open(int row, int col) {
        // Check if site is already open, then don't do anything
        if (!isOpen(row, col)) {
            siteData[row - 1][col - 1] = true;  // True mean sites is open now
            openSites++; // Increment open site count
            
            int p = getSiteIndex(row, col);     // Get index of current site
            
            // If first row site, then add union with first row dummy site 
            if (row == 1) {
                wf.union(p, n*n);
            }
            
            // If last row site, then add union with last row dummy site
            if (row == n) {
                wf.union(p, n*n+1);
            }
            
            // If previous row site is valid and open, then add union
            if (row > 1 && isOpen(row - 1, col)) {
                wf.union(p, getSiteIndex(row - 1, col));
            }
            
            // If next row site is valid and open, then add union
            if (row < n && isOpen(row + 1, col)) {
                wf.union(p, getSiteIndex(row + 1, col));
            }
            
            // If previous column site is valid and open, then add union
            if (col > 1 && isOpen(row, col - 1)) {
                wf.union(p, getSiteIndex(row, col - 1));
            }
            
            // If next column site is valid and open, then add union
            if (col < n && isOpen(row, col + 1)) {
                wf.union(p, getSiteIndex(row, col + 1));
            }
        }
    }
    
    /**
     *   Returns site status if open or not
     *
     *   Throws a IllegalArgumentException if location is invalid.
     */
    public boolean isOpen(int row, int col) {
        validateCell(row, col);
        return siteData[row - 1][col - 1];
    }
    
    /**
     *   Returns site status if full or not
     *
     *   Throws a IllegalArgumentException if location is invalid.
     */
    public boolean isFull(int row, int col) {
        validateCell(row, col);
        return wf.connected(getSiteIndex(row, col), n*n);
    }
    
    /**
     *   Checks if a site location exists or not
     *
     *   Throws a IllegalArgumentException if location is invalid.
     */
    private void validateCell(int row, int col) {
        if (row > n || col > n || row < 1 || col < 1) {
            throw new IllegalArgumentException("Outside of prescribed range");
        }
    }
    
    /**
     *   Get site index given row and col position
     */
    private int getSiteIndex(int row, int col) {
        return (row - 1) * n + col - 1;
    }
    
    /**
     *   Return number of open sites
     */
    public int numberOfOpenSites() {
        return openSites;
    }
    
    /**
     *   Using the tree prepared using Union Find algorithm, find if grid is percolated.
     *   If top and bottom dummy sites are connected, meaning grid is percolated.
     */
    public boolean percolates() {
        return wf.connected(n * n, n * n + 1);
    }
    
    /**
     *   Main method for testing the program
     */
    public static void main(String[] main) {
        Percolation p = new Percolation(5);
        p.open(1, 1);
        p.open(1, 3);
        p.open(2, 2);
        p.open(2, 3);
        p.open(3, 3);
        p.open(4, 3);
        p.open(4, 1);
        p.open(4, 2);
        p.open(3, 2);
        System.out.println(p.percolates());
    }
}
