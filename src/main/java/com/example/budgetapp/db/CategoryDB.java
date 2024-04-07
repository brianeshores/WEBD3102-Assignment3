package com.example.budgetapp.db;

import com.example.budgetapp.dao.CategoryDao;
import com.example.budgetapp.model.Category;
import com.example.budgetapp.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDB implements CategoryDao {

    private static final String INSERT_CATEGORY_SQL = "INSERT INTO Categories (CategoryName, Type) VALUES (?, ?);";
    private static final String UPDATE_CATEGORY_SQL = "UPDATE Categories SET CategoryName = ?, Type = ? WHERE CategoryId = ?;";
    private static final String DELETE_CATEGORY_SQL = "DELETE FROM Categories WHERE CategoryId = ?;";
    private static final String SELECT_ALL_CATEGORIES_SQL = "SELECT * FROM Categories;";
    private static final String SELECT_CATEGORY_BY_ID_SQL = "SELECT * FROM Categories WHERE CategoryId = ?;";

    @Override
    public long insertCategory(Category category) throws SQLException {
        long categoryId = 0; // Default to 0, indicating no ID was generated

        // The SQL query to insert a new category and retrieve the generated ID
        String INSERT_CATEGORY_SQL = "INSERT INTO Categories (CategoryName, Type) VALUES (?, ?);";

        // Try-with-resources statement to auto-close resources after use
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORY_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, category.getCategoryName());
            preparedStatement.setString(2, category.getType());

            int affectedRows = preparedStatement.executeUpdate();

            // Check if the insert was successful
            if (affectedRows > 0) {
                // Retrieve the generated ID
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        categoryId = generatedKeys.getLong(1); // Assuming the ID is the first column
                    }
                }
            }
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
            // Depending on your error handling, you might want to throw the exception or handle it differently
        }

        return categoryId; // Return the generated category ID
    }

    @Override
    public boolean updateCategory(Category category) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CATEGORY_SQL)) {
            preparedStatement.setString(1, category.getCategoryName());
            preparedStatement.setString(2, category.getType());
            preparedStatement.setLong(3, category.getCategoryId());

            rowUpdated = preparedStatement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    @Override
    public boolean deleteCategory(long categoryId) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_CATEGORY_SQL)) {
            statement.setLong(1, categoryId);

            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public List<Category> selectAllCategories() {
        System.out.println("list categories: ");
        List<Category> categories = new ArrayList<>();
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CATEGORIES_SQL)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("CategoryId");
                String name = rs.getString("CategoryName");
                String type = rs.getString("Type");
                categories.add(new Category(id, name, type));
            }
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
        }
        return categories;
    }

    private long findOrCreateCategory(String categoryName, String type) throws SQLException {
        CategoryDao categoryDao = new CategoryDB(); // Assume this is your DAO implementation

        // Method to find a category by name and type
        // Assuming you have or will implement this method:
        Category category = categoryDao.findByNameAndType(categoryName, type);

        if (category == null) {
            // Category doesn't exist, so create a new one
            category = new Category();
            category.setCategoryName(categoryName);
            category.setType(type);
            // Now insertCategory returns the new ID
            long newCategoryId = categoryDao.insertCategory(category);
            return newCategoryId;
        } else {
            // Category exists, return its ID
            return category.getCategoryId();
        }
    }

    @Override
    public Category selectCategoryById(long categoryId) {
        Category category = null;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CATEGORY_BY_ID_SQL)) {
            preparedStatement.setLong(1, categoryId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String name = rs.getString("CategoryName");
                String type = rs.getString("Type");
                category = new Category(categoryId, name, type);
            }
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
        }
        return category;
    }

    @Override
    public Category findByNameAndType(String categoryName, String type) {
        Category category = null;
        String FIND_BY_NAME_AND_TYPE_SQL = "SELECT * FROM Categories WHERE CategoryName = ? AND Type = ?";

        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME_AND_TYPE_SQL)) {

            preparedStatement.setString(1, categoryName);
            preparedStatement.setString(2, type);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                long id = rs.getLong("CategoryId");
                String name = rs.getString("CategoryName");
                String categoryType = rs.getString("Type");
                category = new Category(id, name, categoryType);
            }
        } catch (SQLException e) {
            JDBCUtils.printSQLException(e);
        }
        return category;
    }
}
