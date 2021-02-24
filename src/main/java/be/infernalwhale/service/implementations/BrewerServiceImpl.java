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
    public static final String QUERY_CREATE_BREWER = "INSERT INTO " + TABLE_BREWERS +
            '(' + COLUMN_BREWERS_ID + ", " + COLUMN_BREWERS_NAME + ", " + COLUMN_BREWERS_ADDRESS + ", " +
            COLUMN_BREWERS_ZIPCODE + ", " + COLUMN_BREWERS_CITY + ", " + COLUMN_BREWERS_TURNOVER + ") " +
            " VALUES(?, ?, ?, ?, ?, ?)";

    public static final String QUERY_UPDATE_BREWER = "UPDATE " + TABLE_BREWERS +
            " SET " + COLUMN_BREWERS_NAME + " = ?, " + COLUMN_BREWERS_ADDRESS + " = ?, " + COLUMN_BREWERS_ZIPCODE +
            " = ?, " + COLUMN_BREWERS_CITY + " = ?, " + COLUMN_BREWERS_TURNOVER + " = ? " +
            " WHERE " + COLUMN_BREWERS_ID + " = ?";

    public static final String QUERY_DELETE_BREWER = "DELETE FROM " + TABLE_BREWERS +
            " WHERE " + COLUMN_BREWERS_ID + " = ?";

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
    public List<Brewer> getBrewers(String nameFilter) {
        return null;
    }

    @Override
    public List<Brewer> getBrewers(String nameFilter, Valuta valuta) {
        return null;
    }

    @Override
    public Brewer createBrewer(Brewer brewer) throws ValidationException {
        try (PreparedStatement statement = connectionManager.getConnection().prepareStatement(QUERY_CREATE_BREWER)){

            if(brewer.getId() == null){
                statement.setInt(1, 0);
            } else {
                statement.setInt(1, brewer.getId());
            }

            statement.setString(2, brewer.getName());
            statement.setString(3, brewer.getAddress());
            statement.setInt(4, brewer.getZipcode());
            statement.setString(5, brewer.getCity());
            statement.setInt(6, brewer.getTurnover());

            if (brewer.getTurnover() < 0) throw new ValidationException("Turnover can not be negative");

            int affectedRows = statement.executeUpdate();
            if(affectedRows != 1) {
                throw new SQLException("Couldn't create category!");
            }
        } catch (SQLException throwables ){
            System.out.println("Create category failed " + throwables.getMessage());
        }
        return brewer;
    }

    @Override
    public Brewer updateBrewer(Brewer brewer) throws ValidationException {
        try (PreparedStatement statement = connectionManager.getConnection().prepareStatement(QUERY_UPDATE_BREWER)){
            statement.setString(1, brewer.getName());
            statement.setString(2, brewer.getAddress());
            statement.setInt(3, brewer.getZipcode());
            statement.setString(4, brewer.getCity());

            if (brewer.getTurnover() < 0) throw new ValidationException("Turnover can not be negative");
            statement.setInt(5, brewer.getTurnover());

            if (brewer.getId() == null) throw new ValidationException("Id can not be empty");
            statement.setInt(6, brewer.getId());

            int affectedRows = statement.executeUpdate();
            if(affectedRows != 1) {
                throw new SQLException("Couldn't update category!");
            }
        } catch (SQLException throwables ){
            System.out.println("Update category failed " + throwables.getMessage());
        }
        return brewer;
    }

    @Override
    public boolean deleteBrewerById(Integer id) {
        try (PreparedStatement statement = connectionManager.getConnection().prepareStatement(QUERY_DELETE_BREWER)){
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            if(affectedRows != 1) {
                throw new SQLException("Couldn't delete brewer!");
            }
            return true;
        } catch (SQLException throwables){
            System.out.println("Failed to delete brewer " + throwables.getMessage());
            return false;
        }
    }
}
