package Application.TrackGame;

import abs.Person;

import java.util.Objects;

public class Athlete extends Person {

    /*
        AF
        this class represents an athlete

        RI
        true

        safety from exposure
        no mutator, all observers return immutable values
     */

    private int number;

    private String country;

    private int age;

    private double bestScoreInYear;

    /**
     * constructor
     *
     * @param name name of a person
     */
    public Athlete(String name, int number, String country, int age, double bestScoreInYear) {
        super(name);
        this.number = number;
        this.country = country;
        this.age = age;
        this.bestScoreInYear = bestScoreInYear;
    }

    public Athlete(Athlete old) {
        super(old.getName());
        this.number = old.number;
        this.age = old.age;
        this.country = old.country;
        this.bestScoreInYear = old.bestScoreInYear;
    }

    public int getNumber() {
        return number;
    }

    public String getCountry() {
        return country;
    }

    public int getAge() {
        return age;
    }

    public double getBestScoreInYear() {
        return bestScoreInYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Athlete athlete = (Athlete) o;

        if (number != athlete.number) return false;
        if (age != athlete.age) return false;
        if (Double.compare(athlete.bestScoreInYear, bestScoreInYear) != 0) return false;
        return Objects.equals(country, athlete.country);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = number;
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + age;
        temp = Double.doubleToLongBits(bestScoreInYear);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
