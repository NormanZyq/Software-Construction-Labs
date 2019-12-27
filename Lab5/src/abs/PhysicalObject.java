package abs;

import application.AtomStructure.Electron;
import application.AtomStructure.Nuclear;
import application.SocialNetworkCircle.SocialPerson;
import application.TrackGame.Athlete;

public abstract class PhysicalObject {
    /**
     * final value: label.
     */
    private final String name;

    /**
     * constructor.
     *
     * @param name label of physical object
     */
    public PhysicalObject(final String name) {
        this.name = name;
    }

    /**
     * factory to create proton.
     *
     * @param label label of proton e.g. Cu, Fe
     * @return a new proton object
     */
    public static Nuclear newProton(final String label) {
        return new Nuclear(label);
    }

    /**
     * factory to create.
     *
     * @param label label of electron
     * @return a new electron
     */
    public static Electron newElectron(final String label) {
        return new Electron(label);
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
    public static Athlete newAthlete(final String name,
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
    public static SocialPerson newPerson(final String name,
                                         final int age,
                                         final char sex) {
        return new SocialPerson(name, age, sex);
    }

    /**
     * get the label of this object.
     *
     * @return label
     */
    public String getName() {
        return name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
