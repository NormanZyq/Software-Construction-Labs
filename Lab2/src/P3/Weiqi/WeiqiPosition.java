package P3.Weiqi;

import P3.Interface.Position;
import P3.Piece;
import P3.Weiqi.Pieces.WeiqiPiece;

public class WeiqiPosition implements Position {

    private Piece piece;

    private int row;

    private int col;

    public WeiqiPosition(Piece piece, int row, int col) {
        this.piece = piece;
        this.row = row;
        this.col = col;
    }

    /**
     * 暂时没有用到
     * @param moveWhat  移动的棋子
     * @param from      起始点
     * @param to        目标点
     * @return  0
     */
    @Override
    public int whatWillHappen(Piece moveWhat, Position from, Position to) {
        return 0;
    }

    @Override
    public Piece getPiece() {
        return this.piece;
    }

    @Override
    public int row() {
        return this.row;
    }

    @Override
    public int col() {
        return this.col;
    }

    @Override
    public boolean setPiece(Piece newPiece) {
        if (!(newPiece instanceof WeiqiPiece)) return false;
        this.piece = newPiece;
        return true;
    }

    @Override
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
