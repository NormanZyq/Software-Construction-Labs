package P3;

import P3.Weiqi.Pieces.WeiqiType;
import P3.Weiqi.WeiqiBoard;
import P3.Weiqi.WeiqiPlayer;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeiqiTest {

    /*
           test for weiqi
           1. test put operation
               1.1 able to put
               1.2 can't put to the place

           2. test pick operation
               2.1 can pick away
               2.2 cannot pick away

           3. test query

     */

    private WeiqiBoard newBoard() {
        WeiqiPlayer playerBlack = new WeiqiPlayer("Black", 1);
        WeiqiPlayer playerWhite = new WeiqiPlayer("White", 2);

        return new WeiqiBoard(playerBlack, playerWhite);
    }

    /**
     * test put a new piece to board
     */
    @Test
    public void testPut() {
        WeiqiBoard wb = newBoard();

        assertTrue(wb.addPiece(wb.getCurrent(), 14, 10));
        assertEquals(WeiqiType.BLACK, wb.getPieceOnBoard(14, 10).type());

    }

    /**
     * test pick a piece awaay from the board
     */
    @Test
    public void testPick() {
        WeiqiBoard wb = newBoard();

        wb.addPiece(wb.getCurrent(), 14, 10);

        assertFalse(wb.removePiece(wb.getCurrent(), 14, 11));
        assertTrue(wb.removePiece(wb.getCurrent(), 14, 10));

    }

    /**
     * test query a location whether it is captured or not
     */
    @Test
    public void testQuery() {
        WeiqiBoard wb = newBoard();

        // now not captured
        assertEquals("查询位置未被占用", wb.queryCapture(wb.getPieceOnBoard(14, 10).getPosition()));

        // now captured
        wb.addPiece(wb.getCurrent(), 14, 10);
        assertEquals("查询位置被黑方玩家Black占用", wb.queryCapture(wb.getPieceOnBoard(14, 10).getPosition()));
    }

}
