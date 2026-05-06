<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Admin Panel - Manage Sessions</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
  </head>
  <body>
    <h1>Manage Cinema Sessions</h1>

    <div class="admin-form">
      <h2>Add New Session</h2>
      <form action="${pageContext.request.contextPath}/admin" method="post">
        <input type="hidden" name="action" value="add" />
        <label>Movie Title 
          <input type="text" name="movieTitle" required />
        </label>
        <label>Price 
          <input type="number" name="price" step="0.01" required />
        </label>
        <button type="submit" class="btn" style="width: 100%;">Add Session</button>
      </form>
    </div>

    <h2>Current Sessions</h2>
    <table>
      <tr>
        <th>Movie Title</th>
        <th>Price</th>
        <th>Action</th>
      </tr>
      <c:forEach var="session" items="${sessions}">
        <tr>
          <td><c:out value="${session.movieTitle}" /></td>
          <td>$<c:out value="${session.price}" /></td>
          <td>
            <form
              action="${pageContext.request.contextPath}/admin"
              method="post" style="margin: 0;"
            >
              <input type="hidden" name="action" value="delete" />
              <input type="hidden" name="sessionId" value="${session.id}" />
              <button type="submit" class="btn" style="background-color: #e74c3c;">Delete</button>
            </form>
          </td>
        </tr>
      </c:forEach>
    </table>

    <br />
    <a href="${pageContext.request.contextPath}/sessions" style="margin-top: 20px;">&larr; Back to Home</a>
  </body>
</html>
