import java.lang.reflect.Field;

public class StudentMain {

    public static void main(String[] args) {

        String[] header = {"name", "age", "city"};
        String[] data = {"Rahul", "20", "Anand"};

        Student student = new Student();

        Field[] fields = Student.class.getDeclaredFields();

        for (Field field : fields) {

            if (field.isAnnotationPresent(Column.class)) {

                Column column = field.getAnnotation(Column.class);
                String columnName = column.name();

                for (int i = 0; i < header.length; i++) {

                    if (header[i].equals(columnName)) {

                        try {
                            field.setAccessible(true);

                            if (field.getType() == int.class) {
                                field.setInt(student, Integer.parseInt(data[i]));
                            } else {
                                field.set(student, data[i]);
                            }

                        } catch (IllegalAccessException | IllegalArgumentException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                }
            }
        }

        student.display();
    }
}