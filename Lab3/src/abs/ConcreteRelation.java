package abs;

import abs.Relation;

public class ConcreteRelation<S, T> implements Relation<S, T> {
    private final S SOURCE;

    private final T TARGET;

    private double weight;

    public ConcreteRelation(S source, T target, double weight) {
        this.SOURCE = source;
        this.TARGET = target;
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public S getSource() {
        return SOURCE;
    }

    @Override
    public T getTarget() {
        return TARGET;
    }
}
