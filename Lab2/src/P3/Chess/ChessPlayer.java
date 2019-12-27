package P3.Chess;

import P3.Piece;
import P3.Player;

import java.util.ArrayList;
import java.util.List;

public class ChessPlayer extends Player {

    private final List<Piece> myPieces = new ArrayList<>();

    public ChessPlayer(String name, int TAG) {
        super(name, TAG);
    }

    /**
     * 判断是否包含棋子p
     * @param p     需要判断的棋子
     * @return      如果myPieces列表中包含这个棋子就返回true，否则为false
     */
    public boolean hasPiece(Piece p) {
        return myPieces.contains(p);
    }

    /**
     * 往myPieces列表中增加棋子
     * @param toAppend  需要增加的棋子
     */
    public void appendPiece(Piece toAppend) {
        myPieces.add(toAppend);
    }

    /**
     * 往myPieces列表中减少棋子
     * @param toRemove  需要删掉的棋子
     */
    public void removePiece(Piece toRemove) {
        myPieces.remove(toRemove);
    }

    @Override
    public String toString() {
        return name + "当前有" + myPieces.size() + "个棋子";
    }
}
