package P3;

import java.util.HashMap;
import java.util.Map;

public class Person {

    //    private static final ArrayList<String> ALL_PEOPLE_NAMES = new ArrayList<>();
    public static final Map<String, Boolean> ALL_PEOPLE_NAMES = new HashMap<>();

    private String name;

    /**
     * Person 对象的构造器，每次会先检查allPeopleNames列表，如果已经存在这个名字，就抛出异常
     * @param name  姓名
     */
    public Person(String name) {
        if (ALL_PEOPLE_NAMES.get(name) != null && ALL_PEOPLE_NAMES.get(name)) {
            throw new RuntimeException("叫做" + name + "的人已经存在");
        }
        this.name = name;
        ALL_PEOPLE_NAMES.put(name, true);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
