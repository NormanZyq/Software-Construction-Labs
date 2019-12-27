/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P4.twitter;

import java.util.*;

/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Filter {

    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        List<Tweet> filteredTweets = new ArrayList<>(); // new tweet list

        // go through all tweets coming in
        for (Tweet tweet : tweets) {
            if (username.equals(tweet.getAuthor())) {
                filteredTweets.add(tweet);
            }
        }

        return filteredTweets;
    }

    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        List<Tweet> filteredTweets = new ArrayList<>();

        for (Tweet tweet : tweets) {
            if (tweet.getTimestamp().isAfter(timespan.getStart()) && tweet.getTimestamp().isBefore(timespan.getEnd())) {
                filteredTweets.add(tweet);
            }
        }

        return filteredTweets;
    }

    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when 
     *         represented as a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes *at least one* of the words 
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<Tweet> filteredTweets = new ArrayList<>();

        for (Tweet tweet : tweets) {
            for (String word : words) {
                String disposedText = tweet.getText().toLowerCase().replaceAll("[^\\w@-]", " ");
//                System.out.println(disposedText);
                List<String> stringAsList = Arrays.asList(disposedText.split("\\s+"));

                if (stringAsList.contains(word.toLowerCase().trim()) && !filteredTweets.contains(tweet)) {
                    filteredTweets.add(tweet);
                    break;
                }

//                String disposedText = textBuilder.toString();
//                if (disposedText.contains(word.toLowerCase().trim())) {
//                    filteredTweets.add(tweet);
//                    break;
//                }
            }
        }

        return filteredTweets;
    }

    /**
     * 过滤出由username写的推文中@到的其他人
     * @param tweets    // 过滤总列表
     * @param username  // 过滤由谁写的推文
     * @return          // username写的推文中，被@到了的用户set
     */
    public static Set<String> mentionedByAuthor(List<Tweet> tweets, String username) {
        // make a regex for extract "@" character

        List<Tweet> tweetsByUser = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if (username.equals(tweet.getAuthor())) {
                tweetsByUser.add(tweet);
            }
        }

        return Extract.getMentionedUsers(tweetsByUser);

//        final String reg = "\\s*[^\\w-]@[\\w-]+";
//        final Pattern pattern = Pattern.compile(reg);
//
//        List<Tweet> tweetsWrittenByCurrentAuthor = Filter.writtenBy(tweets, username);
//
//        // new mentioned set object
//        Set<String> mentionedUsers = new HashSet<>();
//        // go through the tweet list, and get each text to match
//        for (Tweet tweet : tweetsWrittenByCurrentAuthor) {
//            String text = tweet.getText();
//            Matcher matcher = pattern.matcher(text);
//            while (matcher.find()) {
//                String mentionedUsername = matcher.group().trim().replaceFirst("@", "");
//
//                if (!username.equals(mentionedUsername)) {
//                    // every time matcher finds a match then add it to the set
//                    String usernameToCheck = mentionedUsername.toLowerCase();
//                    if (!mentionedUsers.contains(usernameToCheck)) {
//                        mentionedUsers.add(mentionedUsername);
//                    }
//
//                }
//
//            }
//        }

//        return mentionedUsers;
    }
}
