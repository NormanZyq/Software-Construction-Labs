package P3.Chess.Pieces;

import P3.Chess.ChessPosition;
import P3.Chess.ChessStatus;
import P3.Chess.ChessType;
import P3.Interface.Board;
import P3.Piece;
import P3.Interface.Position;
import P3.Player;

public class Pawn extends ChessPiece {
    private static final int TYPE = ChessType.PAWN;

    private static final char PIC = '兵';

    private int status = ChessStatus.ALIVE;     // 棋子状态，默认为alive

    private boolean isFirstMove = true;     // 只有第一次行走时可以走两格

    private final Player belongsTo;

    private ChessPosition position;

    public Pawn(Player belongsTo, int row, int col) {
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
     * 兵的移动规矩判定
     * 兵在第一次移动的时候可以移动一格或者两格，黑棋只能向下走，白棋只能向上走
     * 第二次移动开始只能向前走一格
     *
     * 但是，如果斜前方有敌方棋子，可以斜走并吃掉
     *
     *
     * @param opeBoard
     * @param from  起始位置
     * @param to    目标位置
     * @return      根据上述规则判断出的是否允许移动，如果允许，返回true，否则为false
     */
    @Override
    public boolean canMove(Board opeBoard, Position from, Position to) {
        // todo 有个bug，如果走两格时，中间的格有子，依然会允许行走
        int fromRow = from.row();
        int fromCol = from.col();
        int toRow = to.row();
        int toCol = to.col();

        if (fromRow == toRow) return false;     // 行相同，直接返回false
        Piece opePiece = from.getPiece();   // 操作棋子

        if (opePiece.belongsTo().TAG == 1) {
            // 玩家A，黑棋，向下
            if (fromRow < toRow) return false;   // 向下走时，目标行要更小

            // 此处行（向下走）已经满足要求
            // 判断是否往斜前方走动
            if (Math.abs(toCol - fromCol) == 1) {     // 向斜前方走动
                // 只有斜前方存在敌方棋子的时候才能走动，否则不允许
                return to.getPiece().belongsTo().TAG == 2;
            }

            // 向前方走动
            return isLegalMoveRows(toRow, fromCol, fromRow, toCol);

        } else {
            // 玩家B 白棋，向上
            if (fromRow > toRow) return false;   // 向上走时，目标行要更大

            // 此处行（向上走）已经满足要求
            // 判断是否往斜前方走动
            if (Math.abs(toCol - fromCol) == 1) {     // 向斜前方走动
                // 只有斜前方存在敌方棋子的时候才能走动，否则不允许
                return to.getPiece().belongsTo().TAG == 1;
            }

            return isLegalMoveRows(fromRow, fromCol, toRow, toCol);
        }
    }

    /**
     * idea 自己提取的方法，用于兵棋子最后的判断，包括：
     * 是否正在进行行移动（此时只允许进行行移动）、
     * 根据兵是否为第一次移动来判断能否走两格
     *
     * @param fromRow   起始行
     * @param fromCol   起始列
     * @param toRow     目标行
     * @param toCol     目标列
     * @return          合法返回true，否则返回false
     */
    private boolean isLegalMoveRows(int fromRow, int fromCol, int toRow, int toCol) {
        if (fromCol != toCol) return false;     // 如果还列不同，就不允许了
        int moveRows = toRow - fromRow;         // > 0
        if (isFirstMove) {
            return moveRows <= 2;
        } else {
            return moveRows <= 1;
        }
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
