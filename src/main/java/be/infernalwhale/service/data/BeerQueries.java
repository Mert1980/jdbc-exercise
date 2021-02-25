package be.infernalwhale.service.data;

public class BeerQueries {
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
                    BrewerQueries.TABLE_BREWERS + "." + BrewerQueries.COLUMN_BREWERS_ID + ", " +
                    CategoryQueries.TABLE_CATEGORIES + "." + CategoryQueries.COLUMN_CATEGORIES_ID + ", " +
                    COLUMN_BEERS_PRICE + ", " + COLUMN_BEERS_STOCK + ", " + COLUMN_BEERS_ALCOHOL + " FROM " + TABLE_BEERS +
                    " INNER JOIN " + CategoryQueries.TABLE_CATEGORIES + " ON " + TABLE_BEERS + "." +
                    COLUMN_BEERS_CATEGORY_ID + " = " + CategoryQueries.TABLE_CATEGORIES + "." +
                    CategoryQueries.COLUMN_CATEGORIES_ID + " INNER JOIN " + BrewerQueries.TABLE_BREWERS + " ON " +
                    TABLE_BEERS + "." + COLUMN_BEERS_BREWER_ID + " = " + BrewerQueries.TABLE_BREWERS + "." +
                    BrewerQueries.COLUMN_BREWERS_ID;

    public static final String QUERY_GET_BEERS_ALCOHOL_CONSUMED = QUERY_GET_BEERS + " WHERE " + COLUMN_BEERS_ALCOHOL +
            " = ? LIMIT 1";


}
