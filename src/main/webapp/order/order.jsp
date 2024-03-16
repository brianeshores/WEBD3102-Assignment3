<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Current Order</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/order/order.css">

</head>
<body>
<div class="content-wrapper">
    <h2>Current Order</h2>
    <%-- Display order details here --%>
    <% com.example.ordermanagement.model.Order currentOrder = (com.example.ordermanagement.model.Order) request.getAttribute("currentOrder"); %>
    <% if(currentOrder != null && currentOrder.getProducts() != null && !currentOrder.getProducts().isEmpty()) { %>
    <ul>
        <% for(com.example.ordermanagement.model.Product product : currentOrder.getProducts()) { %>
        <li>
            <%= product.getName() %> - Price: <%= product.getPrice() %>
            <!-- Remove Product Form -->
            <form action="order" method="post">
                <input type="hidden" name="action" value="removeProduct"/>
                <input type="hidden" name="product_id" value="<%= product.getProductId() %>"/>
                <input type="submit" value="Remove"/>
            </form>
        </li>
        <% } %>
    </ul>
    <% } else { %>
    <p>No products in order.</p>
    <% } %>
    <!-- Checkout Button -->
    <a href="order?action=checkout">Checkout</a>
</div>
</body>
</html>