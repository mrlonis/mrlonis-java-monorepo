package com.mrlonis;

import java.util.ArrayList;
import java.util.List;

/**
 * MatchBot class that searches through a user's Tweets and finds Tweets containing patterns. Employs the Naive, KMP, and Boyer-Moore algorithms from StringMatch.java and extends
 * TwitterBot.java.
 *
 * @author Matthew Lonis (mrlonis)
 */
public class MatchBot extends TwitterBot {

    /**
     * Constructs a MatchBot to operate on the last numTweets of the given user.
     */
    public MatchBot(String user,
                    int numTweets) {
        super(user, numTweets);
    }

    public static void main(String... args) {
        /*
         * Set the user to be creeped on.
         */
        String handle = "realDonaldTrump", pattern = "China";
        System.out.println("Creeping on @" + handle + " and looking for the pattern " + pattern + "!");
        MatchBot bot = new MatchBot(handle, 500);

        /*
         * Search all tweets for the pattern.
         */
        List<String> ansNaive = new ArrayList<>();
        int compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
        List<String> ansKMP = new ArrayList<>();
        int compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
        List<String> ansBoyerMoore = new ArrayList<>();
        int compsBoyerMoore = bot.searchTweetsBoyerMoore(pattern, ansBoyerMoore);

        /*
         * Print out total comparisons for each algorithm.
         */
        System.out.println("naive comps = " + compsNaive + ", KMP comps = " + compsKMP + ", Moyer-Moore comps = " + compsBoyerMoore);

        /*
         * Test for consistency between Naive, KMP & Boyer-Moore
         */
        assert ansNaive.size() == ansKMP.size();
        assert ansBoyerMoore.size() == ansKMP.size();

        for (int i = 0; i < ansKMP.size(); i++) {
            String tweet = ansKMP.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
            System.out.println(i++ + ". " + tweet);
            System.out.println(pattern + " appears at index " + tweet.toLowerCase()
                                                                     .indexOf(pattern.toLowerCase()));
        }

        for (int i = 0; i < ansNaive.size(); i++) {
            String tweet = ansNaive.get(i);
            assert tweet.equals(ansKMP.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
        }

        for (int i = 0; i < ansBoyerMoore.size(); i++) {
            String tweet = ansBoyerMoore.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansKMP.get(i));
        }

        /**
         * Do something similar for the Boyer-Moore matching algorithm.
         */

        /*
         * Divider for the console window.
         */
        System.out.println("\n<-------------------- New User -------------------->");

        /*
         * Set the user to be creeped on.
         */
        handle = "THEUALIFESTYLE";
        pattern = "Renaissance";
        System.out.println("\nCreeping on @" + handle + " and looking for the pattern " + pattern + "!");
        bot = new MatchBot(handle, 250);

        /*
         * Search all tweets for the pattern.
         */
        ansNaive = new ArrayList<>();
        compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
        ansKMP = new ArrayList<>();
        compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
        ansBoyerMoore = new ArrayList<>();
        compsBoyerMoore = bot.searchTweetsBoyerMoore(pattern, ansBoyerMoore);

        /*
         * Print out total comparisons for each algorithm.
         */
        System.out.println("naive comps = " + compsNaive + ", KMP comps = " + compsKMP + ", Moyer-Moore comps = " + compsBoyerMoore);

        /*
         * Test for consistency between Naive, KMP & Boyer-Moore
         */
        assert ansNaive.size() == ansKMP.size();
        assert ansBoyerMoore.size() == ansKMP.size();

        for (int i = 0; i < ansKMP.size(); i++) {
            String tweet = ansKMP.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
            System.out.println(i++ + ". " + tweet);
            System.out.println(pattern + " appears at index " + tweet.toLowerCase()
                                                                     .indexOf(pattern.toLowerCase()));
        }

        for (int i = 0; i < ansNaive.size(); i++) {
            String tweet = ansNaive.get(i);
            assert tweet.equals(ansKMP.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
        }

        for (int i = 0; i < ansBoyerMoore.size(); i++) {
            String tweet = ansBoyerMoore.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansKMP.get(i));
        }

        /*
         * Divider for the console window.
         */
        System.out.println("\n<-------------------- New User -------------------->");

        /*
         * Set the user to be creeped on.
         */
        handle = "kenzi_day"; // This is my girl friend
        pattern = "Matthew";
        System.out.println("\nCreeping on @" + handle + " and looking for the pattern " + pattern + "!");
        bot = new MatchBot(handle, 250);

        /*
         * Search all tweets for the pattern.
         */
        ansNaive = new ArrayList<>();
        compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
        ansKMP = new ArrayList<>();
        compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
        ansBoyerMoore = new ArrayList<>();
        compsBoyerMoore = bot.searchTweetsBoyerMoore(pattern, ansBoyerMoore);

        /*
         * Print out total comparisons for each algorithm.
         */
        System.out.println("naive comps = " + compsNaive + ", KMP comps = " + compsKMP + ", Moyer-Moore comps = " + compsBoyerMoore);

        /*
         * Test for consistency between Naive, KMP & Boyer-Moore
         */
        assert ansNaive.size() == ansKMP.size();
        assert ansBoyerMoore.size() == ansKMP.size();

        for (int i = 0; i < ansKMP.size(); i++) {
            String tweet = ansKMP.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
            System.out.println(i++ + ". " + tweet);
            System.out.println(pattern + " appears at index " + tweet.toLowerCase()
                                                                     .indexOf(pattern.toLowerCase()));
        }

        for (int i = 0; i < ansNaive.size(); i++) {
            String tweet = ansNaive.get(i);
            assert tweet.equals(ansKMP.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
        }

        for (int i = 0; i < ansBoyerMoore.size(); i++) {
            String tweet = ansBoyerMoore.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansKMP.get(i));
        }

        /*
         * Divider for the console window.
         */
        pattern = "emotional";
        System.out.println("\n<-------------------- New Pattern: " + pattern + " -------------------->");

        /*
         * Search all tweets for the pattern.
         */
        ansNaive = new ArrayList<>();
        compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
        ansKMP = new ArrayList<>();
        compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
        ansBoyerMoore = new ArrayList<>();
        compsBoyerMoore = bot.searchTweetsBoyerMoore(pattern, ansBoyerMoore);

        /*
         * Print out total comparisons for each algorithm.
         */
        System.out.println("naive comps = " + compsNaive + ", KMP comps = " + compsKMP + ", Moyer-Moore comps = " + compsBoyerMoore);

        /*
         * Test for consistency between Naive, KMP & Boyer-Moore
         */
        assert ansNaive.size() == ansKMP.size();
        assert ansBoyerMoore.size() == ansKMP.size();

        for (int i = 0; i < ansKMP.size(); i++) {
            String tweet = ansKMP.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
            System.out.println(i++ + ". " + tweet);
            System.out.println(pattern + " appears at index " + tweet.toLowerCase()
                                                                     .indexOf(pattern.toLowerCase()));
        }

        for (int i = 0; i < ansNaive.size(); i++) {
            String tweet = ansNaive.get(i);
            assert tweet.equals(ansKMP.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
        }

        for (int i = 0; i < ansBoyerMoore.size(); i++) {
            String tweet = ansBoyerMoore.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansKMP.get(i));
        }

        /*
         * Divider for the console window.
         */
        pattern = "sexual assault";
        System.out.println("\n<-------------------- New Pattern: " + pattern + " -------------------->");

        /*
         * Search all tweets for the pattern.
         */
        ansNaive = new ArrayList<>();
        compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
        ansKMP = new ArrayList<>();
        compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
        ansBoyerMoore = new ArrayList<>();
        compsBoyerMoore = bot.searchTweetsBoyerMoore(pattern, ansBoyerMoore);

        /*
         * Print out total comparisons for each algorithm.
         */
        System.out.println("naive comps = " + compsNaive + ", KMP comps = " + compsKMP + ", Moyer-Moore comps = " + compsBoyerMoore);

        /*
         * Test for consistency between Naive, KMP & Boyer-Moore
         */
        assert ansNaive.size() == ansKMP.size();
        assert ansBoyerMoore.size() == ansKMP.size();

        for (int i = 0; i < ansKMP.size(); i++) {
            String tweet = ansKMP.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
            System.out.println(i++ + ". " + tweet);
            System.out.println(pattern + " appears at index " + tweet.toLowerCase()
                                                                     .indexOf(pattern.toLowerCase()));
        }

        for (int i = 0; i < ansNaive.size(); i++) {
            String tweet = ansNaive.get(i);
            assert tweet.equals(ansKMP.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
        }

        for (int i = 0; i < ansBoyerMoore.size(); i++) {
            String tweet = ansBoyerMoore.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansKMP.get(i));
        }

        /*
         * Divider for the console window.
         */
        pattern = "Trump";
        System.out.println("\n<-------------------- New Pattern: " + pattern + " -------------------->");

        /*
         * Search all tweets for the pattern.
         */
        ansNaive = new ArrayList<>();
        compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
        ansKMP = new ArrayList<>();
        compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
        ansBoyerMoore = new ArrayList<>();
        compsBoyerMoore = bot.searchTweetsBoyerMoore(pattern, ansBoyerMoore);

        /*
         * Print out total comparisons for each algorithm.
         */
        System.out.println("naive comps = " + compsNaive + ", KMP comps = " + compsKMP + ", Moyer-Moore comps = " + compsBoyerMoore);

        /*
         * Test for consistency between Naive, KMP & Boyer-Moore
         */
        assert ansNaive.size() == ansKMP.size();
        assert ansBoyerMoore.size() == ansKMP.size();

        for (int i = 0; i < ansKMP.size(); i++) {
            String tweet = ansKMP.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
            System.out.println(i++ + ". " + tweet);
            System.out.println(pattern + " appears at index " + tweet.toLowerCase()
                                                                     .indexOf(pattern.toLowerCase()));
        }

        for (int i = 0; i < ansNaive.size(); i++) {
            String tweet = ansNaive.get(i);
            assert tweet.equals(ansKMP.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
        }

        for (int i = 0; i < ansBoyerMoore.size(); i++) {
            String tweet = ansBoyerMoore.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansKMP.get(i));
        }

        /*
         * Divider for the console window.
         */
        System.out.println("\n<-------------------- New User -------------------->");

        /*
         * Set the user to be creeped on.
         */
        handle = "PlayOverwatch";
        pattern = "Overwatch";
        System.out.println("\nCreeping on @" + handle + " and looking for the pattern " + pattern + "!");
        bot = new MatchBot(handle, 250);

        /*
         * Search all tweets for the pattern.
         */
        ansNaive = new ArrayList<>();
        compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
        ansKMP = new ArrayList<>();
        compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
        ansBoyerMoore = new ArrayList<>();
        compsBoyerMoore = bot.searchTweetsBoyerMoore(pattern, ansBoyerMoore);

        /*
         * Print out total comparisons for each algorithm.
         */
        System.out.println("naive comps = " + compsNaive + ", KMP comps = " + compsKMP + ", Moyer-Moore comps = " + compsBoyerMoore);

        /*
         * Test for consistency between Naive, KMP & Boyer-Moore
         */
        assert ansNaive.size() == ansKMP.size();
        assert ansBoyerMoore.size() == ansKMP.size();

        for (int i = 0; i < ansKMP.size(); i++) {
            String tweet = ansKMP.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
            System.out.println(i++ + ". " + tweet);
            System.out.println(pattern + " appears at index " + tweet.toLowerCase()
                                                                     .indexOf(pattern.toLowerCase()));
        }

        for (int i = 0; i < ansNaive.size(); i++) {
            String tweet = ansNaive.get(i);
            assert tweet.equals(ansKMP.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
        }

        for (int i = 0; i < ansBoyerMoore.size(); i++) {
            String tweet = ansBoyerMoore.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansKMP.get(i));
        }

        /*
         * Divider for the console window.
         */
        System.out.println("\n<-------------------- New User -------------------->");

        /*
         * Set the user to be creeped on.
         */
        handle = "taylorswift13";
        pattern = "love";
        System.out.println("\nCreeping on @" + handle + " and looking for the pattern " + pattern + "!");
        bot = new MatchBot(handle, 250);

        /*
         * Search all tweets for the pattern.
         */
        ansNaive = new ArrayList<>();
        compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
        ansKMP = new ArrayList<>();
        compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
        ansBoyerMoore = new ArrayList<>();
        compsBoyerMoore = bot.searchTweetsBoyerMoore(pattern, ansBoyerMoore);

        /*
         * Print out total comparisons for each algorithm.
         */
        System.out.println("naive comps = " + compsNaive + ", KMP comps = " + compsKMP + ", Moyer-Moore comps = " + compsBoyerMoore);

        /*
         * Test for consistency between Naive, KMP & Boyer-Moore
         */
        assert ansNaive.size() == ansKMP.size();
        assert ansBoyerMoore.size() == ansKMP.size();

        for (int i = 0; i < ansKMP.size(); i++) {
            String tweet = ansKMP.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
            System.out.println(i++ + ". " + tweet);
            System.out.println(pattern + " appears at index " + tweet.toLowerCase()
                                                                     .indexOf(pattern.toLowerCase()));
        }

        for (int i = 0; i < ansNaive.size(); i++) {
            String tweet = ansNaive.get(i);
            assert tweet.equals(ansKMP.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
        }

        for (int i = 0; i < ansBoyerMoore.size(); i++) {
            String tweet = ansBoyerMoore.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansKMP.get(i));
        }

        /*
         * Divider for the console window.
         */
        System.out.println("\n<-------------------- New User -------------------->");

        /*
         * Set the user to be creeped on.
         */
        handle = "billnye";
        pattern = "science";
        System.out.println("\nCreeping on @" + handle + " and looking for the pattern " + pattern + "!");
        bot = new MatchBot(handle, 250);

        /*
         * Search all tweets for the pattern.
         */
        ansNaive = new ArrayList<>();
        compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
        ansKMP = new ArrayList<>();
        compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
        ansBoyerMoore = new ArrayList<>();
        compsBoyerMoore = bot.searchTweetsBoyerMoore(pattern, ansBoyerMoore);

        /*
         * Print out total comparisons for each algorithm.
         */
        System.out.println("naive comps = " + compsNaive + ", KMP comps = " + compsKMP + ", Moyer-Moore comps = " + compsBoyerMoore);

        /*
         * Test for consistency between Naive, KMP & Boyer-Moore
         */
        assert ansNaive.size() == ansKMP.size();
        assert ansBoyerMoore.size() == ansKMP.size();

        for (int i = 0; i < ansKMP.size(); i++) {
            String tweet = ansKMP.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
            System.out.println(i++ + ". " + tweet);
            System.out.println(pattern + " appears at index " + tweet.toLowerCase()
                                                                     .indexOf(pattern.toLowerCase()));
        }

        for (int i = 0; i < ansNaive.size(); i++) {
            String tweet = ansNaive.get(i);
            assert tweet.equals(ansKMP.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
        }

        for (int i = 0; i < ansBoyerMoore.size(); i++) {
            String tweet = ansBoyerMoore.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansKMP.get(i));
        }

        /*
         * Divider for the console window.
         */
        System.out.println("\n<-------------------- New User -------------------->");

        /*
         * Set the user to be creeped on.
         */
        handle = "nasa";
        pattern = "space";
        System.out.println("\nCreeping on @" + handle + " and looking for the pattern " + pattern + "!");
        bot = new MatchBot(handle, 250);

        /*
         * Search all tweets for the pattern.
         */
        ansNaive = new ArrayList<>();
        compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
        ansKMP = new ArrayList<>();
        compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
        ansBoyerMoore = new ArrayList<>();
        compsBoyerMoore = bot.searchTweetsBoyerMoore(pattern, ansBoyerMoore);

        /*
         * Print out total comparisons for each algorithm.
         */
        System.out.println("naive comps = " + compsNaive + ", KMP comps = " + compsKMP + ", Moyer-Moore comps = " + compsBoyerMoore);

        /*
         * Test for consistency between Naive, KMP & Boyer-Moore
         */
        assert ansNaive.size() == ansKMP.size();
        assert ansBoyerMoore.size() == ansKMP.size();

        for (int i = 0; i < ansKMP.size(); i++) {
            String tweet = ansKMP.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
            System.out.println(i++ + ". " + tweet);
            System.out.println(pattern + " appears at index " + tweet.toLowerCase()
                                                                     .indexOf(pattern.toLowerCase()));
        }

        for (int i = 0; i < ansNaive.size(); i++) {
            String tweet = ansNaive.get(i);
            assert tweet.equals(ansKMP.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
        }

        for (int i = 0; i < ansBoyerMoore.size(); i++) {
            String tweet = ansBoyerMoore.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansKMP.get(i));
        }

        /*
         * Divider for the console window.
         */
        System.out.println("\n<-------------------- New User -------------------->");

        /*
         * Set the user to be creeped on.
         */
        handle = "mike_pence";
        pattern = "great";
        System.out.println("\nCreeping on @" + handle + " and looking for the pattern " + pattern + "!");
        bot = new MatchBot(handle, 250);

        /*
         * Search all tweets for the pattern.
         */
        ansNaive = new ArrayList<>();
        compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
        ansKMP = new ArrayList<>();
        compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
        ansBoyerMoore = new ArrayList<>();
        compsBoyerMoore = bot.searchTweetsBoyerMoore(pattern, ansBoyerMoore);

        /*
         * Print out total comparisons for each algorithm.
         */
        System.out.println("naive comps = " + compsNaive + ", KMP comps = " + compsKMP + ", Boyer-Moore comps = " + compsBoyerMoore);

        /*
         * Test for consistency between Naive, KMP & Boyer-Moore
         */
        assert ansNaive.size() == ansKMP.size();
        assert ansBoyerMoore.size() == ansKMP.size();

        for (int i = 0; i < ansKMP.size(); i++) {
            String tweet = ansKMP.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
            System.out.println(i++ + ". " + tweet);
            System.out.println(pattern + " appears at index " + tweet.toLowerCase()
                                                                     .indexOf(pattern.toLowerCase()));
        }

        for (int i = 0; i < ansNaive.size(); i++) {
            String tweet = ansNaive.get(i);
            assert tweet.equals(ansKMP.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
        }

        for (int i = 0; i < ansBoyerMoore.size(); i++) {
            String tweet = ansBoyerMoore.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansKMP.get(i));
        }
    }

    /**
     * Employs the naive string matching algorithm to find all Tweets containing the given pattern to the provided list. Returns the total number of character comparisons
     * performed.
     *
     * @param pattern The pattern to search for in the user's Tweets.
     * @param ans     The list to add Tweets containing the given patter.
     * @return The total number of character comparisons performed.
     */
    public int searchTweetsNaive(String pattern,
                                 List<String> ans) {
        int count = 0, n = this.numTweets;

        for (int i = 0; i < n - 1; i++) {
            String tweet = this.tweets.get(i);
            Result temp = StringMatch.matchNaive(pattern, tweet);

            if (temp.pos != -1) {
                ans.add(tweet);
            }

            count += temp.comps;
        }

        return count;
    }

    /**
     * Employs the KMP string matching algorithm to add all Tweets containing the given pattern to the provided list. Returns the total number of character comparisons performed.
     *
     * @param pattern The pattern to search for in the user's Tweets.
     * @param ans     The list to add Tweets containing the given patter.
     * @return The total number of character comparisons performed.
     */
    public int searchTweetsKMP(String pattern,
                               List<String> ans) {
        int count = 0, n = this.numTweets;

        for (int i = 0; i < n - 1; i++) {
            String tweet = this.tweets.get(i);
            Result temp = StringMatch.matchKMP(pattern, tweet);

            if (temp.pos != -1) {
                ans.add(tweet);
            }

            count += temp.comps;
        }

        return count;
    }

    /**
     * Employs the Boyer-Moore string matching algorithm to find all tweets containing the given pattern to the provided list. Returns the total number of character comparisons
     * performed.
     *
     * @param pattern The pattern to search for in the user's tweets.
     * @param ans     The list to add tweets containing the given patter.
     * @return The total number of character comparisons performed.
     */
    public int searchTweetsBoyerMoore(String pattern,
                                      List<String> ans) {
        int count = 0, n = this.numTweets;

        for (int i = 0; i < n - 1; i++) {
            String tweet = this.tweets.get(i);
            Result temp = StringMatch.matchBoyerMoore(pattern, tweet);

            if (temp.pos != -1) {
                ans.add(tweet);
            }

            count += temp.comps;
        }

        return count;
    }
}
