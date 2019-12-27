package P3;

import P3.Chess.ChessBoard;
import P3.Chess.ChessPlayer;
import P3.Chess.ChessType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChessTest {

    /*
           国际象棋测试
           this test contains these parts:
           1. test move function.
              1.1 use the return value of move function
              1.2 assert the old location is not the moved piece again
              1.3 assert the new location is the moved piece

           2. test querying capture situation
     */

    /**
     * create a new chess board
     * @return      a new chess board object
     */
    private ChessBoard newBoard() {
        ChessPlayer playerA = new ChessPlayer("Black", 1);
        ChessPlayer playerB = new ChessPlayer("White", 2);

        return new ChessBoard(playerA, playerB);
    }

    /**
     * test move a piece on board
     */
    @Test
    public void testMove() {
        ChessBoard cb = newBoard();

        Piece ope = cb.getPieceOnBoard(6, 0);
        Piece target = cb.getPieceOnBoard(5, 0);

        assertTrue(cb.movePieceOnBoard(ope, ope.getPosition(), target.getPosition()));

        assertEquals(ChessType.BLANK, cb.getPieceOnBoard(6, 0).type());
        assertEquals(ChessType.PAWN, cb.getPieceOnBoard(5, 0).type());
    }

    /**
     * test query a location whether it is captured or not
     */
    @Test
    public void testQuery() {
        ChessBoard cb = newBoard();

        assertEquals("查询位置未被占用", cb.queryCapture(cb.getPieceOnBoard(5, 0).getPosition()));
    }

}
