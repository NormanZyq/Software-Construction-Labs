package P3.Weiqi;

import P3.Player;
import P3.Weiqi.Pieces.WeiqiPiece;

import java.util.HashSet;
import java.util.Set;

public class WeiqiPlayer extends Player {

    private final Set<WeiqiPiece> myPieces = new HashSet<>();

    /**
     * add a new piece for this player
     * @param newPiece
     * @return
     */
    public boolean addPiece(WeiqiPiece newPiece) {
        return myPieces.add(newPiece);
    }

    /**
     *
     * @param piece
     * @return
     */
    public boolean removePiece(WeiqiPiece piece) {
        return myPieces.remove(piece);
    }

    public WeiqiPlayer(String name, int TAG) {
        super(name, TAG);
    }

    @Override
    public String toString() {
        return name + "当前有" + myPieces.size() + "个棋子";
    }
}
