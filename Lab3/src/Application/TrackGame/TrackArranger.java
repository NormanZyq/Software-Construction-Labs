package Application.TrackGame;

public abstract class TrackArranger {

    public static TrackArranger randomArranger() {
        return new RandomArranger();
    }

    public static TrackArranger fasterLaterArranger() {
        return new FasterLater();
    }

    abstract void arrange(TrackGame trackGame);

}
