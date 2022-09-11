package com.mrlonis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * This class is provided as an example of how you might process the data fetched from Twitter. Notice that we extend TwitterBot to set up the connection.
 */
public class PopularityBot extends TwitterBot {

    // Read list of boring words from a file. Just need to do this once.
    private static final List<String> boringWords = new ArrayList<>();

    static {
        try {
            Scanner in = new Scanner(new File(Constants.BORING_WORDS));
			while (in.hasNext()) {
				boringWords.add(in.next());
			}
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not open " + Constants.BORING_WORDS);
        }
    }

    private final List<String> words = new ArrayList<>();
    private int popFrequency;
    private String popWord;

    /**
     * Fetches the requested number of tweets from the given user.
     */
    public PopularityBot(String user,
                         int numTweets) {
        super(user, numTweets);
        split();
        findMostPopularWord();
    }

    /**
     * Takes the text of each status and splits it into individual lower-case words, trims the punctuation, and ignores any uninteresting words.
     */
    private void split() {
        for (String tweet : tweets) {
			for (String word : scrub(tweet).toLowerCase()
										   .split(" ")) {
				if (word.length() > 1 && word.charAt(0) != '@' && !boringWords.contains(word)) {
					words.add(word);
				}
			}
        }
        Collections.sort(words);
    }

    /**
     * Finds the most popular word in the user's tweets.
     */
    private void findMostPopularWord() {
        popWord = "";
        int currRun = 0, maxRun = 0;
        for (int i = 0; i < words.size() - 1; i++) {
			if (words.get(i)
					 .equals(words.get(i + 1))) {
				currRun++;
			} else {
				currRun = 1;
			}
            if (currRun > maxRun) {
                maxRun = currRun;
                popWord = words.get(i);
            }
        }
        popFrequency = maxRun;
    }

    /**
     * Returns the result of removing common punctuation from the given word. Note that we do not remove # or @ from the word.
     */
    private String scrub(String word) {
        String ans = "";
		for (int i = 0; i < word.length(); i++) {
			if (Constants.PUNCTUATION.indexOf(word.charAt(i)) == -1) {
				ans += word.charAt(i);
			}
		}
        return ans;
    }

    /**
     * Returns the most popular word in the user's recent tweet stream.
     */
    public String getMostPopularWord() {
        return popWord;
    }

    /**
     * Returns the number of times the most popular word appears in the user's recent tweet stream.
     */
    public int getFrequencyOfMostPopularWord() {
        return popFrequency;
    }
}
