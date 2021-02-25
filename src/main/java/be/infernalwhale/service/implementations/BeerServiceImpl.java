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

    @Override
    public List<Beer> getBeers() {
        List<Beer> beers = new ArrayList<>();

        try(Statement statement = connectionManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(BeerQueries.QUERY_GET_BEERS)) {

            while(resultSet.next()){
                Category category = new Category(null, resultSet.getString(BeerQueries.INDEX_CATEGORY_NAME));
                Brewer brewer = new Brewer(null, resultSet.getString(BeerQueries.INDEX_BREWERY_NAME), null,
                        null, null, null);

                Beer beer = new Beer(resultSet.getInt(BeerQueries.INDEX_BEERS_ID),
                        resultSet.getString(BeerQueries.INDEX_BEERS_NAME),
                        brewer,
                        category,
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

    @Override
    public List<Beer> getBeers(int alcoholConsumed) {
        List<Beer> beers = new ArrayList<>();

        try(PreparedStatement statement = connectionManager.getConnection().
                prepareStatement(BeerQueries.QUERY_GET_BEERS_ALCOHOL_CONSUMED)) {
            statement.setInt(1, (alcoholConsumed * 3));
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Category category = new Category(null, resultSet.getString(BeerQueries.INDEX_CATEGORY_NAME));
                Brewer brewer = new Brewer(null, resultSet.getString(BeerQueries.INDEX_BREWERY_NAME), null,
                        null, null, null);

                Beer beer = new Beer(resultSet.getInt(BeerQueries.INDEX_BEERS_ID),
                        resultSet.getString(BeerQueries.INDEX_BEERS_NAME),
                        brewer,
                        category,
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
