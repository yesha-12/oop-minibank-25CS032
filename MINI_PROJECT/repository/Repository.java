package repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import model.annotation.Id;

public class Repository<T> {
    private final ConcurrentHashMap<String, T> items = new ConcurrentHashMap<>();

    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        String id = readId(entity);
        items.put(id, entity);
        return entity;
    }

    @SafeVarargs
    public final void saveAll(T... values) {
        for (T value : values) {
            save(value);
        }
    }

    public T findById(String id) {
        return items.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(items.values());
    }

    public boolean delete(String id) {
        return items.remove(id) != null;
    }

    public int size() {
        return items.size();
    }

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values must not be null or empty");
        }
        double total = 0;
        for (N value : values) {
            if (value == null) {
                throw new IllegalArgumentException("Values must not contain null");
            }
            total += value.doubleValue();
        }
        return total / values.length;
    }

    public void clear() {
        items.clear();
    }

    public ConcurrentHashMap<String, T> asConcurrentMap() {
        return items;
    }

    private String readId(T entity) {
        for (Class<?> type = entity.getClass(); type != null; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(entity);
                        if (value == null || value.toString().isBlank()) {
                            throw new IllegalArgumentException(
                                    "@Id field cannot be null or blank");
                        }
                        return value.toString();
                    } catch (IllegalAccessException exception) {
                        throw new IllegalStateException(
                                "Cannot read @Id field " + field.getName(), exception);
                    }
                }
            }
        }
        throw new IllegalArgumentException(
                "Entity type " + entity.getClass().getName() + " has no @Id field");
    }
}
