package P3;

import P3.Interface.Action;
import P3.Interface.Position;
import P3.Player;

public abstract class Piece implements Action {



    public static final int NOT_SET = -1;

    protected int status = NOT_SET;

    /**
     * 设置status，status是用来标记棋子状态的变量，使用时必须使用Piece和其子类，
     * 或者xxxStatus中的常量来设置
     *
     * @param status    新的status
     */
    public abstract void setStatus(int status);

    /**
     * 返回棋子的状态代号
     * @return  棋子状态代号
     */
    public abstract int getStatus();

    /**
     * 获得棋子的"图片"
     * @return  棋子图片或符号
     */
    public abstract char getPic();

    public abstract void setPosition(Position newPosition);

    public abstract Position getPosition();

    public abstract int type();

    /**
     * 返回这个这个棋子归属的玩家
     * @return  这个棋子归属的玩家
     */
    public abstract Player belongsTo();



}
