package abs.Writer;

import abs.OrbitAble;

import java.io.IOException;

public final class UsePrintWriter extends OrbitFileWriter {

    @Override
    public void write(OrbitAble<?, ?> orbit) throws IOException {
        orbit.writeBackWithPrintWriter();
    }
}
