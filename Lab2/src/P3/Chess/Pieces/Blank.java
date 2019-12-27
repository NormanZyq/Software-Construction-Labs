package P3.Chess.Pieces;

import P3.Chess.ChessPosition;
import P3.Chess.ChessStatus;
import P3.Chess.ChessType;
import P3.Interface.Board;
import P3.Interface.Position;
import P3.Player;

public class Blank extends ChessPiece {
    private static final int TYPE = ChessType.BLANK;       // 棋子类型

    private static final char PIC = '　';         // 棋子图案

//    private int status = ChessStatus.BACK_UP;     // 空白棋子，棋子状态

//    private Player belongsTo;

    private final ChessPosition position;

    public Blank(int x, int y) {
        this.position = new ChessPosition(this, x, y);
    }

    @Override
    public char getPic() {
        return PIC;
    }

    @Override
    public void setPosition(Position newPosition) {

    }

    @Override
    public Position getPosition() {
        return this.position;
    }
    @Override
    public int type() {
        return TYPE;
    }

    @Override
    public Player belongsTo() {
        return null;
    }
    @Override
    public boolean canMove(Board opeBoard, Position from, Position to) {
        return false;
    }

    @Override
    public boolean noBarrier(Board opeBoard, Position from, Position to, int moveType) {
        return false;
    }

    @Override
    public boolean afterAction(int actionType, Position from, Position to) {
        return false;
    }
}
