package P3.Interface;

public interface Action {

    /**
     * 移动棋子之后需要执行的方法
     * @param actionType    操作类型
     * @param from          起始点
     * @param to            目标点
     * @return              返回true
     */
    boolean afterAction(int actionType, Position from, Position to);

}
