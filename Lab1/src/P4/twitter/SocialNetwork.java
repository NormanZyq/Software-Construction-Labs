/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P4.twitter;

import java.util.*;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followGraph = new HashMap<>();

        for (Tweet tweetForAuthor : tweets) {
            Set<String> usernameMentionUsersByCurrentAuthor = Filter.mentionedByAuthor(tweets, tweetForAuthor.getAuthor());

            followGraph.put(tweetForAuthor.getAuthor(), usernameMentionUsersByCurrentAuthor);
        }

        return followGraph;
    }

    /**
     * 在原始的交际圈猜测中做出改进，原本只根据a提到了b来推测a关注b，
     * 而现在假设关注具有传递性，即a提到了b，b提到了c，能反应a关注了b，b关注了c，进而a关注了c，
     * 本方法通过这个做出进一步的交际圈猜测
     * @param tweets    推文
     * @return          新的交际圈
     */
    public static Map<String, Set<String>> smarterGuessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> simpleFollowGraph = guessFollowsGraph(tweets);
        Map<String, Set<String>> complexFollowGraph = new HashMap<>(simpleFollowGraph);

        // 遍历简单交际圈map的所有key
        for (Map.Entry<String, Set<String>> entry : simpleFollowGraph.entrySet()) {
            String aUser = entry.getKey();      // 记录key，作为a用户
            Set<String> newAFollowsSet = new HashSet<>();   // 声明一个增量set，表示是a用户可能关注的更多的用户

            // 遍历交际圈map的所有value，记录为b用户
            for (String bUser : entry.getValue()) {
                try {
                    // 通过b用户名遍历交际圈map中b关注的用户，而这是个set，所以再遍历这个set里面的所有内容，这个用户名作为c用户
                    for (String cUser : simpleFollowGraph.get(bUser)) {
                        // 将a->c的关系添加到a的增量关注表
                        if (!aUser.equals(cUser)) {
                            newAFollowsSet.add(cUser);
                        }
                    }
                } catch (NullPointerException ex) {
                    // 可能出现只被at了的用户，所以行中没有他，可能空指针异常
                }
            }
            complexFollowGraph.get(aUser).addAll(newAFollowsSet);       // 将增量关注列表增加给a用户
        }
        return simpleFollowGraph;
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        /*
            what to do:
            为了通过follow关系图计算粉丝数目，
            而该关系图的行可视为一个表格，每行都是一个Set，Set的索引是用户名，Set里储存的是当前行的用户关注的人

            所以需要解析所有Set，结合当前索引得知当前作为索引的用户是谁的粉丝，为该用户的粉丝数++
         */

        class User {
            private String username;
            private int follower;

            public User(String username) {
                this.username = username;
                this.follower = 0;
            }

            public String getUsername() {
                return username;
            }

            public int getFollower() {
                return follower;
            }

            public void appendFollower() {
                ++this.follower;
            }

            @Override
            public String toString() {
                return username;
            }
        }

        List<User> users = new ArrayList<>();   // 初始化user列表

        // 遍历所有key和value
        for (Map.Entry<String, Set<String>> entry : followsGraph.entrySet()) {
            String currentUsername = entry.getKey();    // 获得作为索引的用户名
            User currentUser = new User(currentUsername);   // 创建用户的对象，粉丝数初始化为0
            users.add(currentUser);     // 添加到user列表

            for (Set<String> usernameSets : followsGraph.values()) {
                for (String username : usernameSets) {      // entry.getValue()返回的是Set
                    // 每次getValue()都是一个新的Set，遍历这个Set的所有内容
                    if (currentUsername.equals(username)) {     // 如果作为索引的用户名出现在了这个Set中，则说明多了一个粉丝
                        currentUser.appendFollower();   // 粉丝数++
                    }
                }
            }
        }

        users.sort((o1, o2) -> o2.getFollower() - o1.getFollower());

        for (User user : users) {
            System.out.println(user.getUsername() + " has " + user.getFollower() + " followers.");
        }

        // 仅将user列表中的用户名复制出来
        List<String> usernames = new ArrayList<>();
        for (User user : users) {
            usernames.add(user.getUsername());
        }
        return usernames;
//        throw new RuntimeException("not implemented");
    }

}
