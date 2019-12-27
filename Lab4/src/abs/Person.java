package abs;

public class Person extends PhysicalObject {

    /**
     * constructor
     *
     * @param name name of a person
     */
    public Person(String name) {
        super(name);
    }

    public Person(Person old) {
        super(old.getName());
    }

}
