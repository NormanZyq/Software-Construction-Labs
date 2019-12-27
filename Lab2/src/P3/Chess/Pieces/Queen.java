package P3.Chess.Pieces;

import P3.Chess.ChessPosition;
import P3.Chess.ChessStatus;
import P3.Chess.ChessType;
import P3.Interface.Board;
import P3.Interface.Position;
import P3.Player;

public class Queen extends ChessPiece {
    private static final int TYPE = ChessType.QUEEN;

    private static final char PIC = '后';

    private int status = ChessStatus.ALIVE;     // 棋子状态，默认为alive

    private boolean isFirstMove = true;     // 后记录这个信息似乎没什么卵用

    private Player belongsTo;

    private ChessPosition position;

    public Queen(Player belongsTo, int row, int col) {
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
     *
     *
     *
     * @param opeBoard  操作游戏面板
     * @param from      起始位置
     * @param to        目标位置
     * @return          根据上述规则判断出的是否允许移动，如果允许，返回true，否则为false
     */
    @Override
    public boolean canMove(Board opeBoard, Position from, Position to) {

        int fromRow = from.row();
        int fromCol = from.col();
        int toRow = to.row();
        int toCol = to.col();

        if (fromRow == toRow && fromCol == toCol) return false;     // 如果在原地，不可移动

        if (fromRow == toRow) {
            // 可能是横走
            return noBarrier(opeBoard, from, to, 1);
        } else if (fromCol == toCol) {
            // 可能是竖走
            return noBarrier(opeBoard, from, to, 2);
        } else if (Math.abs(fromRow - toRow) == Math.abs(fromCol - toCol)) {
            // 如果行之差==列之差，则是斜走
            return noBarrier(opeBoard, from, to, 3);
        }

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

    /**
     * 判断Queen棋子从from到to的合法路径之间是否有其他棋子阻挡
     * Queen有两种走法：直走、斜走，直走可细分为横走、竖走
     *
     * @param opeBoard      操作的游戏面板
     * @param from          起始点
     * @param to            目标点
     * @param moveType      移动类型
     * @return              没有障碍返回true，否则为false
     */
    @Override
    public boolean noBarrier(Board opeBoard, Position from, Position to, int moveType) {
        int fromRow = from.row();
        int fromCol = from.col();
        int toRow = to.row();
        int toCol = to.col();

        switch (moveType) {
            case 1:     // 横走
                if (toCol - fromCol > 0) {
                    // 向右移动
                    for (int currentCol = fromCol + 1; currentCol < toCol; currentCol++) {
                        // 不会判断到目标位置，会判断到目标位置的前一格
                        if (opeBoard.getPieceOnBoard(fromRow, currentCol).belongsTo() != null)
                            return false;     // 移动方向被棋子阻挡，不可移动
                    }

                } else {
                    // 向左移动
                    for (int currentCol = fromCol - 1; currentCol > toCol; currentCol--) {
                        // 不会判断到目标位置，会判断到目标位置的前一格
                        if (opeBoard.getPieceOnBoard(fromRow, currentCol).belongsTo() != null)
                            return false;     // 移动方向被棋子阻挡，不可移动
                    }
                }
                return true;
//                break;
            case 2:     // 竖走
                // 列相同时，行必须不同
                if (toRow - fromRow > 0) {
                    // 向上移动
                    for (int currentRow = fromRow + 1; currentRow < toRow; currentRow++) {
                        // 不会判断到目标位置，会判断到目标位置的前一格
                        if (opeBoard.getPieceOnBoard(currentRow, fromCol).belongsTo() != null)
                            return false;     // 移动方向被棋子阻挡，不可移动
                    }
                } else {
                    // 向下移动
                    for (int currentRow = fromRow - 1; currentRow > toRow; currentRow--) {
                        // 不会判断到目标位置，会判断到目标位置的前一格
                        if (opeBoard.getPieceOnBoard(currentRow, fromCol).belongsTo() != null)
                            return false;     // 移动方向被棋子阻挡，不可移动
                    }
                }
                return true;
            case 3:     // 斜走
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
        return false;

    }

    @Override
    public boolean afterAction(int actionType, Position from, Position to) {
        isFirstMove = false;
        return true;
    }
}











