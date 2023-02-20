package com.fazziclay.neosocket.test;

import com.fazziclay.neosocket.packet.PacketsRegistry;

public class TestRegistry extends PacketsRegistry {
    public TestRegistry() {
        super(new Info[]{
                new Info(1, TestMessagePacket.class, TestMessagePacket.CONVERTER)
        });
    }
}
