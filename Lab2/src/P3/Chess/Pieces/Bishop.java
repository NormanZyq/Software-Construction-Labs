package P3.Chess.Pieces;

import P3.Chess.ChessPosition;
import P3.Chess.ChessStatus;
import P3.Chess.ChessType;
import P3.Interface.Board;
import P3.Interface.Position;
import P3.Player;

public class Bishop extends ChessPiece {
    private static final int TYPE = ChessType.BISHOP;

    private static final char PIC = '象';

    private int status = ChessStatus.ALIVE;     // 棋子状态，默认为alive

    private final Player belongsTo;

    private final ChessPosition position;

    public Bishop(Player belongsTo, int row, int col) {
        this.belongsTo = belongsTo;
        position = new ChessPosition(this, row, col);
    }

    private void setPosition(int row, int col) {
        this.position.setPosition(row, col);
    }

    /**
     * 兵的移动规矩判定
     * 兵在第一次移动的时候可以移动一格或者两格，黑棋只能向下走，白棋只能向上走
     * 第二次移动开始只能向前走一格
     *
     * 但是，如果斜前方有敌方棋子，可以斜走并吃掉
     *
     *
     * @param opeBoard  操作的游戏面板
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

        // 因象斜走，所以如果行之差≠列之差（都指绝对值），或者行、列中有一个没发生变化（例如是原地移动的情况，满足前半部分），就不满足要求
        if (Math.abs(fromRow - toRow) != Math.abs(fromCol - toCol) || fromRow == toRow || fromCol == toCol) return false;

        // 现在满足列的要求：
        return noBarrier(opeBoard, from, to, -1);


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

    /**
     * 象斜走，判断路径上是否有挡路的棋子
     * @param opeBoard  操作的游戏面板
     * @param from      起始点
     * @param to        目标点
     * @param moveType
     * @return          无阻挡，返回true，否则返回false
     */
    @Override
    public boolean noBarrier(Board opeBoard, Position from, Position to, int moveType) {
        // todo 有bug，有时候无法判断阻挡
        int fromRow = from.row();
        int fromCol = from.col();
        int toRow = to.row();
        int toCol = to.col();

        if (fromRow > toRow) {
            // 向下走
            if (toCol - fromCol > 0) {
                // 右下方走
                for (int row = fromRow - 1, col = fromCol + 1; row > toRow; row--, col++) {     // 应该判断条件只用一个就行了
                    if (opeBoard.getPieceOnBoard(row, col).belongsTo() != null) return false;
                }
            } else {
                // 左下方走
                for (int row = fromRow - 1, col = fromCol - 1; row > toRow; row--, col--) {     // 应该判断条件只用一个就行了
                    if (opeBoard.getPieceOnBoard(row, col).belongsTo() != null) return false;
                }
            }
        } else {
            // 向上走
            if (toCol - fromCol > 0) {
                // 右上方走
                for (int row = fromRow + 1, col = fromCol + 1; row < toRow; row++, col++) {     // 应该判断条件只用一个就行了
                    if (opeBoard.getPieceOnBoard(row, col).belongsTo() != null) return false;
                }
            } else {
                // 左上方走
                for (int row = fromRow + 1, col = fromCol - 1; row < toRow; row++, col--) {     // 应该判断条件只用一个就行了
                    if (opeBoard.getPieceOnBoard(row, col).belongsTo() != null) return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean afterAction(int actionType, Position from, Position to) {

        return true;
    }
}
