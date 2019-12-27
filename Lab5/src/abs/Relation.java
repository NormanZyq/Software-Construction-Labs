package abs;

/**
 * abstract relation.
 *
 * @param <S> source
 * @param <T> target
 */
public interface Relation<S, T> {

    /**
     * get source of this relation.
     *
     * @return source
     */
    S getSource();

    /**
     * get target of this relation.
     *
     * @return target
     */
    T getTarget();

    /**
     * get weight of this relation.
     *
     * @return weight
     */
    double getWeight();

}
