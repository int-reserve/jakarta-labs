package edu.kpi.servlet;

import edu.kpi.model.MovieSession;
import edu.kpi.repository.DataStore;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/book")
public class BookingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getParameter("sessionId");
        MovieSession session = DataStore.getSession(sessionId);
        if (session != null) {
            req.setAttribute("session", session);
            req.setAttribute("seats", DataStore.getSeats(sessionId));
            req.getRequestDispatcher("/WEB-INF/views/session.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Session not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getParameter("sessionId");
        String seatParam = req.getParameter("seatNumber");
        
        if (sessionId != null && seatParam != null) {
            try {
                int seatNumber = Integer.parseInt(seatParam);
                DataStore.bookSeat(sessionId, seatNumber);
                resp.sendRedirect(req.getContextPath() + "/book?sessionId=" + sessionId);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid seat number");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
        }
    }
}
