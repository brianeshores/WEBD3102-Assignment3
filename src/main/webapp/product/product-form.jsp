        <%--
  Created by IntelliJ IDEA.
  User: brian
  Date: 2024-03-12
  Time: 3:34 p.m.
  To change this template use File | Settings | File Templates.
--%>
        <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>Product Form</title>
            <link rel="stylesheet" href="<%=request.getContextPath()%>/product/product-form.css">
        </head>
        <body>
        <div class="form-wrapper">
            <h2><%=request.getAttribute("product") != null ? "Edit Product" : "Add New Product"%></h2>
            <form action="product" method="post" class="product-form">
                <% com.example.ordermanagement.model.Product product = null;
                    if (request.getAttribute("product") != null) {
                        product = (com.example.ordermanagement.model.Product) request.getAttribute("product");
                %>
                <input type="hidden" name="product_id" value="<%=product.getProductId()%>" />
                <% } %>
                <div class="form-group">
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name" class="form-control" value="<%=request.getAttribute("product") != null ? product.getName() : ""%>" required>
                </div>

                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea id="description" name="description" class="form-control" required><%=request.getAttribute("product") != null ? product.getDescription() : ""%></textarea>
                </div>

                <div class="form-group">
                    <label for="price">Price:</label>
                    <input type="number" step="0.01" id="price" name="price" class="form-control" value="<%=request.getAttribute("product") != null ? product.getPrice() : ""%>" required>
                </div>

                <div class="form-group">
                    <label for="quantity">Quantity:</label>
                    <input type="number" id="quantity" name="quantity" class="form-control" value="<%=request.getAttribute("product") != null ? product.getQuantity() : ""%>" required>
                </div>

                <div class="form-group">
                    <label for="category">Category:</label>
                    <input type="text" id="category" name="category" class="form-control" value="<%=request.getAttribute("product") != null ? product.getCategory() : ""%>" required>
                </div>

                <button type="submit" class="btn" name="action" value="<%=request.getAttribute("product") != null ? "update" : "insert"%>"><%=request.getAttribute("product") != null ? "Update" : "Add"%></button>
            </form>
        </div>
        </body>
        </html>


