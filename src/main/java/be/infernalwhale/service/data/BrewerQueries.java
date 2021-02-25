package be.infernalwhale.service.data;

public class BrewerQueries {
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
}
