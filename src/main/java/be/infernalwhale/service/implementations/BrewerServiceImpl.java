package be.infernalwhale.service.implementations;

import be.infernalwhale.model.Brewer;
import be.infernalwhale.service.BrewersService;
import be.infernalwhale.service.ConnectionManager;
import be.infernalwhale.service.ServiceFactory;
import be.infernalwhale.service.data.BrewerQueries;
import be.infernalwhale.service.data.Valuta;
import be.infernalwhale.service.exception.ValidationException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BrewerServiceImpl implements BrewersService {

    ConnectionManager connectionManager = ServiceFactory.createConnectionManager();

    @Override
    public List<Brewer> getBrewers() {
        List<Brewer> brewers = new ArrayList<>();

        try(Statement statement = connectionManager.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(BrewerQueries.QUERY_GET_BREWERS)) {

            while(resultSet.next()){
                Brewer brewer = new Brewer(resultSet.getInt(BrewerQueries.INDEX_BREWER_ID),
                        resultSet.getString(BrewerQueries.INDEX_BREWER_NAME),
                        resultSet.getString(BrewerQueries.INDEX_BREWER_ADDRESS),
                        Integer.parseInt(resultSet.getString(BrewerQueries.INDEX_BREWER_ZIPCODE)),
                        resultSet.getString(BrewerQueries.INDEX_BREWER_CITY),
                        resultSet.getInt(BrewerQueries.INDEX_BREWER_TURNOVER));
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
                prepareStatement(BrewerQueries.QUERY_GET_BREWERS_WITH_CURRENCY)) {
            statement.setDouble(1, valuta.getConversionRate());

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Brewer brewer = new Brewer(resultSet.getInt(BrewerQueries.INDEX_BREWER_ID),
                        resultSet.getString(BrewerQueries.INDEX_BREWER_NAME),
                        resultSet.getString(BrewerQueries.INDEX_BREWER_ADDRESS),
                        Integer.parseInt(resultSet.getString(BrewerQueries.INDEX_BREWER_ZIPCODE)),
                        resultSet.getString(BrewerQueries.INDEX_BREWER_CITY),
                        resultSet.getInt(BrewerQueries.INDEX_BREWER_TURNOVER));
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
       List<Brewer> brewers = new ArrayList<>();

       try(PreparedStatement statement = connectionManager.getConnection().
               prepareStatement(BrewerQueries.QUERY_GET_BREWERS_NAME_FILTER)) {
           statement.setString(1, nameFilter);

           ResultSet resultSet = statement.executeQuery();

           while(resultSet.next()){
               Brewer brewer = new Brewer(resultSet.getInt(BrewerQueries.INDEX_BREWER_ID),
                       resultSet.getString(BrewerQueries.INDEX_BREWER_NAME),
                       resultSet.getString(BrewerQueries.INDEX_BREWER_ADDRESS),
                       Integer.parseInt(resultSet.getString(BrewerQueries.INDEX_BREWER_ZIPCODE)),
                       resultSet.getString(BrewerQueries.INDEX_BREWER_CITY),
                       resultSet.getInt(BrewerQueries.INDEX_BREWER_TURNOVER));
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
    public List<Brewer> getBrewers(String nameFilter, Valuta valuta) {
        List<Brewer> brewers = new ArrayList<>();

        try(PreparedStatement statement = connectionManager.getConnection().
                prepareStatement(BrewerQueries.QUERY_GET_BREWERS_NAME_AND_CURRENCY_FILTER)) {

            statement.setDouble(1, valuta.getConversionRate());
            statement.setString(2, nameFilter);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                Brewer brewer = new Brewer(resultSet.getInt(BrewerQueries.INDEX_BREWER_ID),
                        resultSet.getString(BrewerQueries.INDEX_BREWER_NAME),
                        resultSet.getString(BrewerQueries.INDEX_BREWER_ADDRESS),
                        Integer.parseInt(resultSet.getString(BrewerQueries.INDEX_BREWER_ZIPCODE)),
                        resultSet.getString(BrewerQueries.INDEX_BREWER_CITY),
                        resultSet.getInt(BrewerQueries.INDEX_BREWER_TURNOVER));
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
    public Brewer createBrewer(Brewer brewer) throws ValidationException {
        try (PreparedStatement statement = connectionManager.getConnection().
                prepareStatement(BrewerQueries.QUERY_CREATE_BREWER)){

            if (brewer.getName().isEmpty()) {
                throw new ValidationException("Brewer name can not be empty");
            }

            if (brewer.getId() == null) {
                statement.setInt(1, 0);
            } else if (brewer.getId() < 0) {
                throw new ValidationException("Id can not be a negative");
            } else {
                statement.setInt(1, brewer.getId());
            }

            if (brewer.getZipcode() < 0){
                throw new ValidationException("Zipcode can not be a negative");
            }

            if (brewer.getTurnover() < 0) throw new ValidationException("Turnover can not be negative");

            statement.setString(2, brewer.getName());
            statement.setString(3, brewer.getAddress());
            statement.setInt(4, brewer.getZipcode());
            statement.setString(5, brewer.getCity());
            statement.setInt(6, brewer.getTurnover());

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
        try (PreparedStatement statement = connectionManager.getConnection().
                prepareStatement(BrewerQueries.QUERY_UPDATE_BREWER)){

            if (brewer.getTurnover() < 0) throw new ValidationException("Turnover can not be negative");
            statement.setInt(5, brewer.getTurnover());

            if (brewer.getId() == null) throw new ValidationException("Id can not be empty");

            if (brewer.getId() < 0) throw new ValidationException("Id can not be negative");

            if (brewer.getZipcode() < 0){
                throw new ValidationException("Zipcode can not be a negative");
            }

            statement.setString(1, brewer.getName());
            statement.setString(2, brewer.getAddress());
            statement.setInt(3, brewer.getZipcode());
            statement.setString(4, brewer.getCity());
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
        try (PreparedStatement statement = connectionManager.getConnection().
                prepareStatement(BrewerQueries.QUERY_DELETE_BREWER)){

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
