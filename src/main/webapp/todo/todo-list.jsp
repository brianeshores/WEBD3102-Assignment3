<%--
  Created by IntelliJ IDEA.
  User: brian
  Date: 2024-02-24
  Time: 11:20 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ page isELIgnored="false" %>


<html>
<head>
  <title>To-do App</title>
</head>

<body>
<header>
  <nav class="navbar navbar-expand-md navbar-dark"
       style="background-color: tomato">
    <div>
      <a href="https://www.javaguides.net" class="navbar-brand"> Todo
        App</a>
    </div>

    <ul class="navbar-nav">
      <li><a href="<%=request.getContextPath()%>/list"
             class="nav-link">Todos</a></li>
    </ul>
  </nav>
</header>

<div class="row">
  <!-- <div class="alert alert-success" *ngIf='message'>{{message}}</div> -->

  <div class="container">
    <h3 class="text-center">List of Todos</h3>
    <hr>
    <div class="container text-left">

      <a href="<%=request.getContextPath()%>/new"
         class="btn btn-success">Add Todo</a>
    </div>
    <br>
    <table class="table table-bordered">
      <thead>
      <tr>
        <th>Title</th>
        <th>Target Date</th>
        <th>Todo Status</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <p></p>
      <c:forEach var="todo" items="${listTodo}">

        <tr>
          <td><c:out value="${todo.title}" /></td>
          <td><c:out value="${todo.targetDate}" /></td>
          <td><c:out value="${todo.status}" /></td>

          <td><a href="edit?id=<c:out value='${todo.id}' />">Edit</a>
            &nbsp;&nbsp;&nbsp;&nbsp; <a
                    href="delete?id=<c:out value='${todo.id}' />">Delete</a></td>

<%--          <td><button (click)="updateTodo(todo.id)" class="btn btn-success">Update</button>--%>
<%--                    <button (click)="deleteTodo(todo.id)" class="btn btn-warning">Delete</button></td>--%>
        </tr>
      </c:forEach>
      <!-- } -->
      </tbody>

    </table>
  </div>
</div>
</body>
</html>
