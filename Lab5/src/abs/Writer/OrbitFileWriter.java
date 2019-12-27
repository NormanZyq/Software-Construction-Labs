package abs.Writer;

import abs.OrbitAble;

import java.io.IOException;

public abstract class OrbitFileWriter {

    public static void writeBack(OrbitAble<?, ?> orbit, OrbitFileWriter writer) throws IOException {
        writer.write(orbit);
    }

    public abstract void write(OrbitAble<?, ?> orbit) throws IOException;


}
