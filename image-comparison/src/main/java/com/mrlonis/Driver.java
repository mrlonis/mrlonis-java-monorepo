package com.mrlonis;

import java.awt.Color;

/** @author Matthew Lonis */
public class Driver {

    private static int numCollisions;

    /**
     * Return the ColorTable associated with this image, assuming the color key space is restricted to bitsPerChannel.
     * Increment numCollisions after each increment.
     *
     * @param image The image that will be converted into a ColorTable.
     * @param bitsPerChannel The bits per channel that will be used in the ColorTable.
     * @return A ColorTable representation of image.
     */
    public static ColorTable vectorize(Image image, int bitsPerChannel) {
        int width = image.getWidth();
        int height = image.getHeight();
        // numCollisions = 0;

        ColorTable colorTable = new ColorTable(11, bitsPerChannel, Constants.QUADRATIC, 0.49);
        // ColorTable colorTable = new ColorTable(11, bitsPerChannel, Constants.LINEAR, 0.49);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                colorTable.increment(image.getColor(i, j));
                numCollisions += ColorTable.getNumCollisions();
            }
        }

        return colorTable;
    }

    /**
     * Return the result of running Util.cosineSimilarity() on the vectorized images.
     *
     * @param image1 The first image.
     * @param image2 The second image.
     * @param bitsPerChannel The bits per channel used when converting image1 and image2 into a ColorTable.
     * @return The cosine similarity of image1 and image2.
     */
    public static double similarity(Image image1, Image image2, int bitsPerChannel) {
        ColorTable image1CT = vectorize(image1, bitsPerChannel);
        ColorTable image2CT = vectorize(image2, bitsPerChannel);
        return Util.cosineSimilarity(image1CT, image2CT);
    }

    /**
     * Uses the Painting images and all 8 bitsPerChannel values to compute and print out a table of collision counts.
     */
    public static void allPairsTest() {
        Painting[] paintings = Painting.values();
        int n = paintings.length;
        for (int y = 0; y < n; y++) {
            for (int x = y + 1; x < n; x++) {
                System.out.println(paintings[y].get().getName() + " and "
                        + paintings[x].get().getName() + ":");
                for (int bitsPerChannel = 1; bitsPerChannel <= 8; bitsPerChannel++) {
                    numCollisions = 0;
                    System.out.printf(
                            "   %d: %.2f %d%n",
                            bitsPerChannel,
                            similarity(paintings[x].get(), paintings[y].get(), bitsPerChannel),
                            numCollisions);
                }
                System.out.println();
            }
        }
    }

    /** Simple testing */
    public static void main(String[] args) {
        System.out.println(Constants.TITLE + "\n");
        System.out.println("Report - Number 3 - Testing...");
        Image mona = Painting.MONA_LISA.get();
        ColorTable monaCT = vectorize(mona, 2);
        long numBlackMonaCT = monaCT.get(Color.BLACK);
        System.out.println(
                "Number of Black pixels in davinci Mona Lisa image at 2 bits per channel: " + numBlackMonaCT);
        Image starry = Painting.STARRY_NIGHT.get();
        ColorTable starryCT = vectorize(starry, 2);
        long numBlackStarryCT = starryCT.get(Color.BLACK);
        System.out.println("Number of Black pixels in vangoah Starry Night image at 2 bits per channel: "
                + numBlackStarryCT + "\n");
        Image christina = Painting.CHRISTINAS_WORLD.get();
        System.out.println("It looks like all three test images were successfully loaded!");
        System.out.println("mona's dimensions are " + mona.getWidth() + " x " + mona.getHeight());
        System.out.println("starry's dimenstions are " + starry.getWidth() + " x " + starry.getHeight());
        System.out.println("christina's dimensions are " + christina.getWidth() + " x " + christina.getHeight());
        System.out.println();
        System.out.println("All Pairs Testing...");
        allPairsTest();
        System.out.println("...Done!");
    }
}
