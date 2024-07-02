package de.eztxm.ezlib.serial;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BoudRate {
    BOUD_RATE_4800(4800),
    BOUD_RATE_9600(9600),
    BOUD_RATE_19200(19200),
    BOUD_RATE_57600(57600),
    BOUD_RATE_115200(115200);

    final int value;
}
