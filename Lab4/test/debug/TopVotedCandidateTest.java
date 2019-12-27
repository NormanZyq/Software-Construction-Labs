package debug;
import static org.junit.Assert.*;

import org.junit.Test;
public class TopVotedCandidateTest {

    @Test
    public void firstTest(){
        int[] persons = {0,1,1,0,0,1,0};
        int[] times = {0, 5, 10, 15, 20, 25, 30};
        TopVotedCandidate topVotedCandidate = new TopVotedCandidate(persons, times);
        assertEquals(0, topVotedCandidate.q(3));
        assertEquals(1, topVotedCandidate.q(12));
        assertEquals(1, topVotedCandidate.q(25));
    }

    @Test
    public void secondTest(){
        int[] persons = {1,1,1,0,0,0};
        int[] times = {3,5,7,9,11,13};

        TopVotedCandidate topVotedCandidate = new TopVotedCandidate(persons, times);
        assertEquals(1, topVotedCandidate.q(10));
        assertEquals(1, topVotedCandidate.q(12));
        assertEquals(0, topVotedCandidate.q(13));
    }

    @Test
    public void thirdTest(){
        int[] persons = {1,1,1,1,1,1};
        int[] times = {1,2,3,4,5,6};

        TopVotedCandidate topVotedCandidate = new TopVotedCandidate(persons, times);
        assertEquals(1, topVotedCandidate.q(1));
        assertEquals(1, topVotedCandidate.q(4));
        assertEquals(1, topVotedCandidate.q(6));
        assertEquals(1, topVotedCandidate.q(8));
    }
    
}