import java.lang.reflect.Field;
import java.util.ArrayList;

class FormValidator {

    public static String[] validate(Object obj) {

        ArrayList<String> errors = new ArrayList<>();

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {

            field.setAccessible(true);

            try {
                Object value = field.get(obj);

                if (field.isAnnotationPresent(NotBlank.class)) {

                    if (value == null || value.toString().trim().isEmpty()) {
                        errors.add(field.getName() + " must not be blank");
                    }
                }

                if (field.isAnnotationPresent(MaxLength.class)) {

                    MaxLength annotation = field.getAnnotation(MaxLength.class);

                    if (value != null && value.toString().length() > annotation.value()) {
                        errors.add(field.getName() + " is too long");
                    }
                }

            } catch (IllegalAccessException | IllegalArgumentException e) {
                errors.add("Error checking " + field.getName());
            }
        }

        return errors.toArray(String[]::new);
    }
}