package P3.Weiqi.Pieces;

import P3.Piece;
import P3.Interface.Position;
import P3.Player;
import P3.Weiqi.WeiqiAction;
import P3.Weiqi.WeiqiPosition;

public class WeiqiPiece extends Piece implements WeiqiAction {

    /*
        AF
        this represents weiqi piece, including two types: black and white, distinguished by type field
     */

    /*
        RI
        true
     */

    /*
        safety from exposure
        when returning a mutable field, it will be a copied version
     */

    private final int TYPE;     // 棋子类型

    private final char PIC;     // 棋子"图片"

    private int status = WeiqiStatus.ALIVE;     // 棋子状态

    private final Player BELONGS_TO;    // 归属者

    private final WeiqiPosition position;      // 棋子在棋盘中的位置

    /**
     * fast create a blank piece on (row,col)
     * @param row   row
     * @param col   column
     * @return      a new blank piece object with its position property is (row,col)
     */
    public static WeiqiPiece newBlankPiece(int row, int col) {
        return new WeiqiPiece(WeiqiType.BLANK, null, row, col);
    }

    /**
     * @param TYPE          type contains only black and white in this class
     * @param BELONGS_TO    the player that this piece belongs to
     * @param row           position row
     * @param col           position col
     */
    public WeiqiPiece(int TYPE, Player BELONGS_TO, int row, int col) {
        this.TYPE = TYPE;
        if (TYPE == WeiqiType.BLACK) {
            this.PIC = '●';
        } else if (TYPE == WeiqiType.WHITE) {
            this.PIC = '○';
        } else {
            this.PIC = ' ';
        }
        this.BELONGS_TO = BELONGS_TO;
        this.position = new WeiqiPosition(this, row, col);
    }

    private void setPosition(int row, int col) {
        this.position.setPosition(row, col);
    }

    @Override
    public boolean afterAction(int actionType, Position from, Position to) {
        return false;
    }

    @Override
    public void setStatus(int status) {
        if (status != 0 && status != 1 && status != -1) return;
        this.status = status;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public char getPic() {
        return this.PIC;
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
        return BELONGS_TO;
    }


}