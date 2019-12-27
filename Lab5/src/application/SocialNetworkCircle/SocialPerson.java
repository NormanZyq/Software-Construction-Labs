package application.SocialNetworkCircle;

import abs.Person;

public final class SocialPerson extends Person {
    /**
     * age.
     */
    private final int age;

    /**
     * sex.
     */
    private final char sex;

    /**
     * constructor with simple data.
     *
     * @param name name
     * @param age  age
     * @param sex  sex
     */
    public SocialPerson(final String name, final int age, final char sex) {
        super(name);
        this.age = age;
        this.sex = sex;
    }

    /**
     * copy a person from old.
     *
     * @param old old
     */
    public SocialPerson(final SocialPerson old) {
        super(old);
        this.age = old.age;
        this.sex = old.sex;
    }

    /**
     * get age.
     *
     * @return age
     */
    public int getAge() {
        return age;
    }

    /**
     * get sex.
     *
     * @return sex
     */
    public char getSex() {
        return sex;
    }
}
