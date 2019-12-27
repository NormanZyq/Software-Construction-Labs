package P3.Chess;

import P3.Chess.Pieces.ChessPiece;
import P3.Piece;
import P3.Interface.Position;

public class ChessPosition implements Position {

    private Piece piece;

    private int row;

    private int col;

    public ChessPosition(Piece piece, int row, int col) {
        this.piece = piece;
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean setPiece(Piece newPiece) {
        if (!(newPiece instanceof ChessPiece)) return false;
        this.piece = newPiece;
        return true;
    }

    @Override
    public int whatWillHappen(Piece moveWhat, Position from, Position to) {
        return 0;
    }

    @Override
    public Piece getPiece() {
        return piece;
    }

    @Override
    public int row() {
        return row;
    }

    @Override
    public int col() {
        return col;
    }

    @Override
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }


}
