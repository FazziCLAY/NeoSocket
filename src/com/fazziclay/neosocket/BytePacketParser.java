package com.fazziclay.neosocket;

import java.io.IOException;
import java.io.InputStream;

/**
 * @see #parse()
 */
public class BytePacketParser {
    private final InputStream inputStream;
    private int state = 0; // 0 - wait packet length; 1 - wait data by packet length
    private int len = -1; // if state == 0: -1 else: length of packet

    public BytePacketParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Parse first data
     * <br>00 00 00 00 aa aa aa aa
     * <br>| First 4 bytes: length (lengthOfData + 4)
     * <br>| Other bytes (length - 4): data
     * @return parsed data
     * @throws IOException default socket exception
     * @throws RuntimeException what situations/parser bug/loss data end etc...
     */
    public byte[] parse() throws IOException {
        if (state == 0) {
            if (inputStream.available() >= 4) {
                byte[] buff = new byte[4];
                int readed = inputStream.read(buff, 0, 4);
                if (readed != 4) {
                    throw new RuntimeException("WTF: read not 4 bytes: " + readed);
                }
                len = Util.bytesToInt(buff);
                if (len < 4) {
                    throw new RuntimeException("WTF: Packet len < 4");
                }
                state = 1;
            }
        } else if (state == 1) {
            if (len == 4) {
                state = 0;
                len = -1;
                return new byte[0];
            } else {
                if (inputStream.available() >= (len-4)) {
                    byte[] buff = new byte[len-4];
                    int readed = inputStream.read(buff, 0, len-4);
                    if (readed != len-4) {
                        throw new RuntimeException("WTF: read not (len-4) bytes: " + readed);
                    }
                    state = 0;
                    len = -1;
                    return buff;
                }
            }
        }
        return null;
    }
}
