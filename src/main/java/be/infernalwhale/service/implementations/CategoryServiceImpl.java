package be.infernalwhale.service.implementations;

import be.infernalwhale.model.Category;
import be.infernalwhale.service.CategoryService;
import be.infernalwhale.service.ConnectionManager;
import be.infernalwhale.service.ServiceFactory;
import be.infernalwhale.service.data.CategoryQueries;
import be.infernalwhale.service.exception.ValidationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryServiceImpl implements CategoryService{
    ConnectionManager connectionManager = ServiceFactory.createConnectionManager();

    @Override
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();

        try(Statement statement = connectionManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(CategoryQueries.QUERY_GET_CATEGORIES)) {

            while(resultSet.next()){
                Category category = new Category(resultSet.getInt(CategoryQueries.INDEX_CATEGORY_ID),
                        resultSet.getString(CategoryQueries.INDEX_CATEGORY_NAME));
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
        try (PreparedStatement statement = connectionManager.getConnection().
                prepareStatement(CategoryQueries.QUERY_CREATE_CATEGORY)){

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
        try (PreparedStatement statement = connectionManager.getConnection().
                prepareStatement(CategoryQueries.QUERY_UPDATE_CATEGORY)){
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
        try (PreparedStatement statement = connectionManager.getConnection().
                prepareStatement(CategoryQueries.QUERY_DELETE_CATEGORY)){
            statement.setInt(1, category.getId());
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
