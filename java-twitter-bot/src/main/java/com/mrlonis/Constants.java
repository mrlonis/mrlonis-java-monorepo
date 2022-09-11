package com.mrlonis;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public interface Constants {

    String HASHTAG = "#C343Rocks";
    int PAGE_SIZE = 20;
    String PUNCTUATION = ".,'?!:;\"(){}^{}<>-";
    String BORING_WORDS = "boringWords.txt";
    // Assume alphabet consists of the basic ASCII code.
	int SIGMA_SIZE = 128;
    // The factory instance is re-useable and thread safe.
	Twitter TWITTER = TwitterFactory.getSingleton();
}
