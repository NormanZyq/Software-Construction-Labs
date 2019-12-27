package Application.SocialNetworkCircle;

import abs.Person;

public class SocialPerson extends Person {

    private final int age;

    private final char sex;

    public SocialPerson(String name, int age, char sex) {
        super(name);
        this.age = age;
        this.sex = sex;
    }

    public SocialPerson(SocialPerson old) {
        super(old);
        this.age = old.age;
        this.sex = old.sex;
    }

    public int getAge() {
        return age;
    }

    public char getSex() {
        return sex;
    }
}
