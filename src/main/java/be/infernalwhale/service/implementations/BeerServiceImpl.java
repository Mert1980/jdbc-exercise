package be.infernalwhale.service.implementations;

import be.infernalwhale.model.Beer;
import be.infernalwhale.model.Brewer;
import be.infernalwhale.model.Category;
import be.infernalwhale.service.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BeerServiceImpl implements BeerService {
    public static final String TABLE_BEERS = "Beers";
    public static final String COLUMN_BEERS_ID = "Id";
    public static final String COLUMN_BEERS_NAME = "Name";
    public static final String COLUMN_BEERS_BREWER_ID = "BrewerId";
    public static final String COLUMN_BEERS_CATEGORY_ID = "CategoryId";
    public static final String COLUMN_BEERS_PRICE = "Price";
    public static final String COLUMN_BEERS_STOCK = "Stock";
    public static final String COLUMN_BEERS_ALCOHOL = "Alcohol";

    public static final int INDEX_BEERS_ID = 1;
    public static final int INDEX_BEERS_NAME = 2;
    public static final int INDEX_BREWERY_NAME = 3;
    public static final int INDEX_CATEGORY_NAME = 4;
    public static final int INDEX_BEERS_PRICE = 5;
    public static final int INDEX_BEERS_STOCK = 6;
    public static final int INDEX_BEERS_ALCOHOL = 7;

    public static final String QUERY_GET_BEERS =
            "SELECT " + TABLE_BEERS + "." + COLUMN_BEERS_ID + ", " + TABLE_BEERS + "." + COLUMN_BEERS_NAME + ", " +
            BrewerServiceImpl.TABLE_BREWERS + "." + BrewerServiceImpl.COLUMN_BREWERS_ID + ", " +
            CategoryServiceImpl.TABLE_CATEGORIES + "." + CategoryServiceImpl.COLUMN_CATEGORIES_ID + ", " +
            COLUMN_BEERS_PRICE + ", " + COLUMN_BEERS_STOCK + ", " + COLUMN_BEERS_ALCOHOL + " FROM " + TABLE_BEERS +
            " INNER JOIN " + CategoryServiceImpl.TABLE_CATEGORIES + " ON " + TABLE_BEERS + "." +
            COLUMN_BEERS_CATEGORY_ID + " = " + CategoryServiceImpl.TABLE_CATEGORIES + "." +
            CategoryServiceImpl.COLUMN_CATEGORIES_ID + " INNER JOIN " + BrewerServiceImpl.TABLE_BREWERS + " ON " +
            TABLE_BEERS + "." + COLUMN_BEERS_BREWER_ID + " = " + BrewerServiceImpl.TABLE_BREWERS + "." +
            BrewerServiceImpl.COLUMN_BREWERS_ID;

    public static final String QUERY_GET_BEERS_ALCOHOL_CONSUMED = QUERY_GET_BEERS + " WHERE " + COLUMN_BEERS_ALCOHOL +
            " = ? LIMIT 1";


    ConnectionManager connectionManager = ServiceFactory.createConnectionManager();
    private final BrewersService brewerService = ServiceFactory.createBrewersService();
    private final CategoryService categoryService = ServiceFactory.createCategoryService();

    @Override
    public List<Beer> getBeers() {
        List<Beer> beers = new ArrayList<>();
        List<Brewer> brewers = brewerService.getBrewers();
        List<Category> categories = categoryService.getCategories();

        try(Statement statement = connectionManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY_GET_BEERS)) {
            while(resultSet.next()){
                Beer beer = new Beer(resultSet.getInt(INDEX_BEERS_ID),
                        resultSet.getString(INDEX_BEERS_NAME),
                        getBrewerById(brewers, resultSet.getInt(INDEX_BREWERY_NAME)),
                        getCategoryById(categories, resultSet.getInt(INDEX_CATEGORY_NAME)),
                        resultSet.getFloat(INDEX_BEERS_PRICE),
                        resultSet.getInt(INDEX_BEERS_STOCK),
                        (int)resultSet.getFloat(INDEX_BEERS_ALCOHOL));
                beers.add(beer);
            }
            resultSet.close();
            return beers;
        } catch (SQLException throwables) {
            System.out.println("Query failed " + throwables.getMessage());
            throwables.printStackTrace();
            return null;
        }
    }

    private Category getCategoryById(List<Category> categories, int catID) {
        Category foundCategory = categories.stream().filter(category -> category.getId() == catID).
                findFirst().orElse(null);
        return foundCategory;
    }

    private Brewer getBrewerById(List<Brewer> brewers, int brewerID) {
        Brewer foundBrewer = brewers.stream().filter(brewer -> brewer.getId() == brewerID).
                findFirst().orElse(null);
        return foundBrewer;
    }

    @Override
    public List<Beer> getBeers(int alcoholConsumed) {
        List<Beer> beers = new ArrayList<>();
        List<Brewer> brewers = brewerService.getBrewers();
        List<Category> categories = categoryService.getCategories();

        try(PreparedStatement statement = connectionManager.getConnection().
                prepareStatement(QUERY_GET_BEERS_ALCOHOL_CONSUMED)) {
            statement.setInt(1, alcoholConsumed);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Beer beer = new Beer(resultSet.getInt(INDEX_BEERS_ID),
                        resultSet.getString(INDEX_BEERS_NAME),
                        getBrewerById(brewers, resultSet.getInt(INDEX_BREWERY_NAME)),
                        getCategoryById(categories, resultSet.getInt(INDEX_CATEGORY_NAME)),
                        resultSet.getFloat(INDEX_BEERS_PRICE),
                        resultSet.getInt(INDEX_BEERS_STOCK),
                        (int)resultSet.getFloat(INDEX_BEERS_ALCOHOL));
                beers.add(beer);
            }
            resultSet.close();
            return beers;
        } catch (SQLException throwables) {
            System.out.println("Query failed " + throwables.getMessage());
            throwables.printStackTrace();
            return null;
        }
    }
}
