package P3;

public class Player {

    /*
        AF
        this class represents a play in every game
     */

    /*
        RI
        true
     */

    protected String name;

    public final int TAG;

    protected Player(String name, int TAG) {
        this.name = name;
        this.TAG = TAG;
    }

    public String getName() {
        return name;
    }

    public Player(int TAG) {
        this.TAG = TAG;
    }


}
