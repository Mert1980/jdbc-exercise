package be.infernalwhale.service.implementations;

import be.infernalwhale.model.Beer;
import be.infernalwhale.model.Brewer;
import be.infernalwhale.model.Category;
import be.infernalwhale.service.*;
import be.infernalwhale.service.data.BeerQueries;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BeerServiceImpl implements BeerService {

    ConnectionManager connectionManager = ServiceFactory.createConnectionManager();
    private final BrewersService brewerService = ServiceFactory.createBrewersService();
    private final CategoryService categoryService = ServiceFactory.createCategoryService();

    @Override
    public List<Beer> getBeers() {
        List<Beer> beers = new ArrayList<>();
        List<Brewer> brewers = brewerService.getBrewers();
        List<Category> categories = categoryService.getCategories();

        try(Statement statement = connectionManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(BeerQueries.QUERY_GET_BEERS)) {

            while(resultSet.next()){
                Beer beer = new Beer(resultSet.getInt(BeerQueries.INDEX_BEERS_ID),
                        resultSet.getString(BeerQueries.INDEX_BEERS_NAME),
                        getBrewerById(brewers, resultSet.getInt(BeerQueries.INDEX_BREWERY_NAME)),
                        getCategoryById(categories, resultSet.getInt(BeerQueries.INDEX_CATEGORY_NAME)),
                        resultSet.getFloat(BeerQueries.INDEX_BEERS_PRICE),
                        resultSet.getInt(BeerQueries.INDEX_BEERS_STOCK),
                        (int)resultSet.getFloat(BeerQueries.INDEX_BEERS_ALCOHOL));
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
                prepareStatement(BeerQueries.QUERY_GET_BEERS_ALCOHOL_CONSUMED)) {
            statement.setInt(1, (alcoholConsumed * 3));
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Beer beer = new Beer(resultSet.getInt(BeerQueries.INDEX_BEERS_ID),
                        resultSet.getString(BeerQueries.INDEX_BEERS_NAME),
                        getBrewerById(brewers, resultSet.getInt(BeerQueries.INDEX_BREWERY_NAME)),
                        getCategoryById(categories, resultSet.getInt(BeerQueries.INDEX_CATEGORY_NAME)),
                        resultSet.getFloat(BeerQueries.INDEX_BEERS_PRICE),
                        resultSet.getInt(BeerQueries.INDEX_BEERS_STOCK),
                        (int)resultSet.getFloat(BeerQueries.INDEX_BEERS_ALCOHOL));
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
