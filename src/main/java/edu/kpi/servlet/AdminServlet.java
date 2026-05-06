package edu.kpi.servlet;

import edu.kpi.model.MovieSession;
import edu.kpi.repository.DataStore;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String editId = req.getParameter("editId");
        if (editId != null) {
            MovieSession sessionToEdit = DataStore.getSession(editId);
            req.setAttribute("editSession", sessionToEdit);
        }
        req.setAttribute("sessions", DataStore.getAllSessions());
        req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        
        if ("add".equals(action)) {
            String title = req.getParameter("movieTitle");
            String priceStr = req.getParameter("price");
            String timeStr = req.getParameter("startTime");

            if (title != null && !title.trim().isEmpty() && priceStr != null && timeStr != null && !timeStr.isEmpty()) {
                try {
                    LocalDateTime startTime = LocalDateTime.parse(timeStr);
                    double price = Double.parseDouble(priceStr);
                    MovieSession newSession = new MovieSession(UUID.randomUUID().toString(), title, startTime, price);
                    DataStore.addSession(newSession);
                } catch (NumberFormatException e) {
                    // pass for the sake of simplicity in lab work
                }
            }
        } else if ("delete".equals(action)) {
            String id = req.getParameter("sessionId");
            if (id != null) {
                DataStore.deleteSession(id);
            }
        } else if ("update".equals(action)) {
            String id = req.getParameter("sessionId");
            String title = req.getParameter("movieTitle");
            String priceStr = req.getParameter("price");
            String timeStr = req.getParameter("startTime");

            MovieSession existing = DataStore.getSession(id);
            if (existing != null) {
                existing.setMovieTitle(title);
                existing.setPrice(Double.parseDouble(priceStr));
                existing.setStartTime(LocalDateTime.parse(timeStr));
            }
        }

        
        resp.sendRedirect(req.getContextPath() + "/admin");
    }
}
