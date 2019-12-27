package abs;

/**
 * concrete relation class.
 *
 * @param <S> S means source, that is
 *            what you want to set as source
 * @param <T> T means target, that is
 *            what you want to set as target
 */
public final class ConcreteRelation<S, T> implements Relation<S, T> {
    /**
     * source.
     */
    private final S source;

    /**
     * target.
     */
    private final T target;

    /**
     * weight.
     */
    private double weight;

    /**
     * constructor.
     *
     * @param source relation source
     * @param target relation target
     * @param weight relation weight
     */
    public ConcreteRelation(final S source, final T target, final double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public S getSource() {
        return source;
    }

    @Override
    public T getTarget() {
        return target;
    }
}
