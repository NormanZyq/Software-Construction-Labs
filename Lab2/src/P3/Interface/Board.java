package P3.Interface;

import P3.Piece;

public interface Board {

    /**
     * 通过棋子获得其位置
     * @param piece     需要获得位置的棋子
     * @return          棋子的位置
     */
    Position getPositionByPiece(Piece piece);

    /**
     * get the piece on board by row and col number
     * @param row   row number
     * @param col   col number
     * @return      piece object on (row,col)
     */
    Piece getPieceOnBoard(int row, int col);

    /**
     * get the piece on board by position
     * @param position  position you want to get the piece
     * @return      the piece on the position
     */
    Piece getPieceOnBoard(Position position);

    /**
     * move piece from
     * @param ope
     * @param from
     * @param to
     * @return
     */
    boolean movePieceOnBoard(Piece ope, Position from, Position to);

    String queryCapture(Position position);

    void printBoard();

    boolean isLegalTargetPosition(Position target);
}
