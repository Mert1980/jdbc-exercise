package be.infernalwhale.service.implementations;

import be.infernalwhale.model.Brewer;
import be.infernalwhale.model.Category;
import be.infernalwhale.service.BrewersService;
import be.infernalwhale.service.ConnectionManager;
import be.infernalwhale.service.ServiceFactory;
import be.infernalwhale.service.data.Valuta;
import be.infernalwhale.service.exception.ValidationException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BrewerServiceImpl implements BrewersService {

    public static final String TABLE_BREWERS = "Brewers";
    public static final String COLUMN_BREWERS_ID = "Id";
    public static final String COLUMN_BREWERS_NAME = "Name";
    public static final String COLUMN_BREWERS_ADDRESS = "Address";
    public static final String COLUMN_BREWERS_ZIPCODE = "ZipCode";
    public static final String COLUMN_BREWERS_CITY = "City";
    public static final String COLUMN_BREWERS_TURNOVER = "Turnover";

    public static final int INDEX_BREWER_ID = 1;
    public static final int INDEX_BREWER_NAME = 2;
    public static final int INDEX_BREWER_ADDRESS = 3;
    public static final int INDEX_BREWER_ZIPCODE = 4;
    public static final int INDEX_BREWER_CITY = 5;
    public static final int INDEX_BREWER_TURNOVER = 6;

    public static final String QUERY_GET_BREWERS = "SELECT * FROM " + TABLE_BREWERS;
    public static final String QUERY_GET_BREWERS_WITH_CURRENCY =
            "SELECT " + COLUMN_BREWERS_ID + ", " + COLUMN_BREWERS_NAME + ", " + COLUMN_BREWERS_ADDRESS + ", " +
                    COLUMN_BREWERS_ZIPCODE + ", " + COLUMN_BREWERS_CITY + ", " + COLUMN_BREWERS_TURNOVER + " * ?" +
                    " FROM " + TABLE_BREWERS;

    ConnectionManager connectionManager = ServiceFactory.createConnectionManager();

    @Override
    public List<Brewer> getBrewers() {
        List<Brewer> brewers = new ArrayList<>();

        try(Statement statement = connectionManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY_GET_BREWERS)) {
            while(resultSet.next()){
                Brewer brewer = new Brewer(resultSet.getInt(INDEX_BREWER_ID),
                        resultSet.getString(INDEX_BREWER_NAME), resultSet.getString(INDEX_BREWER_ADDRESS),
                        Integer.parseInt(resultSet.getString(INDEX_BREWER_ZIPCODE)), resultSet.getString(INDEX_BREWER_CITY),
                        resultSet.getInt(INDEX_BREWER_TURNOVER));
                brewers.add(brewer);
            }
            resultSet.close();
            return brewers;
        } catch (SQLException throwables) {
            System.out.println("Query failed " + throwables.getMessage());
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Brewer> getBrewers(Valuta valuta) {
        List<Brewer> brewers = new ArrayList<>();

        try(PreparedStatement statement = connectionManager.getConnection().
                prepareStatement(QUERY_GET_BREWERS_WITH_CURRENCY)) {
            statement.setDouble(1, valuta.getConversionRate());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Brewer brewer = new Brewer(resultSet.getInt(INDEX_BREWER_ID),
                        resultSet.getString(INDEX_BREWER_NAME), resultSet.getString(INDEX_BREWER_ADDRESS),
                        Integer.parseInt(resultSet.getString(INDEX_BREWER_ZIPCODE)), resultSet.getString(INDEX_BREWER_CITY),
                        resultSet.getInt(INDEX_BREWER_TURNOVER));
                brewer = convertToCurrency(brewer, valuta);
                brewers.add(brewer);
            }
            resultSet.close();
            return brewers;
        } catch (SQLException throwables) {
            System.out.println("Query failed " + throwables.getMessage());
            throwables.printStackTrace();
            return null;
        }
    }

    private Brewer convertToCurrency(Brewer b, Valuta currency) {
        Brewer brewerCopy = new Brewer(b);
        Integer turnover = (int) Math.round(b.getTurnover() * currency.getConversionRate());
        brewerCopy.setTurnover(turnover);
        return brewerCopy;
    }

    @Override
    public List<Brewer> getBrewers(String nameFilter) {
        return null;
    }

    @Override
    public List<Brewer> getBrewers(String nameFilter, Valuta valuta) {
        return null;
    }

    @Override
    public Brewer createBrewer(Brewer brewer) throws ValidationException {
        return null;
    }

    @Override
    public Brewer updateBrewer(Brewer brewer) throws ValidationException {
        return null;
    }

    @Override
    public boolean deleteBrewerById(Integer id) {
        return false;
    }
}
