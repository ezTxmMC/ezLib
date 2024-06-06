package de.eztxm.serial;

import gnu.io.NRSerialPort;
import gnu.io.SerialPortEvent;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.function.Consumer;

@Getter
public class Serial {
    NRSerialPort port;
    @Setter
    private String selectedPort;
    @Setter
    private int boud;
    private DataInputStream in;
    private DataOutputStream out;

    public Serial(String port, BoudRate boudrate) {
        this.selectedPort = port;
        this.boud = boudrate.value;
        this.port = new NRSerialPort(this.selectedPort, this.boud);
    }

    public Serial(String port) {
        this.selectedPort = port;
        this.boud = BoudRate.BOUD_RATE_9600.value;
        this.port = new NRSerialPort(this.selectedPort, this.boud);
    }

    public Serial(BoudRate boudrate) {
        this.boud = boudrate.value;
        if(!availablePorts().isEmpty()) {
            this.selectedPort = availablePorts().stream().toList().get(0);
        }
        this.port = new NRSerialPort(this.selectedPort, this.boud);
    }

    public Serial() {
        this.boud = BoudRate.BOUD_RATE_9600.value;
        if(!availablePorts().isEmpty()) {
            this.selectedPort = availablePorts().stream().toList().get(0);
        }
        this.port = new NRSerialPort(this.selectedPort, this.boud);
    }

    public boolean open() {
        boolean connect = this.port.connect();
        this.in = new DataInputStream(this.port.getInputStream());
        this.out = new DataOutputStream(this.port.getOutputStream());
        return connect;
    }

    public void close() {
        if(this.port.isConnected()) {
            this.port.disconnect();
        }
    }

    public void onReceive(Consumer<String> data) {
        if(this.port == null) return;
        try {
            this.port.addEventListener(e -> {
                if(e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                    StringBuilder builder = new StringBuilder();
                    try {
                        while (this.in.available() > 0) {
                            char c = (char) this.in.read();
                            builder.append(c);
                        }
                        data.accept(builder.toString());
                    } catch (IOException ex) {
                        ex.fillInStackTrace();
                    }
                }
            });
        } catch (TooManyListenersException e) {
            e.fillInStackTrace();
        }
    }

    public void send(Packet packet) {
        try {
            if (this.port.isConnected()) {
                String pack = packet.id() + ":" + packet.data();
                this.out.write(pack.getBytes());
                System.out.println("Packet send!");
            } else {
                System.out.println("Packet not send!");
            }
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    public void send(Packet... packets) {
        for (Packet packet : packets) {
            try {
                if (this.port.isConnected()) {
                    String pack = packet.id() + ":" + packet.data();
                    this.out.write(pack.getBytes());
                }
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }
    }

    public static Set<String> availablePorts() {
        return NRSerialPort.getAvailableSerialPorts();
    }
}
