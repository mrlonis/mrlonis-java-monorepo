package com.mrlonis;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * This class is provided as a base model for interacting with the Twitter API.
 */
public class TwitterBot {

    protected String user;
    protected List<String> tweets = new ArrayList<>();
    protected int numTweets;

    /**
     * Fetches the requested number of tweets from the given user.
     */
    public TwitterBot(String user,
                      int numTweets) {
        this.user = user;
        this.numTweets = numTweets;
		if (numTweets > 0) {
			fetch();
		}
    }

    /**
     * Fetches the most recent tweets from the user and stores them in tweets. For debugging purposes, the retrieved tweet data is also written to a file.
     */
    private void fetch() {
        try {
            PrintStream out = new PrintStream(new FileOutputStream(user + "_tweets.txt"));
            Paging page = new Paging(1, Constants.PAGE_SIZE);
            for (int p = 1; p <= Math.ceil((double) numTweets / Constants.PAGE_SIZE); p++) {
                page.setPage(p);
				for (Status status : Constants.TWITTER.getUserTimeline(user, page)) {
					tweets.add(status.getText());
				}
            }
            int numberTweets = tweets.size();
            out.println("Number of tweets = " + numberTweets);
            int i = 1;
			for (String tweet : tweets) {
				out.println(i++ + ".  " + tweet);
			}
            out.close();
        } catch (IOException e) {
            System.out.println("Problem creating tweet file for " + user);
        } catch (TwitterException e) {
            System.out.println("Unable to retrieve tweets from Twitter for " + user + " at this time.");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns the user associated with this bot.
     */
    public String getUser() {
        return user;
    }

    /**
     * Returns the number of tweets retrieved from Twitter for the user by this bot.
     */
    public int getNumTweets() {
        return tweets.size();
    }
}
