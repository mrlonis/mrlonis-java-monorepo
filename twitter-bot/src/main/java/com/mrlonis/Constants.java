package com.mrlonis;

import twitter4j.Twitter;

public interface Constants {

    String HASHTAG = "#C343Rocks";
    int PAGE_SIZE = 20;
    String PUNCTUATION = ".,'?!:;\"(){}^{}<>-";
    String BORING_WORDS = "boringWords.txt";
    // Assume alphabet consists of the basic ASCII code.
    int SIGMA_SIZE = 128;
    // The factory instance is re-useable and thread safe.
    Twitter TWITTER = Twitter.getInstance();
}
