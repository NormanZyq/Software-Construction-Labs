package abs;

public class Person extends PhysicalObject {

    /**
     * constructor.
     *
     * @param name name of a person
     */
    public Person(final String name) {
        super(name);
    }

    /**
     * use old person to copy a new one.
     *
     * @param old old person
     */
    public Person(final Person old) {
        super(old.getName());
    }

}
