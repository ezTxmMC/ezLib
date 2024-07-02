package de.eztxm.ezlib.serial;

public class PingPacket extends Packet {
    @Override
    public String id() {
        return "ping";
    }

    @Override
    public String data() {
        return "Hallo !";
    }
}
