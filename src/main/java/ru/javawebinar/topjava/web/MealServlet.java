package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class MealServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    private static final MealStorage mealStorage = MealStorage.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String editMealId = request.getParameter("editmeal");
        String deleteMealId = request.getParameter("delmeal");

        if (editMealId != null) {
            Meal meal;

            try {
                meal = mealStorage.get(Long.parseLong(editMealId));
            } catch (NumberFormatException e) {
                meal = new Meal(null, LocalDateTime.now(), "", 0);
            }

            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/meal.jsp").forward(request, response);
            getServletContext().getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }

        if (deleteMealId != null) {
            LOG.debug("delete meal " + deleteMealId);
            mealStorage.delete(Long.parseLong(deleteMealId));
            response.sendRedirect("meals");
            return;
        }

        LOG.debug("redirect to meals");
        request.setAttribute("mealToList", MealsUtil.getFilteredWithExcess(mealStorage.getAll(), LocalTime.MIDNIGHT, LocalTime.MAX, 2000));
        getServletContext().getRequestDispatcher("/meals.jsp").forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        Long id;
        try {
            id = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            id = null;
        }

        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(request.getParameter("datetime"));
        } catch (Exception e) {
            dateTime = LocalDateTime.now();
        }

        String description = request.getParameter("description");

        int calories;
        try {
            calories = Integer.parseInt(request.getParameter("calories"));
        } catch (NumberFormatException e) {
            calories = 0;
        }

        if (id == null){
            LOG.debug("add new meal");
            mealStorage.insert(dateTime, description, calories);
        } else {
            LOG.debug("update meal " + id);
            mealStorage.update(new Meal(id, dateTime, description, calories));
        }

        request.setAttribute("mealToList", MealsUtil.getFilteredWithExcess(mealStorage.getAll(), LocalTime.MIDNIGHT, LocalTime.MAX, 2000));
        getServletContext().getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
