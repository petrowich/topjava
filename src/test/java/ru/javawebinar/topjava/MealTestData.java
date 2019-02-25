package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID_01 = START_SEQ + 2;
    public static final int USER_MEAL_ID_02 = START_SEQ + 3;
    public static final int USER_MEAL_ID_03 = START_SEQ + 4;

    public static final int USER_MEAL_ID_11 = START_SEQ + 5;
    public static final int USER_MEAL_ID_12 = START_SEQ + 6;
    public static final int USER_MEAL_ID_13 = START_SEQ + 7;

    public static final int ADMIN_MEAL_ID_01 = START_SEQ + 8;
    public static final int ADMIN_MEAL_ID_02 = START_SEQ + 9;
    public static final int ADMIN_MEAL_ID_03 = START_SEQ + 10;

    public static final Meal USER_MEAL_01 = new Meal(USER_MEAL_ID_01, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final Meal USER_MEAL_02 = new Meal(USER_MEAL_ID_02, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
    public static final Meal USER_MEAL_03 = new Meal(USER_MEAL_ID_03, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);

    public static final Meal USER_MEAL_11 = new Meal(USER_MEAL_ID_11, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 500);
    public static final Meal USER_MEAL_12 = new Meal(USER_MEAL_ID_12, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 1000);
    public static final Meal USER_MEAL_13 = new Meal(USER_MEAL_ID_13, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);

    public static final Meal ADMIN_MEAL_01 = new Meal(ADMIN_MEAL_ID_01, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак админа", 1000);
    public static final Meal ADMIN_MEAL_02 = new Meal(ADMIN_MEAL_ID_02, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед админа", 2000);
    public static final Meal ADMIN_MEAL_03 = new Meal(ADMIN_MEAL_ID_03, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин админа", 1000);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "roles");
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "roles").isEqualTo(expected);
    }
}
