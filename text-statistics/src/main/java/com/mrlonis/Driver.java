package com.mrlonis;

/**
 * Running this driver will print some statistics about a small sample text.
 *
 * <p>Once your code passes all the unit tests, replace text with the contents of Alice In Wonderland. Figure out how to
 * calculate the percentage of bits saved. Do the same for Moby Dick. Then, download the plain text version of a
 * favorite book from http://www.gutenberg.org to your project directory, and collect those statistics.
 *
 * <p>Create a text file named report.txt in your project directory. This file should include your full name, username,
 * and lab section, and a table of your statistics regarding the three books.
 *
 * @author Matthew Lonis (mrlonis)
 */
public class Driver {
    public static void main(String[] args) {
        System.out.println(Constants.TITLE);
        /*
        String text = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + "bbbbbbbbbbbbbb" + "cccccccccccc"
        		+ "ddddddddddddddddddddd" + "eeeeeeeee" + "fffff";
        */
        // String text = Util.loadFile(Constants.ALICE);
        String text = Util.loadFile(Constants.MOBY_DICK);
        // String text = Util.loadFile(Constants.TOM_SAWYER);

        System.out.println("\nThe original text has " + text.length() + " characters.");
        CodeBook book = new CodeBook(text);
        System.out.println(String.format("The average length of a code is %.2f bits.", book.getWeightedAverage()));
        Zipper zipper = new Zipper(book);
        String bits = zipper.encode(text);
        System.out.println("The text is encoded in " + bits.length() + " bits.");
        int originalBitAmount = text.length() * Constants.BITESIZE;

        System.out.println("\nPercentage of Bits Saved Stats...");
        System.out.println("The original number of bits is " + originalBitAmount + " bits.");
        long compressedBitAmount = bits.length();
        System.out.println("The compressed number of bits is " + compressedBitAmount + " bits.");
        long amountOfBitsSaved = (text.length() * Constants.BITESIZE - bits.length());
        System.out.println(
                "The number of bits saved is " + (text.length() * Constants.BITESIZE - bits.length()) + " bits.");
        double percentBitsSaved = (((double) amountOfBitsSaved / (double) originalBitAmount) * 100);
        System.out.println("The percent of bits saved is " + percentBitsSaved + " bits.");

        System.out.println("\nPercentage of Chars Saved Stats...");
        String packing = zipper.compress(bits);
        long compressedChar = packing.length();
        System.out.println("The compressed text is encoded in " + compressedChar + " characters.");
        long compressedCharSavings = (text.length() - packing.length());
        System.out.println("The savings is " + compressedCharSavings + " characters.");
        String unpacking = zipper.decompress(packing);
        double compressedCharPercentSaved = (((double) compressedCharSavings / (double) compressedChar) * 100);
        System.out.println("The percent of characters saved is " + compressedCharPercentSaved + " characters.");

        System.out.println("\nDecompressing yields " + unpacking.length() + " bits.");
        String recoveredText = zipper.decode(unpacking);
        System.out.println("The recovered text has " + recoveredText.length() + " characters.");
        assert recoveredText.equals(text);
        System.out.println("\nAll tests passed...");
    }
}
