package de.eztxm.ezlib.config.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.eztxm.ezlib.config.annotation.JsonIgnore;
import de.eztxm.ezlib.config.annotation.JsonValue;
import de.eztxm.ezlib.config.object.JsonArray;
import de.eztxm.ezlib.config.object.JsonObject;
import de.eztxm.ezlib.config.object.JsonUtil;
import de.eztxm.ezlib.config.parse.JsonParser;

public class JsonMapper {

    public static <T> T fromJson(String json, Class<T> clazz) {
        Object parsed = new JsonParser().parse(json);
        if (!(parsed instanceof JsonObject obj)) {
            throw new IllegalArgumentException("Expected JSON object");
        }

        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(JsonIgnore.class)) {
                    continue;
                }

                field.setAccessible(true);

                String jsonKey = field.getName();
                if (field.isAnnotationPresent(JsonValue.class)) {
                    jsonKey = field.getAnnotation(JsonValue.class).name();
                }

                Object rawValue = obj.get(jsonKey);
                Object converted = convert(rawValue, field.getType());

                if (converted != null) {
                    Method setter = findSetter(clazz, field.getName(), field.getType());
                    if (setter != null) {
                        setter.invoke(instance, converted);
                    } else {
                        field.setAccessible(true);
                        field.set(instance, converted);
                    }

                }
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map JSON to " + clazz.getSimpleName(), e);
        }
    }

    public static String toJson(Object obj) {
        if (obj instanceof JsonObject || obj instanceof JsonArray<?>) {
            return JsonUtil.valueToJson(obj);
        }

        JsonObject json = new JsonObject();

        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(JsonIgnore.class)) {
                continue;
            }

            field.setAccessible(true);
            String key = field.getName();

            if (field.isAnnotationPresent(JsonValue.class)) {
                key = field.getAnnotation(JsonValue.class).name();
            }

            try {
                json.put(key, field.get(obj));
            } catch (Exception e) {
                throw new RuntimeException("Error accessing field " + field.getName(), e);
            }
        }

        return json.toJsonString();
    }

    private static Method findSetter(Class<?> clazz, String fieldName, Class<?> fieldType) {
        String methodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        try {
            return clazz.getMethod(methodName, fieldType);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static Object convert(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        if (targetType.isInstance(value)) {
            return value;
        }

        if (value instanceof Number number) {
            if (targetType == int.class || targetType == Integer.class) {
                return number.intValue();
            }
            if (targetType == long.class || targetType == Long.class) {
                return number.longValue();
            }
            if (targetType == float.class || targetType == Float.class) {
                return number.floatValue();
            }
            if (targetType == double.class || targetType == Double.class) {
                return number.doubleValue();
            }
        }

        if (value instanceof Boolean bool) {
            if (targetType == boolean.class || targetType == Boolean.class) {
                return bool;
            }
        }

        if (value instanceof String str) {
            if (targetType == String.class) {
                return str;
            }
        }

        if (value instanceof JsonObject obj) {
            if (targetType == JsonObject.class) {
                return obj;
            }

            return fromJson(obj.toJsonString(), targetType);
        }

        if (value instanceof JsonArray<?> arr) {
            if (JsonArray.class.isAssignableFrom(targetType)) {
                return arr;
            }

            if (targetType.isArray()) {
                Class<?> componentType = targetType.getComponentType();
                return convertArray(arr, componentType);
            }

            if (List.class.isAssignableFrom(targetType)) {
                if (arr.isEmpty()) {
                    return new ArrayList<>();
                }

                Object first = arr.get(0);
                Class<?> elementType = first != null ? first.getClass() : Object.class;

                return convertList(arr, elementType);
            }
        }

        if (targetType.isEnum() && value instanceof String str) {
            Object[] constants = targetType.getEnumConstants();

            for (Object constant : constants) {
                if (((Enum<?>) constant).name().equalsIgnoreCase(str)) {
                    return constant;
                }
            }
        }

        return null;
    }

    private static Object convertArray(JsonArray<?> jsonArray, Class<?> componentType) {
        int size = jsonArray.size();
        Object array = java.lang.reflect.Array.newInstance(componentType, size);

        for (int i = 0; i < size; i++) {
            Object element = convert(jsonArray.get(i), componentType);
            java.lang.reflect.Array.set(array, i, element);
        }

        return array;
    }

    private static Object convertList(JsonArray<?> jsonArray, Class<?> elementType) {
        List<Object> list = new ArrayList<>();

        for (Object item : jsonArray) {
            if (item instanceof JsonArray<?> innerArray && elementType == List.class) {
                list.add(convertList(innerArray, Object.class));
                continue;
            }

            Object converted = convert(item, elementType);
            list.add(converted);
        }

        return list;
    }

}
