package abs;

/**
 *
 * @param <S>   source
 * @param <T>   target
 */
public interface Relation<S, T> {

    S getSource();

    T getTarget();

    double getWeight();

}
