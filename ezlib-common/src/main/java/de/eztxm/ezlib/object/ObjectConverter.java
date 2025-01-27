package de.eztxm.ezlib.object;

import java.util.Collections;
import java.util.List;

public class ObjectConverter {
    private final Object object;

    public ObjectConverter(Object object) {
        this.object = object;
    }

    public Object asObject() {
        try {
            return object;
        } catch (Exception e) {
            return null;
        }
    }

    public String asString() {
        try {
            return object.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean asBoolean() {
        try {
            return (boolean) object;
        } catch (Exception e) {
            return false;
        }
    }

    public int asInteger() {
        try {
            return (int) object;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public double asDouble() {
        try {
            return (double) object;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public float asFloat() {
        try {
            return (float) object;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public List<Object> asList() {
        try {
            return Collections.singletonList(object);
        } catch (Exception e) {
            return null;
        }
    }
}
