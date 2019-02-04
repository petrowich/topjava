package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExceed> userMealsWithExceed = new ArrayList<>();   //лист с результатом
        Map<LocalDate, Integer> dailyCalories = new HashMap();              //меп для хранения суммы каллорий за каждый день из переданного списка

        for (UserMeal userMeal : mealList) {                                //для каждого userMeal
            LocalDate userMealDate = userMeal.getDateTime().toLocalDate();  //заранее сохраняем дату, чтобы строку ниже сделать короче и не вызывать toLocalDate() два раза
            dailyCalories.put(userMealDate, userMeal.getCalories() + dailyCalories.getOrDefault(userMealDate, 0)); //сохраняем в меп dailyCalories сумму каллорий за каждый день, используем getOrDefault чтобы сократить код
        }

        //к сожалению, TimeUtil.isBetween(...), isBefore() и isAfter() не подходят для проверки условия по времени,
        //т.к. нам нужно включать в выборку userMeal с dateTime равным startTime и/или endTime
        //поэтому будем сравнивать по количеству секунд от начала дня
        int startTimeSeconds = startTime.toSecondOfDay();   //заранее сохраняем секунды фильтра
        int endTimeSeconds = endTime.toSecondOfDay();       //чтобы сократить код ниже

        for (UserMeal userMeal : mealList) {                                //заполняем userMealsWithExceed из mealList
            int userMealTimeSeconds = userMeal.getDateTime().toLocalTime().toSecondOfDay(); //заранее сохраняем секунды userMeal, чтобы сократить код ниже и не вызывать toLocalTime().toSecondOfDay() два раза
            if (startTimeSeconds <= userMealTimeSeconds && endTimeSeconds >= userMealTimeSeconds) {
                userMealsWithExceed.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), dailyCalories.getOrDefault(userMeal.getDateTime().toLocalDate(), 0) > caloriesPerDay));
            }
        }

        return userMealsWithExceed;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededStreamAPI(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        //меп для хранения суммы каллорий за каждый день из переданного списка
        Map<LocalDate, Integer> dailyCalories = mealList.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));

        //сразу результат
        return mealList.stream()
                .filter(userMeal -> (userMeal.getDateTime().toLocalTime().toSecondOfDay() >= startTime.toSecondOfDay() && userMeal.getDateTime().toLocalTime().toSecondOfDay() <= endTime.toSecondOfDay()))
                .map(userMeal -> new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), dailyCalories.getOrDefault(userMeal.getDateTime().toLocalDate(), 0) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
