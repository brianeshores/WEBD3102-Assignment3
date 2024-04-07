package com.example.budgetapp.dao;

import com.example.budgetapp.model.Category;
import java.sql.SQLException;
import java.util.List;

public interface CategoryDao {
    long insertCategory(Category category) throws SQLException;
    boolean updateCategory(Category category) throws SQLException;
    boolean deleteCategory(long categoryId) throws SQLException;
    List<Category> selectAllCategories();
    Category selectCategoryById(long categoryId);

    Category findByNameAndType(String categoryName, String type);
}