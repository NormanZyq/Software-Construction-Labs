package P2;

import com.sun.istack.internal.NotNull;

public class Person {

    private String name;

    // AF
    //  This class represents a person, each object has a name

    // RI
    //  The name string can't be null or empty

    // safety from exposure
    // there's not mutator in this class. Only a constructor can mutate an object, but the checkRep() method follows.

    // checkRep. when the name of a person is null or empty, it becomes illegal
    private void checkRep() {
        if (name == null || "".equals(name)) {
            throw new RuntimeException("姓名不可为空！");
        }
    }

    /**
     * Person 对象的构造器
     * @param name  姓名
     * when the name passed in is a null string or empty string, RuntimeException will be thrown
     */
    public Person(@NotNull String name) {
        if (name == null || "".equals(name)) {
            throw new RuntimeException("姓名不可为空！");
        }
        this.name = name;
        checkRep();
    }

    public String getName() {
        checkRep();
        return name;
    }

    @Override
    public String toString() {
        checkRep();
        return this.name;
    }
}
