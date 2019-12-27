package P3.Chess.Pieces;

import P3.Chess.ChessAction;
import P3.Chess.ChessStatus;
import P3.Interface.Board;
import P3.Piece;
import P3.Interface.Position;

public abstract class ChessPiece extends Piece implements ChessAction {

    /*
        AF
        this abstract class is a model of every chess piece including pawn,
            bishop, king, knight, queen, rook, and these are also their type field.
        they all have their status and type(mentioned above). status represents a status in a chess game,
            for example, alive means the piece is still working. dead means it has benn eaten.
        they have their positions. position means the (row,col) in a chess board.
     */

    /*
        RI
        true
     */

    /*
        safety from exposure
        when returning a mutable field, it will be a copied version
     */

    private int status = ChessStatus.NOT_SET;

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public abstract boolean canMove(Board opeBoard, Position from, Position to);
}
