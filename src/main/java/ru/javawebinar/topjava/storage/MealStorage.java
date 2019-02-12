package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MealStorage {
    private static volatile MealStorage instance;

    private static Map<Long, Meal> mealMap = new ConcurrentHashMap<>();

    private static Long nextId = 0L;

    private MealStorage() {
        insert(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
        insert(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
        insert(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
        insert(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
        insert(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
        insert(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);
    }

    public static MealStorage getInstance() {
        MealStorage localInstance = instance;
        if (localInstance == null) {
            synchronized (MealStorage.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MealStorage();
                }
            }
        }
        return localInstance;
    }

    private static Long getNextId() {
        return ++nextId;
    }

    public void insert(LocalDateTime dateTime, String description, int calories) {
        Long id = getNextId();
        mealMap.put(id, new Meal(id, dateTime, description, calories));
    }

    public void update(Meal meal) {
        mealMap.put(meal.getId(), meal);
    }

    public void delete(Long id) {
        mealMap.remove(id);
    }

    public void delete(Meal meal) {
        mealMap.remove(meal.getId());
    }

    public Meal get(Long id) {
        return mealMap.get(id);
    }

    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }

}
