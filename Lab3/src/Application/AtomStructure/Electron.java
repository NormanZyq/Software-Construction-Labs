package Application.AtomStructure;

import abs.PhysicalObject;

public class Electron extends PhysicalObject {
    private final int electricity = -1;

    public Electron(String label) {
        super(label);
    }

    public int getElectricity() {
        return electricity;
    }

}
