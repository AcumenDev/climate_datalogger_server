package com.acumendev.climatelogger.input.tcp.dto;

public interface ResponsePacket {
    short getId();

    byte[] getBytes();
}
