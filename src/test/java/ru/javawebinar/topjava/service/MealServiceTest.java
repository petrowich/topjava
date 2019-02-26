package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() throws Exception {
        Meal newMeal = new Meal(null, LocalDateTime.of(2015, Month.MAY, 29, 10, 0), "Обед", 1000);
        service.create(newMeal, USER_ID);
        assertMatch(service.getAll(USER_ID)
                , USER_MEAL_13, USER_MEAL_12, USER_MEAL_11
                , USER_MEAL_03, USER_MEAL_02, USER_MEAL_01
                , newMeal);
    }

    @Test(expected = org.springframework.dao.DuplicateKeyException.class)
    public void createSameUserDateTime() throws Exception {
        Meal newMeal = new Meal(null, USER_MEAL_01.getDateTime(), "Обед", 1000);
        service.create(newMeal, USER_ID);
    }

    @Test
    public void get() throws Exception {
        Meal meal = service.get(USER_MEAL_ID_11, USER_ID);
        assertMatch(meal, USER_MEAL_11);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(USER_MEAL_ID_11, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(service.getAll(USER_ID)
                , USER_MEAL_13, USER_MEAL_12, USER_MEAL_11
                , USER_MEAL_03, USER_MEAL_02, USER_MEAL_01);
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        assertMatch(service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 30, 13, 0)
                , LocalDateTime.of(2015, Month.MAY, 31, 10, 0), USER_ID)
                , USER_MEAL_11, USER_MEAL_03, USER_MEAL_02);
    }

    @Test
    public void getBetweenDates() throws Exception {
        assertMatch(service.getBetweenDates(LocalDate.of(2015, Month.MAY, 30)
                , LocalDate.of(2015, Month.MAY, 31), USER_ID)
                , USER_MEAL_13, USER_MEAL_12, USER_MEAL_11
                , USER_MEAL_03, USER_MEAL_02, USER_MEAL_01);
    }

    @Test
    public void delete() throws Exception {
        service.delete(ADMIN_MEAL_ID_02, ADMIN_ID);
        List<Meal> meals = service.getAll(ADMIN_ID);
        assertMatch(meals, ADMIN_MEAL_03, ADMIN_MEAL_01);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        service.delete(ADMIN_MEAL_ID_02, USER_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updatedMeal = new Meal(USER_MEAL_01);
        updatedMeal.setDateTime(LocalDateTime.now());
        updatedMeal.setDescription("Ланч");
        updatedMeal.setCalories(666);
        service.update(updatedMeal, USER_ID);
        Meal currentMeal = service.get(updatedMeal.getId(), USER_ID);
        assertMatch(currentMeal, updatedMeal);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        service.update(new Meal(USER_MEAL_01), ADMIN_ID);
    }

}