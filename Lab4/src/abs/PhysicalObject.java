package abs;

import Application.AtomStructure.Electron;
import Application.AtomStructure.Nuclear;
import Application.SocialNetworkCircle.SocialPerson;
import Application.TrackGame.Athlete;

public abstract class PhysicalObject {
    /**
     * factory to create proton
     * @param label     label of proton e.g. Cu, Fe
     * @return      a new proton object
     */
    public static Nuclear newProton(String label) {
        return new Nuclear(label);
    }

    /**
     * factory to create
     * @param label     label of electron
     * @return          a new electron
     */
    public static Electron newElectron(String label) {
        return new Electron(label);
    }

    /**
     * new athlete
     * @param name
     * @param number
     * @param country
     * @param age
     * @param bestScoreInYear
     * @return
     */
    public static Athlete newAthlete(String name, int number, String country, int age, double bestScoreInYear) {
        return new Athlete(name, number, country, age, bestScoreInYear);
    }

    /**
     *
     * @param name
     * @param age
     * @param sex
     * @return
     */
    public static SocialPerson newPerson(String name, int age, char sex) {
        return new SocialPerson(name, age, sex);
    }

    /**
     * final value: label
     */
    private final String name;

    /**
     * constructor
     * @param name     label of physical object
     */
    public PhysicalObject(String name) {
        this.name = name;
    }

    /**
     * get the label of this object
     * @return  label
     */
    public String getName() {
        return name;
    }

}
