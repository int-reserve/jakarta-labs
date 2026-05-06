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
        req.setAttribute("sessions", DataStore.getAllSessions());
        req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        
        if ("add".equals(action)) {
            String title = req.getParameter("movieTitle");
            String priceStr = req.getParameter("price");
            
            if (title != null && !title.trim().isEmpty() && priceStr != null) {
                try {
                    double price = Double.parseDouble(priceStr);
                    MovieSession newSession = new MovieSession(UUID.randomUUID().toString(), title, LocalDateTime.now().plusDays(1), price);
                    DataStore.addSession(newSession);
                } catch (NumberFormatException e) {
                    // Ignore for simplistic lab
                }
            }
        } else if ("delete".equals(action)) {
            String id = req.getParameter("sessionId");
            if (id != null) {
                DataStore.deleteSession(id);
            }
        }
        
        resp.sendRedirect(req.getContextPath() + "/admin");
    }
}
