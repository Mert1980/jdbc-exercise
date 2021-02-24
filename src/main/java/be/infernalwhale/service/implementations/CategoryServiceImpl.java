package be.infernalwhale.service.implementations;

import be.infernalwhale.model.Category;
import be.infernalwhale.service.CategoryService;
import be.infernalwhale.service.ConnectionManager;
import be.infernalwhale.service.ServiceFactory;
import be.infernalwhale.service.exception.ValidationException;
import com.mysql.cj.jdbc.ConnectionImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImpl extends ConnectionManagerImpl implements CategoryService{
    public static final String TABLE_CATEGORIES = "Categories";
    public static final String COLUMN_CATEGORIES_ID = "Id";
    public static final String COLUMN_CATEGORIES_CATEGORY = "Category";
    public static final int INDEX_CATEGORY_ID = 1;
    public static final int INDEX_CATEGORY_NAME = 2;

    public static final String QUERY_GET_CATEGORIES = "SELECT * FROM " + TABLE_CATEGORIES;
    public static final String QUERY_CREATE_CATEGORY = "INSERT INTO " + TABLE_CATEGORIES +
            '(' + COLUMN_CATEGORIES_ID + ", " + COLUMN_CATEGORIES_CATEGORY + ") VALUES(?, ?)";

    public static final String QUERY_DELETE_CATEGORY = "DELETE FROM " + TABLE_CATEGORIES +
            " WHERE " + COLUMN_CATEGORIES_CATEGORY + " LIKE ?";

    public static final String QUERY_UPDATE_CATEGORY = "UPDATE " + TABLE_CATEGORIES +
            " SET " + COLUMN_CATEGORIES_CATEGORY + " = ? WHERE " + COLUMN_CATEGORIES_ID + " = ?";

    ConnectionManager connectionManager = ServiceFactory.createConnectionManager();

    @Override
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();

        try(Statement statement = connectionManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY_GET_CATEGORIES)) {
            while(resultSet.next()){
                Category category = new Category(resultSet.getInt(INDEX_CATEGORY_ID),
                        resultSet.getString(INDEX_CATEGORY_NAME));
                categories.add(category);
            }
            resultSet.close();
            return categories;
        } catch (SQLException throwables) {
            System.out.println("Query failed " + throwables.getMessage());
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public Category createCategory(Category category) throws ValidationException {
        try (PreparedStatement statement = connectionManager.getConnection().prepareStatement(QUERY_CREATE_CATEGORY)){

            if(category.getId() == null){
                statement.setInt(1, 0);
            } else {
                statement.setInt(1, category.getId());
            }

            statement.setString(2, category.getCategoryName());
            int affectedRows = statement.executeUpdate();
            if(affectedRows != 1) {
                throw new SQLException("Couldn't create category!");
            }
        } catch (SQLException throwables ){
            System.out.println("Create category failed " + throwables.getMessage());
        }
        return category;
    }

    @Override
    public Category updateCategory(Category category) {
        try (PreparedStatement statement = connectionManager.getConnection().prepareStatement(QUERY_UPDATE_CATEGORY)){
            statement.setString(1, category.getCategoryName());
            statement.setInt(2, category.getId());
            int affectedRows = statement.executeUpdate();
            if(affectedRows != 1) {
                throw new SQLException("Couldn't update category!");
            }
        } catch (SQLException throwables ){
            System.out.println("Update category failed " + throwables.getMessage());
        }
        return category;
    }

    @Override
    public boolean deleteCategory(Category category) {
        try (PreparedStatement statement = connectionManager.getConnection().prepareStatement(QUERY_DELETE_CATEGORY)){
            statement.setString(1, category.getCategoryName());
            int affectedRows = statement.executeUpdate();
            if(affectedRows != 1) {
                throw new SQLException("Couldn't delete category!");
            }
            return true;
        } catch (SQLException throwables){
            System.out.println("Failed to delete category " + category.getCategoryName() +
                    " " + throwables.getMessage());
            return false;
        }
    }
}
