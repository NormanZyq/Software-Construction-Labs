package application.AtomStructure;

import abs.PhysicalObject;

public class Electron extends PhysicalObject implements Cloneable {
    /**
     * constructor of electron object.
     *
     * @param label label of this electron
     */
    public Electron(final String label) {
        super(label);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
