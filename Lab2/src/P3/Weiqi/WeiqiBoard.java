package P3.Weiqi;

import P3.Interface.Board;
import P3.Piece;
import P3.Interface.Position;
import P3.Weiqi.Pieces.WeiqiPiece;
import P3.Weiqi.Pieces.WeiqiStatus;
import P3.Weiqi.Pieces.WeiqiType;

public class WeiqiBoard implements Board {

    /*
        AF
        this class represents a weiqi board
        it has 19*19 places to put piece, and 2 players in one game
     */

    /*
        RI
        every cell on the board is not null;
        two players are not null and not same;
        current is in the players[]
     */

    /*
        safety from exposure
        will return a mutable filed's copied value
     */

    // checkRep
    private void checkRep() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == null) throw new RuntimeException("棋盘出现异常");
            }
        }
        if (!(players[0] == current || players[1] == current)) throw new RuntimeException("棋手不在参赛队员中");
        if (players[0] == players[1]) throw new RuntimeException("棋手不能相同");
    }

    private static final int SIZE = 18 + 1;

    private final WeiqiPiece[][] board = new WeiqiPiece[SIZE][SIZE];

    private final WeiqiPlayer[] players = new WeiqiPlayer[2];

    private WeiqiPlayer current;

    /**
     * constructor for WeiqiBoard.
     * to instantiate an object, we need two Player objects
     *
     * @param playerBlack   black player
     * @param playerWhite   white player
     */
    public WeiqiBoard(WeiqiPlayer playerBlack, WeiqiPlayer playerWhite) {
        // 设置本局玩家
        this.players[0] = playerBlack;
        this.players[1] = playerWhite;

        current = playerBlack;  // 初始为黑棋玩家

        // 围棋棋盘不需要特殊的初始化，只用将Blank填满就行了
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = new WeiqiPiece(WeiqiType.BLANK, null, row, col);
            }
        }

        checkRep();
    }

    /**
     * get current player(whose turn to do his operation)
     * @return  current player
     */
    public WeiqiPlayer getCurrent() {
        return current;
    }

    /**
     * 获得执黑棋的玩家
     * @return  黑棋玩家
     */
    private WeiqiPlayer getBlackPlayer() {
        return this.players[0];
    }

    /**
     * 获得执白棋的玩家
     * @return  白棋玩家
     */
    private WeiqiPlayer getWhitePlayer() {
        return this.players[1];
    }

    private void changePlayer() {
        current = current == getBlackPlayer() ? getWhitePlayer() : getBlackPlayer();
    }

    /**
     * add piece method's detail implementation inside this class
     * @param newPiece  new piece that's going to put on the board
     * @param target    where you want to put this new piece
     *
     * @return          true if adding piece successfully, false otherwise
     *              details:
     *                  only when the target position is a blank piece, this method returns true.
     *
     *       special spec:
     *          this method should only be called in the method
     *                      addPiece(WeiqiPlayer opePlayer, int row, int col)
     *          besides, every time when adding a piece must call this method once and only once
     */
    private boolean addPiece(WeiqiPiece newPiece, Position target) {
        if (target.getPiece().type() == WeiqiType.BLANK) {      // judge if the target is blank
            board[target.row()][target.col()] = newPiece;       // make target a new piece
            target.setPiece(newPiece);                          // set piece on this position
            newPiece.setPosition(target);                       // set position is the piece
            changePlayer();     // change current player
            return true;
        }
        return false;
    }

    /**
     * add piece method for outers.
     * @param opePlayer the player who is doing this operation
     * @param row       the number of the row that you want to put the piece on
     * @param col       the number of the col that you want to put the piece on
     * @return          true if adding piece successfully, false otherwise
     *              details:
     *                  only when the row and col is not out of bound and the player in argument now can put a piece,
     *                  and target position is a blank piece, this method returns true
     */
    public boolean addPiece(WeiqiPlayer opePlayer, int row, int col) {
        if (!inBound(row, col)) return false;
        Position target = this.board[row][col].getPosition();
        WeiqiPiece newPiece;
        if (opePlayer == getBlackPlayer()) {
            // black player
            newPiece = new WeiqiPiece(WeiqiType.BLACK, opePlayer, row, col);

        } else if (opePlayer == getWhitePlayer()) {
            // white player
            newPiece = new WeiqiPiece(WeiqiType.WHITE, opePlayer, row, col);

        } else return false;    // illegal player, return false

        // legal player, add the piece for the player, and do more detailed adding stuff
        boolean re = opePlayer.addPiece(newPiece) && addPiece(newPiece, target);
        checkRep();
        return re;

    }

    /**
     * judge if the row and col are in bound
     * @param row   row number
     * @param col   col number
     * @return      true only when row and col are all <= SIZE
     */
    private boolean inBound(int row, int col) {
        return row <= SIZE && col <= SIZE && row >= 0 && col >= 0;
    }

    /**
     * 提子操作
     * 玩家可以提走对面的棋子
     * @param opePlayer 执行提子操作的玩家
     * @param row       行数
     * @param col       列数
     * @return          是否提子成功，成功为true，否则是false
     */
    public boolean removePiece(WeiqiPlayer opePlayer, int row, int col) {
        if (!inBound(row, col)) return false;       // not in bound
//        Position target = this.board[row][col].getPosition();
        WeiqiPiece targetPiece = (WeiqiPiece) this.getPieceOnBoard(row, col);

        WeiqiPiece blank = WeiqiPiece.newBlankPiece(row, col);      // a new blank piece to replace the piece which is being removed
        if (opePlayer == getBlackPlayer()) {
            if (targetPiece.type() == WeiqiType.WHITE) {
                // black player can only remove white's piece
                if (getWhitePlayer().removePiece(targetPiece)) {    // remove white's piece
                    this.board[row][col] = blank;   // replace it by blank after successfully remove piece from the white player
                    targetPiece.setStatus(WeiqiStatus.GONE);
                    changePlayer();
                    checkRep();
                    return true;
                }

            }
        } else if (opePlayer == getWhitePlayer()) {
            if (targetPiece.type() == WeiqiType.BLACK) {
                // white player can only remove black's piece
                if (getBlackPlayer().removePiece(targetPiece)) {    // remove black's piece
                    this.board[row][col] = blank;   // replace it by blank after successfully remove piece from the black player
                    targetPiece.setStatus(WeiqiStatus.GONE);
                    changePlayer();
                    checkRep();
                    return true;
                }
            }

        }
        checkRep();
        return false;
    }

    public void giveUp() {
        changePlayer();
    }

    @Override
    public Position getPositionByPiece(Piece piece) {
        return piece.getPosition();
    }

    @Override
    public Piece getPieceOnBoard(int row, int col) {
        return board[row][col];
    }

    @Override
    public Piece getPieceOnBoard(Position position) {
        return getPieceOnBoard(position.row(), position.col());
    }

    @Override
    public boolean movePieceOnBoard(Piece ope, Position from, Position to) {
        return false;
    }

    @Override
    public String queryCapture(Position position) {
        Piece piece = getPieceOnBoard(position);
        if (piece.type() == WeiqiType.BLANK) {
            return "查询位置未被占用";
        } else if (piece.type() == WeiqiType.BLACK) {
            return "查询位置被黑方玩家" + piece.belongsTo().getName() + "占用";
        } else if (piece.type() == WeiqiType.WHITE) {
            return "查询位置被白方玩家" + piece.belongsTo().getName() + "占用";
        } else throw new RuntimeException("访问位置非法");
    }

    @Override
    public void printBoard() {
        System.out.println(this.toString());
    }

    @Override
    public boolean isLegalTargetPosition(Position target) {
        int row = target.row();
        int col = target.col();
        return inBound(row, col);
    }

    /**
     * Overridden toString method,
     * to parse the board to a string and simulate the real Weiqi board
     * @return  board string, and can be printed directly then is a board pic.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                sb.append(appendWhat(row, col));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private String appendWhat(int row, int col) {
        if (row == 0) {
            // 第一行
            if (col == 0) {
                // 第一列
                if (board[row][col].type() == WeiqiType.BLANK) {
                    // 空白棋子加┏┳┓ ━
                    return "┏━";
                } else {
                    return board[row][col].getPic() + "━";
                }
            } else if (col == SIZE - 1) {
                // 最后一列
                if (board[row][col].type() == WeiqiType.BLANK) {
                    return "┓";
                } else {
                    return board[row][col].getPic() + "┓";
                }
            } else {
                // 中间列
                if (board[row][col].type() == WeiqiType.BLANK) {
                    return "┳━";
                } else {
                    return board[row][col].getPic() + "━";
                }
            }

        } else if (row == SIZE - 1) {
            // 最后一行 ┗┻┛ ━
            if (col == 0) {
                // 第一列
                if (board[row][col].type() == WeiqiType.BLANK) {
                    // 空白棋子加┣
                    return "┗━";
                } else {
                    return board[row][col].getPic() + "━";
                }
            } else if (col == SIZE - 1) {
                // 最后一列
                if (board[row][col].type() == WeiqiType.BLANK) {
                    // 空白棋子加
                    return "┛";
                } else {
                    return String.valueOf(board[row][col].getPic());
                }
            } else {
                // 中间列
                if (board[row][col].type() == WeiqiType.BLANK) {
                    return "┻━";
                } else {
                    return board[row][col].getPic() + "━";
                }
            }

        } else {
            // 中间行
            if (col == 0) {
                // 第一列
                if (board[row][col].type() == WeiqiType.BLANK) {
                    // 空白棋子加┣
                    return "┣━";
                } else {
                    return board[row][col].getPic() + "━";
                }
            } else if (col == SIZE - 1) {
                // 最后一列
                if (board[row][col].type() == WeiqiType.BLANK) {
                    // 空白棋子加
                    return "┫";
                } else {
                    return board[row][col].getPic() + "━";
                }
            } else {
                // 中间列
                if (board[row][col].type() == WeiqiType.BLANK) {
                    // 空白棋子加┏┳┓
                    return "╋━";
                } else {
                    return board[row][col].getPic() + "━";
                }
            }
        }
    }
}
