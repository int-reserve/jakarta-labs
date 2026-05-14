package edu.kpi.servlet;

import edu.kpi.model.MovieSession;
import edu.kpi.service.MovieSessionService;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

  @EJB
  private MovieSessionService sessionService;

  private final Validator validator;

  public AdminServlet() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    this.validator = factory.getValidator();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String editIdParam = req.getParameter("editId");
    if (editIdParam != null && !editIdParam.isEmpty()) {
      try {
        long editId = Long.parseLong(editIdParam);
        MovieSession sessionToEdit = sessionService.getSession(editId);
        req.setAttribute("editSession", sessionToEdit);
      } catch (NumberFormatException ignored) {
      }
    }

    req.setAttribute("sessions", sessionService.getAllSessions());
    req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String action = req.getParameter("action");

    try {
      if ("add".equals(action) || "update".equals(action)) {
        String sessionIdParam = req.getParameter("sessionId");

        String title = req.getParameter("movieTitle");
        double price = Double.parseDouble(req.getParameter("price"));
        LocalDateTime startTime = LocalDateTime.parse(req.getParameter("startTime"));
        
        Long sessionId = null;
        if ("update".equals(action) && sessionIdParam != null && !sessionIdParam.isEmpty()) {
          sessionId = Long.parseLong(sessionIdParam);
        }

        MovieSession session = new MovieSession(sessionId, title, startTime, price);

        Set<ConstraintViolation<MovieSession>> violations = validator.validate(session);
        if (!violations.isEmpty()) {
          List<String> errors = violations.stream()
              .map(ConstraintViolation::getMessage)
              .collect(Collectors.toList());
          req.setAttribute("errors", errors);
          if ("update".equals(action)) {
            req.setAttribute("editSession", session);
          }
          req.setAttribute("sessions", sessionService.getAllSessions());
          req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
          return;
        }

        if ("add".equals(action)) {
          sessionService.addSession(session);
        } else {
          sessionService.updateSession(sessionId, session);
        }

      } else if ("delete".equals(action)) {
        long id = Long.parseLong(req.getParameter("sessionId"));
        sessionService.deleteSession(id);
      }
    } catch (Exception e) {
      req.setAttribute("errors",
          List.of("Помилка обробки форми: перевірте формат введених даних."));
      req.setAttribute("sessions", sessionService.getAllSessions());
      req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
      return;
    }

    resp.sendRedirect(req.getContextPath() + "/admin");
  }
}
