package P3.Chess.Pieces;

import P3.Chess.ChessBoard;
import P3.Chess.ChessPosition;
import P3.Chess.ChessStatus;
import P3.Chess.ChessType;
import P3.Interface.Board;
import P3.Interface.Position;
import P3.Player;

public class Knight extends ChessPiece {
    private static final int TYPE = ChessType.KNIGHT;
    private static final char PIC = '马';

    private int status = ChessStatus.ALIVE;     // 棋子状态，默认为alive

    private final Player belongsTo;

    private final ChessPosition position;

    public Knight(Player belongsTo, int row, int col) {
        this.belongsTo = belongsTo;
        position = new ChessPosition(this, row, col);
    }

    private void setPosition(int row, int col) {
        this.position.setPosition(row, col);
    }

    /**
     * 马的移动合法性判断
     * 马走日，如果走的是日即可
     *
     * @param opeBoard      操作的棋盘
     * @param from  起始位置
     * @param to    目标位置
     * @return      根据上述规则判断出的是否允许移动，如果允许，返回true，否则为false
     */
    @Override
    public boolean canMove(Board opeBoard, Position from, Position to) {
        int fromRow = from.row();
        int fromCol = from.col();
        int toRow = to.row();
        int toCol = to.col();

        if (toRow >= ChessBoard.SIZE || toRow < 0 || toCol < 0 || toCol >= ChessBoard.SIZE) return false;

        if (Math.abs(fromRow - toRow) == 2 && Math.abs(fromCol - toCol) == 1) return true;
        if (Math.abs(fromRow - toRow) == 1 && Math.abs(fromCol - toCol) == 2) return true;

        return false;

    }

    @Override
    public char getPic() {
        return PIC;
    }

    @Override
    public void setPosition(Position newPosition) {
        this.setPosition(newPosition.row(), newPosition.col());
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
        return this.belongsTo;
    }

    @Override
    public boolean noBarrier(Board opeBoard, Position from, Position to, int moveType) {
        return true;
    }

    @Override
    public boolean afterAction(int actionType, Position from, Position to) {
        return true;
    }
}
