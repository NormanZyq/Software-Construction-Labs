/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P4.twitter;

import P4.twitter.Extract;
import P4.twitter.Timespan;
import P4.twitter.Tweet;
import org.junit.Test;

import java.time.Instant;
import java.util.*;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-18T11:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-15T11:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "zz", "I found your pen in my bag. @iceiwant", d3);
    private static final Tweet tweet4 = new Tweet(4, "iceiwant", "Thank you. @zz. Did you find my CSAPP book? @yyy", d4);
    private static final Tweet tweet5 = new Tweet(5, "wang", "my email is nhjskd@hit.edu.cn", d2);
    private static final Tweet tweet6 = new Tweet(6, "wang", "@hitt.edu.cn", d2);
    private static final Tweet tweet7 = new Tweet(7, "wang", "@iceiwantq@jisdf @ra @ra @rb @rc- mit@hit.edu.cn @haveme/ @nome//", d2);

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4));
        
        assertEquals("expected start", d4, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }

    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));

        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsers() {
        List<Tweet> testTweets = new ArrayList<>();
        testTweets.add(tweet3);
        testTweets.add(tweet4);
        testTweets.add(tweet5);
        testTweets.add(tweet6);
        testTweets.add(tweet7);

        Set<String> answer = new HashSet<>();

        answer.add("yyy");
        answer.add("iceiwant");
        answer.add("zz");
        answer.add("ra");
        answer.add("rb");
        answer.add("rc-");
        answer.add("iceiwantq");
        answer.add("hitt");
        answer.add("haveme");
        answer.add("nome");         // lenient reg makes containing this
        assertEquals(answer, Extract.getMentionedUsers(testTweets));

    }


    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
