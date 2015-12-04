package com.smilehacker.coffeeknife.common.string;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Created by kleist on 14/10/20.
 */
public class ToStringBuilder {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ToString {
        String key();
    }

    public static String build(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append(object.getClass().getSimpleName()).append('{');

        boolean firstRound = true;

        for (Field field : fields) {
            if (!firstRound) {
                sb.append(", ");
            }
            firstRound = false;
            field.setAccessible(true);
            try {
                final Object fieldObj = field.get(object);
                final String value;
                if (null == fieldObj) {
                    value = "null";
                } else {
                    value = fieldObj.toString();
                }
                sb.append(field.getName()).append('=')
                        .append(value);
            } catch (IllegalAccessException ignore) {
                //this should never happen
            }

        }

        sb.append('}');
        return sb.toString();
    }

    public static String buildWithAnnotation(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append(object.getClass().getSimpleName()).append('{');

        boolean isFirst = true;
        for (Field field : fields) {
            String key;

            if (!isFirst) {
                sb.append(", ");
            } else {
                isFirst = false;
            }

            if (!field.isAnnotationPresent(ToString.class)) {
                continue;
            }

            key = getFiledKeyName(field);

            try {
                final Object fieldObj = field.get(object);
                final String value;
                if (null == fieldObj) {
                    value = "null";
                } else {
                    value = fieldObj.toString();
                }
                if (key != null && key != "") {
                    sb.append(field.getName()).append('=')
                            .append(value);
                } else {
                    sb.append(field.getName()).append('=')
                            .append(value);

                }
            } catch (IllegalAccessException ignore) {
                //this should never happen
            }
        }

        sb.append('}');

        return sb.toString();
    }

    private static String getFiledKeyName(Field field) {
        ToString toString = (ToString) field.getAnnotation(ToString.class);
        return toString.key();
    }
}
