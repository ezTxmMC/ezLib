import de.eztxm.Packet;

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
