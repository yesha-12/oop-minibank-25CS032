package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import model.annotation.MaxLength;
import model.annotation.Positive;

public final class AnnotationValidator {

    private AnnotationValidator() {
    }

    public static String[] validate(Object obj) {
        List<String> errors = new ArrayList<>();

        for (Class<?> type = obj.getClass(); type != null; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);

                try {
                    if (field.isAnnotationPresent(Positive.class)) {
                        Object value = field.get(obj);
                        if (!(value instanceof Number number) || number.doubleValue() <= 0) {
                            errors.add(field.getName() + " "
                                    + field.getAnnotation(Positive.class).message());
                        }
                    }

                    if (field.isAnnotationPresent(MaxLength.class)) {
                        Object value = field.get(obj);
                        MaxLength annotation = field.getAnnotation(MaxLength.class);
                        if (value instanceof String text && text.length() > annotation.value()) {
                            errors.add(field.getName() + " must have at most "
                                    + annotation.value() + " characters");
                        }
                    }
                } catch (IllegalAccessException exception) {
                    errors.add("Unable to validate " + field.getName());
                }
            }
        }

        return errors.toArray(new String[0]);
    }
}
