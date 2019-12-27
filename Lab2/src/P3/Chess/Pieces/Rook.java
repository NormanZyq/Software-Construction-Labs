package P3.Chess.Pieces;

import P3.Chess.ChessPosition;
import P3.Chess.ChessStatus;
import P3.Chess.ChessType;
import P3.Interface.Board;
import P3.Interface.Position;
import P3.Player;

public class Rook extends ChessPiece {

    private static final int TYPE = ChessType.ROCK;       // 棋子类型

    private static final char PIC = '车';         // 棋子图案

    private int status = ChessStatus.ALIVE;     // 棋子状态，默认为alive

    private boolean isFirstMove = true;     // 车记录这个信息似乎没什么卵用

    private Player belongsTo;

    private ChessPosition position;

    public Rook(Player belongsTo, int row, int col) {
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
     * 根据车的规则判断是否能够从from移动到to
     *
     * @param opeBoard  操作的面板
     * @param from  起始位置
     * @param to    目标位置
     * @return      根据上述规则判断出的是否允许移动，如果允许，返回true，否则为false
     */
    @Override
    public boolean canMove(Board opeBoard, Position from, Position to) {
        return noBarrier(opeBoard, from, to, -1);

//        if (from.row() == to.row() && from.col() != to.col()) {
//            // 行相同时，列必须不同，横向行棋，变动列
//
//
//        } else if ((from.col() == to.col() && from.row() != to.row())) {
//            // 列相同时，行必须不同
//
//        }
//
//        // 其他情况，不可移动
//        return false;
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
        int fromRow = from.row();
        int fromCol = from.col();
        int toRow = to.row();
        int toCol = to.col();

        if (fromRow == toRow && fromCol != toCol) {
            // 行相同时，列必须不同，横向行棋，变动列
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

            // 未被阻挡，可以移动
            return true;

        } else if ((fromCol == toCol && fromRow != toRow)) {
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
        }
        return false;

    }

    @Override
    public boolean afterAction(int actionType, Position from, Position to) {
        isFirstMove = false;
        return true;
    }


}
