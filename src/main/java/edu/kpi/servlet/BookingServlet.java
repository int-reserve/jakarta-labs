package edu.kpi.servlet;

import edu.kpi.model.MovieSession;
import edu.kpi.service.MovieSessionService;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/book")
public class BookingServlet extends HttpServlet {

  @EJB
  private MovieSessionService sessionService;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String sessionIdParam = req.getParameter("sessionId");
    if (sessionIdParam == null || sessionIdParam.isEmpty()) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sessionId parameter");
      return;
    }
    try {
      long sessionId = Long.parseLong(sessionIdParam);
      MovieSession session = sessionService.getSession(sessionId);
      if (session == null) {
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Session not found");
        return;
      }
      req.setAttribute("session", session);
      req.setAttribute("seats", sessionService.getSeats(sessionId));
      req.getRequestDispatcher("/WEB-INF/views/session.jsp").forward(req, resp);
    } catch (NumberFormatException e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid sessionId");
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String sessionIdParam = req.getParameter("sessionId");
    String seatParam = req.getParameter("seatNumber");

    if (sessionIdParam == null || seatParam == null) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
      return;
    }
    try {
      long sessionId = Long.parseLong(sessionIdParam);
      int seatNumber = Integer.parseInt(seatParam);
      sessionService.bookSeat(sessionId, seatNumber);
      resp.sendRedirect(req.getContextPath() + "/book?sessionId=" + sessionId);
    } catch (NumberFormatException e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter format");
    }
  }
}
