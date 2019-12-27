package P3.Chess;

import P3.Chess.Pieces.*;
import P3.Interface.Board;
import P3.Piece;
import P3.Interface.Position;

import java.util.HashMap;
import java.util.Map;

public class ChessBoard implements Board {

    // AF
    //  This class represents a chess board

    // RI
    // every cell on the board is not null;
    // two players are not null and not same;
    // current is in the players[]

    // safety from exposure
    // will return a mutable filed's copied value

    public static final int SIZE = 8;

    private final Piece[][] board = new Piece[SIZE][SIZE];    // 以size常量为大小初始化棋盘

    private Map<Piece, Position> piecePositionMap = new HashMap<>();

    private final ChessPlayer[] players = new ChessPlayer[2];

    private ChessPlayer current;

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

    /**
     * 根据国际象棋初始棋盘内容来初始化一个棋盘
     * 同时设置本局玩家
     * @param playerA   执黑棋玩家
     * @param playerB   执白棋玩家
     */
    public ChessBoard(ChessPlayer playerA, ChessPlayer playerB) {
        // 设置本局玩家
        this.players[0] = playerA;
        this.players[1] = playerB;

        current = playerA;

        // 初始化棋盘
        for (int col = 0; col < SIZE; col++) {
            // 初始化黑白兵
            Pawn pB = new Pawn(playerB, 1, col);
            Pawn pA = new Pawn(playerA, 6, col);

//            piecePositionMap.put(pB, new ChessPosition(1, col));
//            piecePositionMap.put(pA, new ChessPosition(6, col));

            playerB.appendPiece(pB);
            playerA.appendPiece(pA);
            board[1][col] = pB;   // 白
            board[6][col] = pA;   // 黑

            // 初始化其他
            switch (col) {
                case 0:
                case 7:
                    // 车
                    Rook rookB = new Rook(playerB, 0, col);
                    Rook rookA = new Rook(playerA, 7, col);

//                    piecePositionMap.put(rookB, new ChessPosition(0, col));
//                    piecePositionMap.put(rookA, new ChessPosition(7, col));

                    playerB.appendPiece(rookB);
                    playerA.appendPiece(rookA);
                    board[0][col] = rookB;
                    board[7][col] = rookA;
                    break;
                case 1:
                case 6:
                    Knight kB = new Knight(playerB, 0, col);
                    Knight kA = new Knight(playerA, 7, col);

//                    piecePositionMap.put(kB, new ChessPosition(0, col));
//                    piecePositionMap.put(kA, new ChessPosition(7, col));

                    playerB.appendPiece(kB);
                    playerA.appendPiece(kA);
                    board[0][col] = kB;
                    board[7][col] = kA;
                    break;
                case 2:
                case 5:
                    Bishop bB = new Bishop(playerB, 0, col);
                    Bishop bA = new Bishop(playerA, 7, col);

//                    piecePositionMap.put(bB, new ChessPosition(0, col));
//                    piecePositionMap.put(bA, new ChessPosition(7, col));

                    playerB.appendPiece(bB);
                    playerA.appendPiece(bA);
                    board[0][col] = bB;
                    board[7][col] = bA;
                    break;
                case 3:
                    King kingB = new King(playerB, 0, col);
                    King kingA = new King(playerA, 7, col);

//                    piecePositionMap.put(kingB, new ChessPosition(0, col));
//                    piecePositionMap.put(kingA, new ChessPosition(7, col));

                    playerB.appendPiece(kingB);
                    playerA.appendPiece(kingA);
                    board[0][col] = kingB;
                    board[7][col] = kingA;
                    break;
                case 4:
                    Queen qB = new Queen(playerB, 0, col);
                    Queen qA = new Queen(playerA, 7, col);

//                    piecePositionMap.put(qB, new ChessPosition(0, col));
//                    piecePositionMap.put(qA, new ChessPosition(7, col));

                    playerB.appendPiece(qB);
                    playerA.appendPiece(qA);
                    board[0][col] = qB;
                    board[7][col] = qA;
                    break;
            }

            // 初始化空白
            for (int row = 2; row <= 5; row++) {
                for (int col1111111 = 0; col1111111 < SIZE; col1111111++) {
                    board[row][col1111111] = new Blank(row, col1111111);
                }
            }
        }

        checkRep();
    }

    /**
     * 交换出手
     */
    private void exchangePlayer() {
        if (current == players[0]) current = players[1];
        else current = players[0];
    }

    /**
     * 在棋盘上移动棋子
     * @param opePiece  移动的棋子
     * @param another   目标棋子
     */
    private void moveOpePieceToPosition(Piece opePiece, Piece another) {
        int row = opePiece.getPosition().row();
        int col = opePiece.getPosition().col();

        // 目标点
        Position toPosition = another.getPosition();
        board[toPosition.row()][toPosition.col()] = opePiece;       // 移动棋子到新的位置

        if (another.type() == ChessType.BLANK) {
            // 目标是空白棋子，直接移动

            opePiece.setPosition(toPosition);       // 设置棋子自己的位置属性

        } else {
            // 目标是敌方棋子，吃掉
            ((ChessPlayer) another.belongsTo()).removePiece(another);   // 从归属者那里移除
            another.setStatus(ChessStatus.DEAD);    // 标记为死亡
        }

        board[row][col] = new Blank(row, col);  // 旧的位置创建一个新的blank棋子

        opePiece.afterAction(-1, opePiece.getPosition(), another.getPosition());

    }

    public ChessPlayer getCurrentPlayer() {
        return current;
    }

    @Override
    public Position getPositionByPiece(Piece piece) {
        return piece.getPosition();
    }

    @Override
    public Piece getPieceOnBoard(int row, int col) {
        if (row >= SIZE || col >= SIZE) throw new RuntimeException("访问棋盘大小超出边界");
        return board[row][col];
    }

    @Override
    public Piece getPieceOnBoard(Position position) {
        return position.getPiece();
    }

    /**
     * 改变棋子的位置
     * 首先经过move()方法检查合法性，然后移动棋子，移动棋子时分为如下情况
     * 1. 目标位置为空即blank棋子所在位置，此时将这两个棋子互换位置
     * 2. 目标位置有敌方棋子，则吃掉，对方棋子被remove，然后新建一个blank棋子到该位置，然后互换这两个棋子的位置
     * @param ope       操作的棋子
     * @param from      起始位置
     * @param to        目标位置
     * @return          移动成功或者失败
     */
    @Override
    public boolean movePieceOnBoard(Piece ope, Position from, Position to) {
//        ChessPiece o = (ChessPiece)ope;
        // 检查from和to的情况
        if (from.getPiece().belongsTo() == current) {
            // 想移动的棋子属于自己
            // 检查出发点和目标点之间是否有障碍
            if (((ChessPiece) ope).canMove(this, from, to)) {
                // 没有障碍
                // 检查目标位置的情况，只要目标不是自己就能移动，可能是移动到空白，也可能是吃子
                if (to.getPiece().belongsTo() != current) {
                    moveOpePieceToPosition(from.getPiece(), to.getPiece());   // 移动位置
                } else return false;

                // 因为没有障碍，所以经过目标点的判断之后仍然没有不合法的情况，都能执行到这里
                exchangePlayer();
                return true;
            }


        }

        return false;

    }

    @Override
    public String queryCapture(Position position) {
        ChessPiece piece = (ChessPiece) position.getPiece();

        switch (piece.type()) {
            case ChessType.BLANK:
                return "查询位置未被占用";
            case ChessType.BISHOP:
            case ChessType.KING:
            case ChessType.KNIGHT:
            case ChessType.PAWN:
            case ChessType.QUEEN:
            case ChessType.ROCK:
                return "(" + position.row() + "," + position.col() + ")" + "当前被" + piece.belongsTo().getName() + "的 \"" + piece.getPic() + "\" 棋子占用";
            default:
                return "查询错误";
        }


    }

    /**
     * 打印棋盘和棋子内容
     */
    @Override
    public void printBoard() {
        System.out.println(this.toString());
    }

    @Override
    public boolean isLegalTargetPosition(Position target) {
        int row = target.row();
        int col = target.col();
        return row < ChessBoard.SIZE && row >= 0 && col >= 0 && col < ChessBoard.SIZE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // 最上横杠
//        sb.append("-----------------------\n");
        sb.append(" ━━━━━━━━━━━━━━━━━━━━━━\n");
        for (int row = SIZE - 1; row >= 0; row--) {
            sb.append(row).append("┃");  // 打印左边框
            // 中间字符和末尾边框
            for (int col = 0; col < SIZE; col++) {
                Piece currentPiece = board[row][col];
                if (currentPiece.belongsTo() == players[0] || currentPiece.belongsTo() == null) {
                    sb.append(currentPiece.getPic());
                } else {
                    sb.append("\033[31;4m").append(currentPiece.getPic()).append("\033[0m");
                }
                sb.append('┃');
            }
            // 换行符
            sb.append("\n");
            // 分隔行
            sb.append(" ━━━━━━━━━━━━━━━━━━━━━━\n");
        }
        // 列序号
        sb.append("  ");
        for (int col = 0; col < SIZE; col++) {
            sb.append(col).append((char)12288);
        }

        return sb.toString();
    }
}
