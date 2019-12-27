package abs;

import java.io.IOException;

public interface OrbitAble<L, E> {
    void writeBackWithPrintWriter() throws IOException;

    void writeBackWithStream() throws IOException;

    void writeBackWithBuffer() throws IOException;
}
