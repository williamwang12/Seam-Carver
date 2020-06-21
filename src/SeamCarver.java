import java.awt.*;
import java.util.*;

public class SeamCarver {
	
    Picture picture;
    Color[][] colors;
    double[][] energies;
    int width;
    int height;

	public SeamCarver(Picture picture) {
        this.picture = picture;
        this.height = picture.height();
        this.width = picture.width();



        this.colors = new Color[picture.height()][picture.width()];
        this.energies = new double[picture.height()][picture.width()];

        for (int i = 0; i < picture.height(); i++){
            for (int a = 0; a < picture.width(); a++){
                colors[i][a] = picture.get(a,i);
            }
        }

    }

    public Picture picture() {
        return picture;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int x, int y) {
	    if (x < 0 || x >= width() || y < 0 || y >= height()){
	        throw new IndexOutOfBoundsException();
        }
        if (x == 0 || y == 0 || x >= (width() - 1) || y >= (height() - 1)) {
            return 1000;
        }


        double temp1 = (Math.pow((double)colors[y][x+1].getRed() - (double)colors[y][x-1].getRed(), 2)
                + Math.pow((double)colors[y][x+1].getGreen() - (double)colors[y][x-1].getGreen(), 2)
                + Math.pow((double)colors[y][x+1].getBlue() - (double)colors[y][x-1].getBlue(), 2));

        double temp2 = (Math.pow((double)colors[y+1][x].getRed() - (double)colors[y-1][x].getRed(), 2)
                + Math.pow((double)colors[y+1][x].getGreen() - (double)colors[y-1][x].getGreen(), 2)
                + Math.pow((double)colors[y+1][x].getBlue() - (double)colors[y-1][x].getBlue(), 2));


        return Math.sqrt(temp1 + temp2);
    }

    public int[] findHorizontalSeam() {
        int[] coordinates = new int[width()];

        int c = 0;
        ArrayList<Double> childEnergies = new ArrayList<>();

        ArrayList<Double> seamEnergies = new ArrayList<Double>();
        ArrayList<ArrayList<Integer>> seamsCoords = new ArrayList<ArrayList<Integer>>();

        ArrayList<Integer> currentCoord;

        double totalEnergy = 0.0;
        for (int i = 0; i < height(); i++){

            for (int a = 0; a < width(); a++){
                energies[i][a] = energy(a, i);
            }
        }

        for (int f = 1; f < height() - 1; f++){

            currentCoord = new ArrayList<Integer>();
            currentCoord.add(f);
            totalEnergy = 0;
            c = f;
            for (int z = 1; z < width(); z++){

                int cPlus1 = c + 1 >= width() ? c : c + 1;
                int cMinus1 = c - 1 < 0 ? 0 : c - 1;
                childEnergies = new ArrayList<Double>();
                childEnergies.add(energy(z, cPlus1));
                childEnergies.add(energy(z, c));
                childEnergies.add(energy(z, cMinus1));
                double minEnergy = 100000000;

                for (Double element : childEnergies){
                    if (element < minEnergy){
                        minEnergy = element;
                    }
                }


                totalEnergy += minEnergy;

                if (cPlus1 != c && minEnergy == energy(z, cPlus1)){
                    c = c + 1;
                }
                else if (cMinus1 != c && minEnergy == energy(z, cMinus1)) {
                    c = c - 1;
                }

                currentCoord.add(c);
            }
            seamEnergies.add(totalEnergy);
            seamsCoords.add(currentCoord);

        }

        double min  = Integer.MAX_VALUE;
        int mindex = 0;
        for(int l = 0; l < seamEnergies.size(); l++){
            if (seamEnergies.get(l) < min){
                min = seamEnergies.get(l);
                mindex = l;
            }
        }

        System.out.println("");
        for (int h = 0; h < seamsCoords.get(mindex).size(); h++){
            coordinates[h] = seamsCoords.get(mindex).get(h);
        }

        return coordinates;
    }

    public int[] findVerticalSeam() {
        int[] coordinates = new int[height()];

        int c = 0;
        ArrayList<Double> childEnergies = new ArrayList<>();

        ArrayList<Double> seamEnergies = new ArrayList<Double>();
        ArrayList<ArrayList<Integer>> seamsCoords = new ArrayList<ArrayList<Integer>>();

        ArrayList<Integer> currentCoord;

        double totalEnergy = 0.0;
        for (int i = 0; i < height(); i++){

            for (int a = 0; a < width(); a++){
                energies[i][a] = energy(a, i);
            }
        }

        for (int f = 1; f < width() - 1; f++){

            currentCoord = new ArrayList<Integer>();
            currentCoord.add(f);
            totalEnergy = 0;
            c = f;
            for (int z = 1; z < height(); z++){

                int cPlus1 = c + 1 >= height() ? c : c + 1;
                int cMinus1 = c - 1 < 0 ? 0 : c - 1;



                childEnergies = new ArrayList<Double>();
                childEnergies.add(energy(cPlus1, z));
                childEnergies.add(energy(c, z));
                childEnergies.add(energy(cMinus1, z));
                double minEnergy = 100000000;

                for (Double element : childEnergies){
                    if (element < minEnergy){
                        minEnergy = element;
                    }
                }


                totalEnergy += minEnergy;

                if (cPlus1 != c && minEnergy == energy(cPlus1, z)){
                    c = c + 1;
                }
                else if (cMinus1 != c && minEnergy == energy(cMinus1, z)) {
                    c = c - 1;
                }

                currentCoord.add(c);
            }
            seamEnergies.add(totalEnergy);
            seamsCoords.add(currentCoord);

        }

        double min  = Integer.MAX_VALUE;
        int mindex = 0;
        for(int l = 0; l < seamEnergies.size(); l++){
            if (seamEnergies.get(l) < min){
                min = seamEnergies.get(l);
                mindex = l;
            }
        }

        System.out.println("");
        for (int h = 0; h < seamsCoords.get(mindex).size(); h++){
            coordinates[h] = seamsCoords.get(mindex).get(h);
        }

        return coordinates;
    }

    public void removeHorizontalSeam(int[] seam) {
	    /*
        for (int i = 0; i < height(); i++){

            for (int a = seam[i]; a < width()-1; a++){
                picture.setRGB(colors[i][a+1].getRed(),colors[i][a+1].getGreen(),colors[i][a+1].getBlue());
            }
        }


        */



	    /*
        if (!(width > 1 || height > 1) || seam.length != width()){
            throw new IllegalArgumentException();
        }


        height--;
        Picture removed = new Picture(width, height);

        int counter = 0;
        int inner;
        while(counter < width()){
            inner = 0;
            while(inner < seam[counter]){

                removed.set(counter,inner,picture().get(counter,inner));

                inner++;
            }

            for (int f = seam[counter]; f < height(); f++){

                removed.set(counter,f,picture().get(counter, f + 1));

            }
            counter++;
        }

        picture = removed;

        if (!(width > 1 || height > 1) || seam.length != width()){
            throw new IllegalArgumentException();
        }
        */



        height = height - 1;
        Picture removed = new Picture(width, height);

        int counter = 0;
        int inner;
        while(counter < width()){
            inner = 0;
            while(inner < seam[counter]){

                removed.set(counter, inner, picture.get(counter,inner));
                inner++;
            }


            for (int f = seam[counter]; f < height; f++){

                removed.set(counter,f,picture().get(counter, f + 1));

            }

            counter++;
        }

        picture = removed;




        /*
        Picture newpic = new Picture(width, --height);

        for (int i = 0; i < width; i++) { // column
            int j;
            for (j = 0; j < seam[i]; j++) // row
                newpic.set(i, j, picture.get(i, j));

            for (j = seam[i]; j < height; j++)
                newpic.set(i, j, picture.get(i, j+1));
        }

        picture = newpic;
        */


    }

    public void removeVerticalSeam(int[] seam) {
	    /*
        if (!(width > 1 || height > 1) || seam.length != width()){
            throw new IllegalArgumentException();
        }





                Picture removed = new Picture(width() - 1, height());

        int counter = 0;
        int inner;
        while(counter < height()){
            inner = 0;
            while(inner < seam[counter]){

                removed.set(inner,counter,picture().get(inner,counter));

                inner++;
            }

            for (int f = seam[counter]; f < width(); f++){

                removed.set(f,counter,picture().get(f + 1, counter));

            }
            counter++;
        }

        picture = removed;


        if (!(width > 1 || height > 1) || seam.length != width()){
            throw new IllegalArgumentException();
        }
*/



        width = width - 1;
        Picture removed = new Picture(width, height);

        int counter = 0;
        int inner;
        while(counter < height){
            inner = 0;
            while(inner < seam[counter]){

                removed.set(inner, counter, picture.get(inner,counter));
                inner++;
            }


            for (int f = seam[counter]; f < width; f++){

                removed.set(f,counter,picture().get(f + 1, counter));

            }

            counter++;
        }

        picture = removed;






/*

        Picture newpic = new Picture(--width, height);

        for (int j = 0; j < height; j++) { // row
            int i;
            for (i = 0; i < seam[j]; i++) // column
                newpic.set(i, j, picture.get(i, j));

            for (i = seam[j]; i < width; i++)
                newpic.set(i, j, picture.get(i+1, j));
        }

        picture = newpic;
        */

    }

}
