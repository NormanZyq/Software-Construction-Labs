package abs.Writer;

import abs.OrbitAble;

import java.io.IOException;

public final class UseBuffer extends OrbitFileWriter {
    @Override
    public void write(OrbitAble<?, ?> orbit) throws IOException {
        orbit.writeBackWithBuffer();
    }
}
