package be.infernalwhale.service.data;

public class CategoryQueries {
    public static final String TABLE_CATEGORIES = "Categories";
    public static final String COLUMN_CATEGORIES_ID = "Id";
    public static final String COLUMN_CATEGORIES_CATEGORY = "Category";
    public static final int INDEX_CATEGORY_ID = 1;
    public static final int INDEX_CATEGORY_NAME = 2;

    public static final String QUERY_GET_CATEGORIES = "SELECT * FROM " + TABLE_CATEGORIES;
    public static final String QUERY_CREATE_CATEGORY = "INSERT INTO " + TABLE_CATEGORIES +
            '(' + COLUMN_CATEGORIES_ID + ", " + COLUMN_CATEGORIES_CATEGORY + ") VALUES(?, ?)";

    public static final String QUERY_DELETE_CATEGORY = "DELETE FROM " + TABLE_CATEGORIES +
            " WHERE " + COLUMN_CATEGORIES_ID + " = ?";

    public static final String QUERY_UPDATE_CATEGORY = "UPDATE " + TABLE_CATEGORIES +
            " SET " + COLUMN_CATEGORIES_CATEGORY + " = ? WHERE " + COLUMN_CATEGORIES_ID + " = ?";
}
