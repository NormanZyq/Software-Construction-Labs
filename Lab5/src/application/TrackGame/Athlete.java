package application.TrackGame;

import abs.Person;

import java.util.Objects;

public final class Athlete extends Person {

    /*
        AF
        this class represents an athlete

        RI
        true

        safety from exposure
        no mutator, all observers return immutable values
     */

    /**
     * number.
     */
    private int number;

    /**
     * country.
     */
    private String country;

    /**
     * age.
     */
    private int age;

    /**
     * best score in a year.
     */
    private double bestScoreInYear;

    /**
     * constructor.
     *
     * @param name            name of a person
     * @param number          number
     * @param country         country
     * @param age             age
     * @param bestScoreInYear best score
     */
    public Athlete(final String name,
                   final int number,
                   final String country,
                   final int age,
                   final double bestScoreInYear) {
        super(name);
        this.number = number;
        this.country = country;
        this.age = age;
        this.bestScoreInYear = bestScoreInYear;
    }

    /**
     * copy an athlete object from old.
     *
     * @param old old athlete
     */
    public Athlete(final Athlete old) {
        super(old.getName());
        this.number = old.number;
        this.age = old.age;
        this.country = old.country;
        this.bestScoreInYear = old.bestScoreInYear;
    }

    /**
     * get number.
     *
     * @return number
     */
    public int getNumber() {
        return number;
    }

    /**
     * get country.
     *
     * @return country
     */
    public String getCountry() {
        return country;
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
     * get best score in a year.
     *
     * @return best score in a year
     */
    public double getBestScoreInYear() {
        return bestScoreInYear;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Athlete athlete = (Athlete) o;

        if (number != athlete.number) {
            return false;
        }
        if (age != athlete.age) {
            return false;
        }
        if (Double.compare(athlete.bestScoreInYear, bestScoreInYear) != 0) {
            return false;
        }
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
