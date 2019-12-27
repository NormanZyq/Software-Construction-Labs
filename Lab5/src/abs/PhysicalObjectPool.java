package abs;

import application.AtomStructure.Electron;
import application.AtomStructure.Nuclear;
import application.SocialNetworkCircle.SocialPerson;
import application.TrackGame.Athlete;

public class PhysicalObjectPool {
    /**
     * instance of this singleton class.
     */
    private static final PhysicalObjectPool INSTANCE = new PhysicalObjectPool();

    /**
     * hide the constructor from outside.
     */
    private PhysicalObjectPool() {
    }

    /**
     * get the instance.
     *
     * @return instance
     */
    public static PhysicalObjectPool getInstance() {
        return INSTANCE;
    }

    /**
     * factory to create.
     *
     * @return a new electron
     */
    public Electron electron() {
        return new Electron("");
    }

    /**
     * factory to create proton.
     *
     * @param label label of proton e.g. Cu, Fe
     * @return a new proton object
     */
    public Nuclear nuclear(final String label) {
        return new Nuclear(label);
    }

    /**
     * new athlete.
     *
     * @param name            athlete's name
     * @param number          number
     * @param country         country
     * @param age             age
     * @param bestScoreInYear best score in a year
     * @return athlete object
     */
    public Athlete athlete(final String name,
                           final int number,
                           final String country,
                           final int age,
                           final double bestScoreInYear) {
        return new Athlete(name, number, country, age, bestScoreInYear);
    }

    /**
     * new social person.
     *
     * @param name person's name
     * @param age  age
     * @param sex  sex
     * @return social person object
     */
    public SocialPerson person(final String name,
                                  final int age,
                                  final char sex) {
        return new SocialPerson(name, age, sex);
    }


}
