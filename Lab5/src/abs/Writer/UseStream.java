package abs.Writer;

import abs.OrbitAble;

import java.io.IOException;

public final class UseStream extends OrbitFileWriter {
    @Override
    public void write(OrbitAble<?, ?> orbit) throws IOException {
        orbit.writeBackWithStream();
    }
}
