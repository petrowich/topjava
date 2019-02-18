package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private MealRestController mealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            mealRestController = appCtx.getBean(MealRestController.class);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                SecurityUtil.authUserId(),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            mealRestController.create(meal);
        } else {
            mealRestController.update(meal, meal.getId());
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(SecurityUtil.authUserId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");

                String userIdValue = request.getParameter("userId");
                if (userIdValue != null && userIdValue.length() != 0) {
                    int userId = Integer.parseInt(userIdValue);
                    SecurityUtil.setAuthUserId(userId);
                }

                String startDateValue = request.getParameter("startDate");
                LocalDate startDate = null;
                if (startDateValue != null && startDateValue.length() != 0){
                    try {
                        startDate = LocalDate.parse(startDateValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String endDateValue = request.getParameter("endDate");
                LocalDate endDate = null;
                if (endDateValue != null && endDateValue.length() != 0){
                    try {
                        endDate = LocalDate.parse(endDateValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String startTimeValue = request.getParameter("startTime");
                LocalTime startTime = null;
                if (startTimeValue != null && startTimeValue.length() != 0){
                    try {
                        startTime = LocalTime.parse(startTimeValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String endTimeValue = request.getParameter("endTime");
                LocalTime endTime = null;
                if (endTimeValue != null && endTimeValue.length() != 0){
                    try {
                        endTime = LocalTime.parse(endTimeValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                request.setAttribute("meals",
                        MealsUtil.getFilteredWithExcess(mealRestController.getAll(startDate, endDate), MealsUtil.DEFAULT_CALORIES_PER_DAY, startTime, endTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
