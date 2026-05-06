<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Book Seats</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <h1>Book Seats for <c:out value="${session.movieTitle}"/></h1>
    <p style="color: #7f8c8d;">Session Time: 
        <fmt:parseDate value="${session.startTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
        <fmt:formatDate value="${parsedDate}" pattern="dd.MM.yyyy HH:mm" />
    </p>

    <div class="cinema-hall">
        <div class="screen">SCREEN</div>
        
        <div class="seat-container">
            <c:forEach var="seat" items="${seats}">
                <c:choose>
                    <c:when test="${seat.booked}">
                        <div class="seat seat-booked">
                            <span>S${seat.number}</span>
                            <span style="font-size: 9px; opacity: 0.8;">booked</span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="seat seat-free">
                            <form action="${pageContext.request.contextPath}/book" method="post" style="margin:0; padding:0; width:100%; height:100%;">
                                <input type="hidden" name="sessionId" value="${session.id}" />
                                <input type="hidden" name="seatNumber" value="${seat.number}" />
                                <button type="submit">${seat.number}</button>
                            </form>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </div>

    <br/>
    <a href="${pageContext.request.contextPath}/sessions" style="margin-top: 20px;">&larr; Back to Sessions</a>
</body>
</html>