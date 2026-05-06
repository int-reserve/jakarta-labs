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
    <br />
    <a href="${pageContext.request.contextPath}/admin">⛭ Admin Panel</a>
