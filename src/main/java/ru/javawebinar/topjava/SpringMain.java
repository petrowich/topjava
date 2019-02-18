package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.create(new Meal(null, 1, LocalDateTime.now(), "еда юзера 1", 500));
            mealRestController.create(new Meal(null, 2, LocalDateTime.now(), "еда юзера 2", 500)); //недолжна сохаранится


            try {
                mealRestController.create(new Meal(10, 1, LocalDateTime.now(), "еда юзера 2", 500)); //не null Id
            } catch (Exception e) {
                e.printStackTrace();
            }

            mealRestController.delete(5);

            try {
                mealRestController.delete(8);  //несуществующий Id
            } catch (Exception e) {
                e.printStackTrace();
            }

            mealRestController.update(new Meal(4, 1, LocalDateTime.now(), "Завтрааааааааак", 555), 4);

            try {
                mealRestController.update(new Meal(3, 1, LocalDateTime.now(), "Ужииииииииин", 666), 2); //не совпадают Id
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                mealRestController.update(new Meal(3, 2, LocalDateTime.now(), "Ужии222222иин", 666), 3); //чужой userId
            } catch (Exception e) {
                e.printStackTrace();
            }

            mealRestController.update(new Meal(3, 1, LocalDateTime.now(), "Ужии1111111иин", 666), 3);

            LocalDate startDate = LocalDate.of(2015, Month.MAY, 31); //всё с 31.05.2015
            LocalDate endDate = null;
            mealRestController.getAll(startDate, endDate).stream().forEach(System.out::println);
        }
    }
}
