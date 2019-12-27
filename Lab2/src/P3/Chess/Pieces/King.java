package P3.Chess.Pieces;

import P3.Chess.ChessPosition;
import P3.Chess.ChessStatus;
import P3.Chess.ChessType;
import P3.Interface.Board;
import P3.Interface.Position;
import P3.Player;

public class King extends ChessPiece {
    private static final int TYPE = ChessType.KING;

    private static final char PIC = '王';

    private int status = ChessStatus.ALIVE;     // 棋子状态，默认为alive

    private boolean isFirstMove = true;     // 王记录这个信息似乎没什么卵用

    private final Player belongsTo;

    private final ChessPosition position;

    /**
     * 王的构造器
     * @param belongsTo 归属玩家
     * @param row       行数
     * @param col       列数
     */
    public King(Player belongsTo, int row, int col) {
        this.belongsTo = belongsTo;
        position = new ChessPosition(this, row, col);
    }

    private void setPosition(int row, int col) {
        this.position.setPosition(row, col);
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    /**
     * 王横竖行走一格、斜走一格均可
     *
     *
     * @param opeBoard 操作面板
     * @param from  起始位置
     * @param to    目标位置
     * @return      根据上述规则判断出的是否允许移动，如果允许，返回true，否则为false
     */
    @Override
    public boolean canMove(Board opeBoard, Position from, Position to) {
        if (!opeBoard.isLegalTargetPosition(to)) return false;

        // 目标位置合法
        int fromRow = from.row();
        int fromCol = from.col();
        int toRow = to.row();
        int toCol = to.col();


        return fromRow == toRow && Math.abs(fromCol - toCol) == 1 ||
                fromCol == toCol && Math.abs(fromRow - toRow) == 1 ||
                (Math.abs(fromRow - toRow) == 1 && Math.abs(fromCol - toCol) == 1);
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
        isFirstMove = false;
        return true;
    }
}
