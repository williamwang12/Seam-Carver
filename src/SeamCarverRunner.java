import java.awt.Color;

public class SeamCarverRunner {
	private static final boolean HORIZONTAL   = true;
    private static final boolean VERTICAL     = false;
	
	public static void main(String[] args)
	{
		// Use this main method and the included methods to test your code.
		Picture test = new Picture("./input/HJocean.png");
		printEnergy(test);
		showEnergy(test);
		showSeams(test);
		printSeams(test);
		resizeDemo(test, 2, 2);
	}
	
	/* Computes and prints a table of the energy of an image
	 * Takes a Picture as its parameter
	 */
	public static void printEnergy(Picture picture)
	{
		System.out.printf("%d-by-%d image\n", picture.width(), picture.height());
        
        SeamCarver sc = new SeamCarver(picture);
        
        System.out.printf("Printing energy calculated for each pixel.\n");        

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.0f ", sc.energy(col, row));
            System.out.println();
        }

	}
	
	/* Computes and draws the energy of an image
	 * Takes a Picture as its parameter
	 */
	public static void showEnergy(Picture picture)
	{
		System.out.printf("%d-by-%d image\n", picture.width(), picture.height());
        picture.show();
        StdDraw.pause(100);
        SeamCarver sc = new SeamCarver(picture);
        
        System.out.printf("Displaying energy calculated for each pixel.\n");
        SCUtility.showEnergy(sc);
	}
	
	/* Computes the horizontal seam, vertical seam, and energy of an image.
	 * Takes a Picture as its parameter
	 * Draws the horizontal and vertical seams over the energy.
	 */
	public static void showSeams(Picture picture)
	{
		System.out.printf("%d-by-%d image\n", picture.width(), picture.height());
        picture.show();        
        SeamCarver sc = new SeamCarver(picture);
        
        System.out.printf("Displaying horizontal seam calculated.\n");
        showHorizontalSeam(sc);

        System.out.printf("Displaying vertical seam calculated.\n");
        showVerticalSeam(sc);
	}
	
	private static void showHorizontalSeam(SeamCarver sc) {
        Picture ep = SCUtility.toEnergyPicture(sc);
        int[] horizontalSeam = sc.findHorizontalSeam();
        Picture epOverlay = SCUtility.seamOverlay(ep, true, horizontalSeam);
        epOverlay.show();
    }

    private static void showVerticalSeam(SeamCarver sc) {
        Picture ep = SCUtility.toEnergyPicture(sc);
        int[] verticalSeam = sc.findVerticalSeam();
        Picture epOverlay = SCUtility.seamOverlay(ep, false, verticalSeam);
        epOverlay.show();
    }

    /* Computes the horizontal seam, vertical seam, and energy of an image.
     * Takes a Picture as a parameter
     * Prints the horizontal and vertical seams as annotations to the energy.
     */
	public static void printSeams(Picture picture) {
		StdOut.printf("(%d-by-%d image)\n", picture.width(), picture.height());
        StdOut.println();
        StdOut.println("The table gives the dual-gradient energies of each pixel.");
        StdOut.println("The asterisks denote a minimum energy vertical or horizontal seam.");
        StdOut.println();

        SeamCarver carver = new SeamCarver(picture);
        
        StdOut.printf("Vertical seam: { ");
        int[] verticalSeam = carver.findVerticalSeam();
        for (int x : verticalSeam)
            StdOut.print(x + " ");
        StdOut.println("}");
        printSeam(carver, verticalSeam, VERTICAL);

        StdOut.printf("Horizontal seam: { ");
        int[] horizontalSeam = carver.findHorizontalSeam();
        for (int y : horizontalSeam)
            StdOut.print(y + " ");
        StdOut.println("}");
        printSeam(carver, horizontalSeam, HORIZONTAL);
	}
	
	private static void printSeam(SeamCarver carver, int[] seam, boolean direction) {
        double totalSeamEnergy = 0.0;

        for (int row = 0; row < carver.height(); row++) {
            for (int col = 0; col < carver.width(); col++) {
                double energy = carver.energy(col, row);
                String marker = " ";
                if ((direction == HORIZONTAL && row == seam[col]) ||
                    (direction == VERTICAL   && col == seam[row])) {
                    marker = "*";
                    totalSeamEnergy += energy;
                }
                StdOut.printf("%7.2f%s ", energy, marker);
            }
            StdOut.println();
        }                
        // StdOut.println();
        StdOut.printf("Total energy = %f\n", totalSeamEnergy);
        StdOut.println();
        StdOut.println();
    }
	
	/* Uses your seam removal methods to resize the image. 
	 * Parameters:
	 *    Picture, removeColumns, removeRows
	 */
	public static void resizeDemo(Picture picture, int removeColumns, int removeRows)
	{
		StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
        SeamCarver sc = new SeamCarver(picture);
 
        Stopwatch sw = new Stopwatch();
 
        for (int i = 0; i < removeRows; i++) {
            int[] horizontalSeam = sc.findHorizontalSeam();
            sc.removeHorizontalSeam(horizontalSeam);
        }
 
        for (int i = 0; i < removeColumns; i++) {
            int[] verticalSeam = sc.findVerticalSeam();
            sc.removeVerticalSeam(verticalSeam);
        }
 
        StdOut.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());
 
        StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
        picture.show();
        sc.picture().show(); 
		
	}
	static class SCUtility {

	    // create random W-by-H picture
	    public static Picture randomPicture(int W, int H) {
	        Picture picture = new Picture(W, H);
	        for (int col = 0; col < W; col++) {
	            for (int row = 0; row < H; row++) {
	                int r = StdRandom.uniform(256);
	                int g = StdRandom.uniform(256);
	                int b = StdRandom.uniform(256);
	                Color color = new Color(r, g, b);
	                picture.set(col, row, color);
	            }
	        }
	        return picture;
	    }

	    // convert SeamCarver picture to W-by-H energy matrix
	    public static double[][] toEnergyMatrix(SeamCarver sc) {
	        double[][] a = new double[sc.width()][sc.height()];
	        for (int col = 0; col < sc.width(); col++)
	            for (int row = 0; row < sc.height(); row++)
	                a[col][row] = sc.energy(col, row);
	        return a;        
	    }

	    // displays grayscale values as energy (converts to picture, calls show)
	    public static void showEnergy(SeamCarver sc) {
	        doubleToPicture(toEnergyMatrix(sc)).show();
	    }

	    // returns picture of energy matrix associated with SeamCarver picture
	    public static Picture toEnergyPicture(SeamCarver sc) {
	        double[][] energyMatrix = toEnergyMatrix(sc);
	        return doubleToPicture(energyMatrix);
	    }

	    // converts a double matrix of values into a normalized picture
	    // values are normalized by the maximum grayscale value
	    public static Picture doubleToPicture(double[][] grayValues) {

	        //each 1D array in the matrix represents a single column, so number
	        //of 1D arrays is the width, and length of each array is the height
	        int width = grayValues.length;
	        int height = grayValues[0].length;

	        Picture picture = new Picture(width, height);

	        double maxVal = 0;
	        for (int col = 0; col < width; col++) {
	            for (int row = 0; row < height; row++) {
	                if (grayValues[col][row] > maxVal) {
	                    maxVal = grayValues[col][row];
	                }
	            }
	        }
	            
	        if (maxVal == 0)
	            return picture;  // return black picture

	        for (int col = 0; col < width; col++) {
	            for (int row = 0; row < height; row++) {
	                float normalizedGrayValue = (float) grayValues[col][row] / (float) maxVal;
	                Color gray = new Color(normalizedGrayValue, normalizedGrayValue, normalizedGrayValue);
	                picture.set(col, row, gray);
	            }
	        }

	        return picture;
	    }


	    // This method is useful for debugging seams. It overlays red
	    // pixels over the calculate seam.
	    public static Picture seamOverlay(Picture picture, boolean isHorizontal, int[] seam) {
	        Picture overlaid = new Picture(picture);
	        
	        //if horizontal seam, set one pixel in every column
	        if (isHorizontal) {
	            for (int col = 0; col < picture.width(); col++)
	                overlaid.set(col, seam[col], Color.RED);
	        }

	        // if vertical seam, set one pixel in each row
	        else {
	            for (int row = 0; row < picture.height(); row++)
	                overlaid.set(seam[row], row, Color.RED);
	        }

	        return overlaid;
	    }

	}
}
