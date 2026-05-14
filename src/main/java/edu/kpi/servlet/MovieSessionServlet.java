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
import java.util.List;

@WebServlet("/sessions")
public class MovieSessionServlet extends HttpServlet {

  @EJB
  private MovieSessionService sessionService;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String titleParam = req.getParameter("title");
    String pageParam = req.getParameter("page");

    int page = (pageParam != null && !pageParam.isEmpty()) ? Integer.parseInt(pageParam) : 1;
    int size = 3;

    List<MovieSession> pagedSessions = sessionService.getFilteredSessions(titleParam, page, size);

    long total = sessionService.countSessions(titleParam);
    int totalPages = (int) Math.ceil((double) total / size);

    req.setAttribute("sessions", pagedSessions);
    req.setAttribute("currentPage", page);
    req.setAttribute("totalPages", totalPages);
    req.setAttribute("searchTitle", titleParam != null ? titleParam : "");

    req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
  }
}