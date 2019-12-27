package P3.Chess;

import P3.Interface.Action;
import P3.Interface.Board;
import P3.Interface.Position;

public interface ChessAction extends Action {

    /**
     * 从from到to的移动是合法的
     * @param opeBoard  操作的棋盘
     * @param from      起始点
     * @param to        目标点
     * @return          如果合法返回true，否则为false
     */
    boolean canMove(Board opeBoard, Position from, Position to);

    /**
     * 为棋子判断从from到to的合法路径之间是否有其他棋子阻挡
     * @param opeBoard  操作的棋盘
     * @param from      起始点
     * @param to        目标点
     * @param moveType  执行时间代号
     * @return          如果没有阻挡，返回true，否则为false
     */
    boolean noBarrier(Board opeBoard, Position from, Position to, int moveType);

}
