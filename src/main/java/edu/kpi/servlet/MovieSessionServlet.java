package edu.kpi.servlet;

import edu.kpi.model.MovieSession;
import edu.kpi.repository.DataStore;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/sessions")
public class MovieSessionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String titleParam = req.getParameter("title");
        String pageParam = req.getParameter("page");

        int page = (pageParam != null && !pageParam.isEmpty()) ? Integer.parseInt(pageParam) : 1;
        int size = 3; // setting manually for testing purposes and simplicity

        List<MovieSession> allSessions = DataStore.getAllSessions();

        if (titleParam != null && !titleParam.trim().isEmpty()) {
            allSessions = allSessions.stream()
                .filter(s -> s.getMovieTitle().toLowerCase().contains(titleParam.toLowerCase()))
                .collect(Collectors.toList());
        }

        int total = allSessions.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int offset = (page - 1) * size;

        List<MovieSession> pagedSessions;
        if (offset >= total) {
            pagedSessions = List.of();
        } else {
            int toIndex = Math.min(offset + size, total);
            pagedSessions = allSessions.subList(offset, toIndex);
        }

        req.setAttribute("sessions", pagedSessions);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("searchTitle", titleParam != null ? titleParam : "");

        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }
}
