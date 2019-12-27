package application.TrackGame;

public abstract class TrackArranger {

    /**
     * create and return a random arranger.
     *
     * @return a random arranger
     */
    public static TrackArranger randomArranger() {
        return new RandomArranger();
    }

    /**
     * create and return a faster-later arranger.
     *
     * @return faster-later arranger
     */
    public static TrackArranger fasterLaterArranger() {
        return new FasterLater();
    }

    /**
     * arrange track game.
     *
     * @param trackGame track game you want to arrange
     */
    abstract void arrange(TrackGame trackGame);

}
