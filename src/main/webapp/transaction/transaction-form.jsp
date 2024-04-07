<%@ page import="com.example.budgetapp.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title><%=request.getAttribute("transaction") != null ? "Edit Transaction" : "Add New Transaction"%></title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/transaction/transaction-form.css">
</head>
<body>
<nav>
  <ul>
    <li><a href="<%=request.getContextPath()%>/dashboard">Home</a></li>
    <li><a href="<%=request.getContextPath()%>/transaction?action=list">Transactions</a></li>
    <li><a href="<%=request.getContextPath()%>/budget?action=list">Budgets</a></li>
  </ul>
</nav>
<div class="form-wrapper">
  <h2><%=request.getAttribute("transaction") != null ? "Edit Transaction" : "Add New Transaction"%></h2>
  <form action="<%=request.getContextPath()%>/transaction" method="post" class="transaction-form">
    <% com.example.budgetapp.model.Transaction transaction = null;
      if (request.getAttribute("transaction") != null) {
        transaction = (com.example.budgetapp.model.Transaction) request.getAttribute("transaction");
      }
    %>
    <input type="hidden" name="transaction_id" value="<%=transaction != null ? transaction.getTransactionId() : "" %>" />

    <div class="form-group">
      <label for="description">Description:</label>
      <input type="text" id="description" name="description" class="form-control" value="<%=transaction != null ? transaction.getDescription() : "" %>" required>
    </div>

    <div class="form-group">
      <label for="amount">Amount:</label>
      <input type="number" step="0.01" id="amount" name="amount" class="form-control" value="<%=transaction != null ? transaction.getAmount().toString() : "" %>" required>
    </div>

    <div class="form-group">
      <label for="transactionDate">Date:</label>
      <input type="date" id="transactionDate" name="transactionDate" class="form-control" value="<%=transaction != null ? transaction.getTransactionDate().toString() : "" %>" required>
    </div>

    <div class="form-group">
      <label for="categoryId">Category:</label>
      <select id="categoryId" name="categoryId" class="form-control" required>
        <% List<Category> categories = (List<Category>) request.getAttribute("listCategory"); // Change "categories" to "listCategory"
          if(categories != null) {
            for(Category category : categories) {
        %>
        <option value="<%= category.getCategoryId() %>"><%= category.getCategoryName() %></option>
        <%
            }
          }
        %>
      </select>
    </div>

    <div class="form-group">
      <label for="categoryType">Category Type:</label>
      <select id="categoryType" name="categoryType" class="form-control" required>
        <option value="Income">Income</option>
        <option value="Expense">Expense</option>
      </select>
    </div>
    <button type="submit" class="btn" name="action" value="<%=transaction != null ? "update" : "add"%>"><%=transaction != null ? "Update" : "Add"%></button>
    <button type="button" class="btn" onclick="window.location.href='<%=request.getContextPath()%>/transaction?action=list'">Back to Transactions</button>
  </form>
</div>
</body>
</html>