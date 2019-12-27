package debug;
/*
 * In an election, the i-th vote was cast for persons[i] at time times[i].
 * <p>
 * Now, we would like to implement the following query function:
 * TopVotedCandidate.q(int t) will return the number of the person that was
 * leading the election at time t.
 * <p>
 * Votes cast at time t will count towards our query. In the case of a tie, the
 * most recent vote (among tied candidates) wins.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: ["TopVotedCandidate","q","q","q","q","q","q"],
 * [[[0,1,1,0,0,1,0],[0,5,10,15,20,25,30]],[3],[12],[25],[15],[24],[8]]
 * Output:
 * [null,0,1,1,0,0,1]
 * <p>
 * Explanation:
 * At time 3, the votes are [0], and 0 is leading.
 * At time 12, the votes are [0,1,1], and 1 is leading.
 * At time 25, the votes are [0,1,1,0,0,1], and 1 is leading (as ties go to the most recent
 * vote.)
 * This continues for 3 more queries at time 15, 24, and 8.
 * <p>
 * <p>
 * Note:
 * <p>
 * 1 <= persons.length = times.length <= 5000
 * 0 <= persons[i] <= persons.length
 * times is a strictly increasing array with all elements in [0, 10^9].
 * TopVotedCandidate.q is called at most 10000 times per test case.
 * TopVotedCandidate.q(int t) is always called with t >= times[0].
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TopVotedCandidate {
    private List<List<Vote>> A = new ArrayList<>();

    public TopVotedCandidate(int[] persons, int[] times) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int i = 0; i < persons.length; ++i) {
            int p = persons[i], t = times[i];
            int c = count.getOrDefault(p, 1);

            count.put(p, c);
            while (A.size() <= c) {
                A.add(new ArrayList<>());
            }
            A.get(p).add(new Vote(p, t));
        }
    }

    public int q(int t) {
        int lo = 0, hi = A.get(0).size() - 1;
        int i;
        int j;
        if (A.get(0).size() != 0) {
            while (lo < hi - 1) {
                int mi = lo + (hi - lo) / 2;
                int time = A.get(0).get(mi).time;
                if (time <= t) {
                    lo = mi;
                } else {
                    hi = mi;
                }
            }
            if (A.get(0).get(lo).time > t) {
                i = 0;
            } else if (A.get(0).get(hi).time <= t) {
                i = lo + 2;
            } else {
                i = lo + 1;
            }
        } else {
            i = 0;
        }

        lo = 0;//0
        hi = A.get(1).size() - 1;
        if (A.get(1).size() != 0) {
            while (lo < hi - 1) {
                int mi = lo + (hi - lo) / 2;
                int time = A.get(1).get(mi).time;
                if (time <= t) {
                    lo = mi;
                } else {
                    hi = mi;
                }
            }
            if (A.get(1).get(lo).time > t) {
                j = 0;
            } else if (A.get(1).get(hi).time <= t) {
                j = lo + 2;
            } else {
                j = Math.max(lo, 0) + 1;
            }
        } else {
            j = 0;
        }

        int max = 0;
        int fin = 0;
        for (List<Vote> list1 : A) {
            for (Vote vote : list1) {
                if (vote.time <= t && vote.time >= max) {
                    max = vote.time;
                    fin = vote.person;
                }
            }
        }
        if (i > j) {
            return 0;
        } else if (i < j) {
            return 1;
        } else return fin;
    }
}

class Vote {
    int person;
    int time;

    Vote(int p, int t) {
        person = p;
        time = t;
    }
}