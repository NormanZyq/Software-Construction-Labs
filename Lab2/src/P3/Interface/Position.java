package P3.Interface;

import P3.Piece;

public interface Position {

    /**
     * 将棋子从from移动到to会发生什么
     *
     * @param moveWhat 移动的棋子
     * @param from     起始点
     * @param to       目标点
     * @return 返回的内容是xxxAction中的常量，根据判断结果返回具体的数字
     */
    int whatWillHappen(Piece moveWhat, Position from, Position to);

//    void makeUsed();

//    void makeUnused();

    /**
     * 获得当前位置的棋子
     *
     * @return 当前位置的棋子
     */
    Piece getPiece();

    /**
     * 返回当前位置的行数
     *
     * @return 行数
     */
    int row();

    /**
     * 返回当前位置的列数
     *
     * @return 列数
     */
    int col();

    /**
     * 修改当前位置的棋子，子类实现时必须判断类型是否是自己需要的
     *
     * @param newPiece 新的棋子
     * @return 设置成功返回true，否则为false
     */
    boolean setPiece(Piece newPiece);

    void setPosition(int row, int col);


}