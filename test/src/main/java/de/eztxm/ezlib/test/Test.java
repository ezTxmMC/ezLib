package de.eztxm.ezlib.test;

import de.eztxm.ezlib.config.object.JsonUtil;
import de.eztxm.ezlib.config.reflect.JsonProcessor;

public class Test {

    public static void main(String[] args) {
        JsonUtil.prettyPrint = true;
        JsonProcessor<Person> processor = JsonProcessor.loadConfiguration(Person.class);
        Person personInstance = processor.getInstance();
        // personInstance.setFirstName("Tom");
        // personInstance.setLastName("Handrick");
        // personInstance.setAge(17);
        // personInstance.setAddress(new Address(
        // 14772,
        // "Brandenburg an der Havel",
        // "Gladiolenweg",
        // "30"));
        // personInstance.setRole("Owner");
        // processor.saveConfiguration();
        System.out.println(personInstance.getAddress().getPostCode());
    }
}
