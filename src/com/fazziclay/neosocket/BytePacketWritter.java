package com.fazziclay.neosocket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BytePacketWritter {
    private final OutputStream outputStream;

    public BytePacketWritter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(byte[] data) throws IOException {
        int len = data.length + 4;
        byte[] lenB = Util.intToBytes(len);

        byte[] result = new byte[data.length + 4];

        System.arraycopy(lenB, 0, result, 0, lenB.length);
        System.arraycopy(data, 0, result, 4, data.length);

        InputStream in = new ByteArrayInputStream(result);
        int count;
        byte[] buffer = new byte[1024];
        while ((count = in.read(buffer)) > 0) {
            outputStream.write(buffer, 0, count);
        }
    }

    public void flush() throws IOException {
        outputStream.flush();
    }
}
