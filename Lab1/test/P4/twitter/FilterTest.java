/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P4.twitter;

import P4.twitter.Filter;
import P4.twitter.Timespan;
import P4.twitter.Tweet;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FilterTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-18T11:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "zz", "I found your pen in my book. @iceiwant", d3);
    private static final Tweet tweet4 = new Tweet(4, "iceiwant", "Thank you. @zz. Did you find my CSAPP book? @yyy .';build-time.", d2);
    private static final Tweet tweet5 = new Tweet(5, "alyssa", "ok.", d2);

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /**
     * 测试空集按作者筛选
     */
    @Test
    public void testWrittenByEmpty() {
        List<Tweet> testTweets = new ArrayList<>();

        // 空的，长度应该为0
        List<Tweet> writtenBy = Filter.writtenBy(testTweets, "alyssa");
        assertEquals("expected singleton list", 0, writtenBy.size());
    }

    /**
     * 测试多数据按作者筛选，且结果长度应该为1
     */
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> testTweets = new ArrayList<>();

        testTweets.add(tweet1);
        testTweets.add(tweet2);

        List<Tweet> writtenBy = Filter.writtenBy(testTweets, "alyssa");

        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    /**
     * 测试多数据按作者筛选，且结果长度应>1
     */
    @Test
    public void testWrittenByMultipleTweetsMultipleResult() {

        List<Tweet> testTweets = new ArrayList<>();

        testTweets.add(tweet1);
        testTweets.add(tweet2);
        testTweets.add(tweet5);
        List<Tweet> writtenBy = Filter.writtenBy(testTweets, "alyssa");
        assertEquals("expected singleton list", 2, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet5));
    }

    /**
     * 测试时间区间
     */
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }

    /**
     * 测试包含文字，测试内容包含多个单词，但返回结果长度应为0
     */
    @Test
    public void testContainingEmpty() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3, tweet4), Arrays.asList("notaword", "hjkdf"));

        assertTrue("expected empty list", containing.isEmpty());
    }

    /**
     * 测试包含文字，测试内容仅包含一个单词
     */
    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    /**
     * 测试包含文字，
     * 测试多组字符串列表，比上个测试更加刁钻，
     * 主要测试有特殊内容：
     * 包括大小写忽略、特殊符号附近的单词。
     */
    @Test
    public void testContainingStrict() {
        List<Tweet> containing1 = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3, tweet4), Arrays.asList("book", "talk"));
        List<Tweet> answerList1 = Arrays.asList(tweet1, tweet2, tweet3, tweet4);
        assertEquals("should contains \"book\" or \"talk\"", answerList1, containing1);

        List<Tweet> containing2 = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3, tweet4), Arrays.asList("talk"));
        List<Tweet> answerList2 = Arrays.asList(tweet1, tweet2);
        assertEquals("should contains \"talk\"", answerList2, containing2);

        List<Tweet> containing3 = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3, tweet4), Arrays.asList("build-time"));
        List<Tweet> answerList3 = Arrays.asList(tweet4);
        assertEquals("should contains \"build\"", answerList3, containing3);

        List<Tweet> containing4 = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3, tweet4), Arrays.asList("in", "hype", "BUILD-TIME"));
        List<Tweet> answerList4 = Arrays.asList(tweet2, tweet3, tweet4);
        assertEquals("should contains \"build\"", answerList4, containing4);

    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}
