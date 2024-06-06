import de.eztxm.BoudRate;
import de.eztxm.Serial;

public class SerialTest {
    public static void main(String[] args) {
        System.out.println(Serial.availablePorts());
        Serial serial = new Serial("COM6", BoudRate.BOUD_RATE_9600);
        if(serial.open()) {
            System.out.println("Port Connected");
        } else {
            System.out.println("Port not correct connected!");
        }

        serial.onReceive(data -> System.out.println("[Data] -> " + data));

        serial.send(new PingPacket());
    }
}
