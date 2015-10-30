package com.ben.colorpicker.db;

/**
 * Created by hui on 15-5-28.
 */
public class Tables {

    public static final String SUFFIX_ID = "id";
    public static final String CONCAT_SEPARATOR = ";";

    private static Column getIdColumn(String tableName) {
        return new Column(tableName, SUFFIX_ID, Column.DataType.TEXT, null);
    }

    private static String makeCreateScript(String table, Column... columns) {
        final StringBuilder sb = new StringBuilder("create table ");
        sb.append(table);
        sb.append(" (");

        if (columns != null) {
            for (int i = 0, size = columns.length; i < size; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(columns[i].getCreateScript());
            }
        }

        sb.append(");");

        return sb.toString();
    }

    public static final class Color {
        public static final String TABLE_NAME = "color";

        public static final Column ID = getIdColumn(TABLE_NAME);
        public static final Column COLOR = new Column(TABLE_NAME, "color_decimal", Column.DataType.INTEGER);
        public static final Column NOTE = new Column(TABLE_NAME, "note", Column.DataType.TEXT);
        public static final Column CREATE_TIME = new Column(TABLE_NAME, "create_time", Column.DataType.TEXT);

        public static final String[] PROJECTION = {ID.getName(), COLOR.getName(), NOTE.getName(),
                CREATE_TIME.getName()};

        public static String createScript() {
            return makeCreateScript(TABLE_NAME, ID,COLOR, NOTE, CREATE_TIME);
        }

        public static Query getQuery() {
            return Query.create()
                    .projection(Color.PROJECTION);
        }
    }
}
