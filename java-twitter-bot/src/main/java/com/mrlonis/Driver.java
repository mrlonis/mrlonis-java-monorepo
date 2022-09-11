package com.mrlonis;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;

@SuppressWarnings("unused")

/**
 * Be sure to add twitter4j-core-4.0.4.jar to your classpath.
 */ public class Driver {

    public static void main(String... args) {
        /*
         * Divider for the console window.
         */
        System.out.println("\n<-------------------- Tweeting... -------------------->\n");

        sendTweet("\"Hello, World!\" from my Twitter bot...", false);

        /*
         * Divider for the console window.
         */
        System.out.println("\n<-------------------- tweetsAbout -------------------->\n");

        /*
         * What are people in my town saying about...?
         */
        tweetsAbout(Constants.HASHTAG, false);

        /*
         * Divider for the console window.
         */
        System.out.println("\n<-------------------- New tweetsAbout -------------------->\n");

        tweetsAbout("Trump", false);

        /*
         * Divider for the console window.
         */
        System.out.println("\n<-------------------- New Process -------------------->\n");

        /*
         * Find and print the most common word that each person has recently
         * tweeted
         */
        for (String friend : new String[]{"taylorswift13", "billnye", "nasa", "realdonaldtrump", "mike_pence", "kenzi_day", "PlayOverwatch", "THEUALIFESTYLE"}) {
            favoriteWord(friend, true);
        }
    }

    /**
     * Tweet out the given message (unless !forReal).
     */
    public static void sendTweet(String message,
                                 boolean forReal) {
        message += " " + Constants.HASHTAG;
		if (!forReal) {
			System.out.println(String.format("Tweet = %s", message));
		} else {
			try {
				Constants.TWITTER.updateStatus(message);
			} catch (TwitterException e) {
				System.out.println("Unable to tweet out at this time.");
				System.out.println(e.getMessage());
			}
		}
    }

    /**
     * This method shows what people have tweeted about the subject this month.
     */
    public static void tweetsAbout(String subject,
                                   boolean forReal) {
		if (!forReal) {
			System.out.println(String.format("tweetsAbout(\"%s\", %b);", subject, forReal));
		} else {
			Query query = new Query(subject);
			/*
			 * This will limit the number of responses to 100.
			 */
			query.setCount(100);
			/*
			 * Could limit the responses to a geographical location, if we
			 * wanted. query.setGeoCode(new GeoLocation(39.22031, -86.45824),
			 * 50, Query.MILES);
			 */
			query.setSince("2017-4-1");
			try {
				QueryResult result = Constants.TWITTER.search(query);
				System.out.println("Number tweets about " + subject + ": " + result.getTweets()
																				   .size());
				for (Status tweet : result.getTweets()) {
					System.out.println("@" + tweet.getUser()
												  .getScreenName() + ": " + tweet.getText());
				}
			} catch (TwitterException e) {
				System.out.println("Problem retrieving tweets about " + subject);
				e.printStackTrace();
			}
		}
    }

    public static void favoriteWord(String handle,
                                    boolean forReal) {
		if (!forReal) {
			System.out.println(String.format("favoriteWord(\"%s\", %b);", handle, forReal));
		} else {
			PopularityBot bot = new PopularityBot(handle, 2000);
			System.out.println(String.format("The most common word in the last %d tweets from @%s is: %s\n" + "It appears %d times.\n", bot.getNumTweets(), handle, bot.getMostPopularWord(),
											 bot.getFrequencyOfMostPopularWord()));
		}
    }
}
