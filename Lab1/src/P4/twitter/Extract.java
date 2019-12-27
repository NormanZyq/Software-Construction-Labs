/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P4.twitter;

import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        /* NOT SURE IF IT SHOULD BE LIKE THIS
         * I CANNOT UNDERSTAND THIS TASK */

        Iterator<Tweet> tweetIterator = tweets.iterator();
        Tweet firstTweet = tweetIterator.next();
        Instant earliest = firstTweet.getTimestamp();
        Instant latest = firstTweet.getTimestamp();
        tweetIterator.next();
        while (tweetIterator.hasNext()) {
            Tweet currentTweet = tweetIterator.next();
            if (currentTweet.getTimestamp().isBefore(earliest)) {
                earliest = currentTweet.getTimestamp();
            }
            if (currentTweet.getTimestamp().isAfter(latest)) {
                latest = currentTweet.getTimestamp();
            }
        }

        return new Timespan(earliest, latest);
    }

    /**
     * Get usernames mentioned in a list of tweets.
     *
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {

        // make a regex to extract "@" character
//        final String reg = "[\\s]+@[\\w-/]+";                     // strict reg
//        final String reg = "[\\s]+@[\\w-]+";                      // lenient reg
        final String reg = "\\s*[^\\w-]@[\\w-]+";                   // lenient reg2
//        final String reg = "\\s+@[\\w-]+";
        final Pattern pattern = Pattern.compile(reg);

        // new mentioned set object
        Set<String> mentionedUsers = new HashSet<>();

        // go through the tweet list, and get each text to match
        for (Tweet tweet : tweets) {
            StringBuilder textSb = new StringBuilder(" ").append(tweet.getText()).append(" ");
//            String text = tweet.getText();
            Matcher matcher = pattern.matcher(textSb);
            while (matcher.find()) {
                String mentionedUsername = matcher.group().trim().replaceAll("@", "");
//                if (mentionedUsername.contains("//")) {       // only works with strict reg
//                    continue;
//                }
//                mentionedUsername = mentionedUsername.replace("/", "");
                // every time matcher finds a match then add it to the set
                String usernameToCheck = mentionedUsername.toLowerCase();
                if (!mentionedUsers.contains(usernameToCheck)) {
                    mentionedUsers.add(mentionedUsername);
                }
//                mentionedUsers.add(matcher.group().trim().replaceFirst("@", ""));
            }
        }

        return mentionedUsers;
    }

}
