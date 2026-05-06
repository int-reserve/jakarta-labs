<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Cinema Tickets</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <h1>Available Movie Sessions</h1>
    <form action="${pageContext.request.contextPath}/sessions" method="get" style="margin-bottom: 20px; display: flex; gap: 10px; max-width: 400px;">
        <input type="text" name="title" value="<c:out value='${searchTitle}'/>" placeholder="Search by title" style="flex-grow: 1; padding: 8px; border-radius: 5px; border: 1px solid #bdc3c7;" />
        <button type="submit" class="btn">Search</button>
        <c:if test="${not empty searchTitle}">
            <a href="${pageContext.request.contextPath}/sessions" class="btn" style="background-color: #95a5a6;">Clear</a>
        </c:if>
    </form>
    <ul>
      <c:forEach var="session" items="${sessions}">
        <li>
          <div>
            <strong><c:out value="${session.movieTitle}" /></strong>
            <c:if test="${session.price > 180}">
              <span style="color: #e74c3c; font-size: 0.9em; margin-left: 5px;">(Premium)</span>
            </c:if>
            <br>
            <span style="color: #7f8c8d; font-size: 0.9em;">
              Time: <fmt:parseDate value="${session.startTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
              <fmt:formatDate value="${parsedDate}" pattern="dd.MM.yyyy HH:mm" /> | Price: $<c:out value="${session.price}" />
            </span>
          </div>
          <a class="btn" href="${pageContext.request.contextPath}/book?sessionId=${session.id}">Book Seats</a>
        </li>
      </c:forEach>
    </ul>
    <div style="display: flex; justify-content: center; gap: 10px; margin-top: 20px; align-items: center;">
        <c:if test="${currentPage > 1}">
            <a href="${pageContext.request.contextPath}/sessions?page=${currentPage - 1}&title=${searchTitle}" class="btn" style="background-color: #7f8c8d;">&larr; Prev</a>
        </c:if>

        <span style="font-weight: bold; color: #2c3e50;">
        Page ${currentPage} of ${totalPages == 0 ? 1 : totalPages}
    </span>

        <c:if test="${currentPage < totalPages}">
            <a href="${pageContext.request.contextPath}/sessions?page=${currentPage + 1}&title=${searchTitle}" class="btn" style="background-color: #7f8c8d;">Next &rarr;</a>
        </c:if>
    </div>
    <br />
    <a href="${pageContext.request.contextPath}/admin">⛭ Admin Panel</a>
