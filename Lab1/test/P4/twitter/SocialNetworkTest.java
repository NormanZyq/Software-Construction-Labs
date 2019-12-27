/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P4.twitter;

import P4.twitter.SocialNetwork;
import P4.twitter.Tweet;
import org.junit.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */


    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");

    public static final Tweet tweet1 = new Tweet(1, "stuA", "I love you @stuB", d1);
    public static final Tweet tweet2 = new Tweet(2, "stuB", "Thank you @stuA", d2);
    public static final Tweet tweet3 = new Tweet(3, "zz", "@yyy hi @stuB @stuA", d2);
    public static final Tweet tweet4 = new Tweet(4, "yyy", "ok @wang @stuB", d2);
    public static final Tweet tweet5 = new Tweet(5, "wang", "come to my office tomorrow @stuA @stuB", d2);
    public static final Tweet tweet6 = new Tweet(6, "wang", "@hahaha @yyy", d2);
//    public static final Tweet tweet7 = new Tweet(7, "wang", "@hahaha @yyy", d2);
//    private static final Tweet tweet6 = new Tweet(6, "wang", "@hit.edu.cn", d2);
//    private static final Tweet tweet7 = new Tweet(7, "wang", "@iceiwant@jisdf @ra @ra @rb @rc- @hit.edu.cn @haveme// @nome://", d2);


    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /**
     * 这个test用于测试GetSmarter部分，猜测关注具有传递性，由此作为依据猜测交际圈
     */
    @Test
    public void testSmarterFollowsGraph() {
        List<Tweet> testTweets = new ArrayList<>();

        testTweets.add(tweet1);
        testTweets.add(tweet2);
        testTweets.add(tweet3);
        testTweets.add(tweet4);
        testTweets.add(tweet5);
        testTweets.add(tweet6);

        Map<String, Set<String>> followsGraphAnswer = new HashMap<>();

        Set<String> answerP1 = new HashSet<>();
        answerP1.add("stuB");
        followsGraphAnswer.put("stuA", answerP1);

        Set<String> answerP2 = new HashSet<>();
        answerP2.add("stuA");
        followsGraphAnswer.put("stuB", answerP2);

        Set<String> answerP3 = new HashSet<>();
        answerP3.add("yyy");
        answerP3.add("stuB");
        answerP3.add("stuA");
        answerP3.add("wang");
        followsGraphAnswer.put("zz", answerP3);

        Set<String> answerP4 = new HashSet<>();
        answerP4.add("wang");
        answerP4.add("stuA");
        answerP4.add("hahaha");
        answerP4.add("stuB");
        followsGraphAnswer.put("yyy", answerP4);

        Set<String> answerP5 = new HashSet<>();
        answerP5.add("stuA");
        answerP5.add("stuB");
        followsGraphAnswer.put("wang", answerP5);

        Set<String> answerP6 = new HashSet<>();
        answerP6.add("hahaha");
        answerP6.add("yyy");
        followsGraphAnswer.get("wang").addAll(answerP6);

        Map<String, Set<String>> followsGraph = SocialNetwork.smarterGuessFollowsGraph(testTweets);

        assertEquals(followsGraphAnswer, followsGraph);

    }

    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    @Test
    public void testGuessFollowerNotEmpty() {
        List<Tweet> testTweets = new ArrayList<>();

        testTweets.add(tweet1);
        testTweets.add(tweet2);
        testTweets.add(tweet3);
        testTweets.add(tweet4);
        testTweets.add(tweet5);
        testTweets.add(tweet6);


        Map<String, Set<String>> followsGraphAnswer = new HashMap<>();

        Set<String> answerP1 = new HashSet<>();
        answerP1.add("stuB");
        followsGraphAnswer.put("stuA", answerP1);

        Set<String> answerP2 = new HashSet<>();
        answerP2.add("stuA");
        followsGraphAnswer.put("stuB", answerP2);

        Set<String> answerP3 = new HashSet<>();
        answerP3.add("yyy");
        answerP3.add("stuB");
        answerP3.add("stuA");
        followsGraphAnswer.put("zz", answerP3);

        Set<String> answerP4 = new HashSet<>();
        answerP4.add("wang");
        answerP4.add("stuB");
        followsGraphAnswer.put("yyy", answerP4);

        Set<String> answerP5 = new HashSet<>();
        answerP5.add("stuA");
        answerP5.add("stuB");
        followsGraphAnswer.put("wang", answerP5);

        Set<String> answerP6 = new HashSet<>();
        answerP6.add("hahaha");
        answerP6.add("yyy");
        followsGraphAnswer.get("wang").addAll(answerP6);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(testTweets);

        assertEquals(followsGraphAnswer, followsGraph);

    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    @Test
    public void testInfluencersNotEmpty() {
        List<Tweet> testTweets = new ArrayList<>();

        testTweets.add(tweet1);
        testTweets.add(tweet2);
        testTweets.add(tweet3);
        testTweets.add(tweet4);
        testTweets.add(tweet5);
        testTweets.add(tweet6);

        // Actual   :{zz=[yyy, stuB], wang=[hahaha, stuB, yyy, stuA], stuB=[stuA], yyy=[wang], stuA=[stuB]}
        List<String> answer = new ArrayList<>();
        answer.add("stuB");
        answer.add("stuA");
        answer.add("yyy");
        answer.add("wang");
        answer.add("zz");

        assertEquals(answer, SocialNetwork.influencers(SocialNetwork.guessFollowsGraph(testTweets)));


    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
