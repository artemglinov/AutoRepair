package data;

import model.Mechanic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOMechanics {
    public static final String TABLE_MECHANICS = "Mechanics";

    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_PATRONYMIC = "patronymic";
    public static final String COLUMN_HOURLY_PAYMENT = "hourly_payment";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STATS = "stats";

    public static final String QUERY_MECHANICS = "SELECT * FROM " + TABLE_MECHANICS;

    public static final String QUERY_MECHANICS_STATS = "SELECT " + " COUNT(DISTINCT " + DAOOrders.TABLE_ORDERS
            + "." + DAOOrders.COLUMN_MECHANIC_ID
            + ") AS STATS FROM " + TABLE_MECHANICS + " LEFT JOIN "
            + DAOOrders.TABLE_ORDERS + " ON " + TABLE_MECHANICS
            + "." + COLUMN_ID + " = " + DAOOrders.TABLE_ORDERS
            + "." + DAOOrders.COLUMN_MECHANIC_ID
            + " WHERE " + COLUMN_ID + " = ? "
            + " GROUP BY " + TABLE_MECHANICS + "." + COLUMN_ID;

    public static final String INSERT_MECHANIC = "INSERT INTO " + TABLE_MECHANICS +
            " (" + COLUMN_FIRST_NAME + ", " + COLUMN_PATRONYMIC + ", "
            + COLUMN_LAST_NAME + ", " + COLUMN_HOURLY_PAYMENT
            + ") VALUES(?, ?, ?, ?)";

    public static final String UPDATE_MECHANIC = "UPDATE " + TABLE_MECHANICS +
            " SET " + COLUMN_FIRST_NAME + " = ?, " + COLUMN_PATRONYMIC + " = ?, "
            + COLUMN_LAST_NAME + " = ?, " + COLUMN_HOURLY_PAYMENT + " = ? WHERE "
            + COLUMN_ID + " = ?";

    public static final String DELETE_MECHANIC = "DELETE FROM " + TABLE_MECHANICS +
            " WHERE " + COLUMN_ID + " = ?";

//    public static final String CHECK_MECHANIC_BY_NAME = QUERY_MECHANICS + " WHERE (" + COLUMN_FIRST_NAME +
//                                                            " = ?) AND (" + COLUMN_PATRONYMIC + " = ?) " +
//            "                                               AND (" + COLUMN_LAST_NAME + " = ?)";


    public static final String CHECK_MECHANIC_BY_ID = QUERY_MECHANICS + " WHERE " + COLUMN_ID + " = ?";

    public static List<Mechanic> query(PreparedStatement queryMechanics) {
        try {
            ResultSet results = queryMechanics.executeQuery();

            List<Mechanic> mechanics = new ArrayList<>();

            while (results.next()) {
                Mechanic mechanic = new Mechanic();
                mechanic.setId(results.getInt(COLUMN_ID));
                mechanic.setFirst_name(results.getString(COLUMN_FIRST_NAME));
                mechanic.setPatronymic(results.getString(COLUMN_PATRONYMIC));
                mechanic.setLast_name(results.getString(COLUMN_LAST_NAME));
                mechanic.setHourly_payment(results.getInt(COLUMN_HOURLY_PAYMENT));

                mechanics.add(mechanic);
            }

            return mechanics;

        } catch (SQLException e) {
            System.out.println("Query for Mechanics failed: " + e.getMessage());
            return null;
        }
    }


    public static boolean insert(Mechanic mechanic, PreparedStatement insertStatement, PreparedStatement checkStatement) throws SQLException {
        String firstName = mechanic.getFirst_name();
        String patronymic = mechanic.getPatronymic();
        String lastName = mechanic.getLast_name();
        int hourlyPayment = mechanic.getHourly_payment();

        checkStatement.setLong(1, mechanic.getId());

        ResultSet results = checkStatement.executeQuery();

        if (results.next()) {
            System.out.println("Mechanic is already present in the table");
            return false;
        } else {

            insertStatement.setString(1, firstName);
            insertStatement.setString(2, patronymic);
            insertStatement.setString(3, lastName);
            insertStatement.setInt(4, hourlyPayment);
            int affectedRows = insertStatement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert mechanic!");
            }

            ResultSet generatedKeys = insertStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return true;
            } else {
                throw new SQLException("Couldn't get id for mechanic");
            }
        }
    }


    public static boolean delete(long id, PreparedStatement deleteStatement) throws SQLException {

        deleteStatement.setLong(1, id);

        int affectedRows = deleteStatement.executeUpdate();

        if (affectedRows != 1) {
            throw new SQLException("Couldn't delete mechanic!");
        }
        return true;
    }


    public static void update(Mechanic mechanic, PreparedStatement updateStatement) throws SQLException {
        String firstName = mechanic.getFirst_name();
        String patronymic = mechanic.getPatronymic();
        String lastName = mechanic.getLast_name();
        int hourlyPayment = mechanic.getHourly_payment();
        long id = mechanic.getId();

        updateStatement.setString(1, firstName);
        updateStatement.setString(2, patronymic);
        updateStatement.setString(3, lastName);
        updateStatement.setInt(4, hourlyPayment);
        updateStatement.setLong(5, id);
        int affectedRows = updateStatement.executeUpdate();

        if (affectedRows != 1) {
            throw new SQLException("Couldn't update mechanic!");
        }
    }

    public static int queryStats(long id, PreparedStatement queryStatement) {
        try {
            queryStatement.setLong(1, id);
            ResultSet results = queryStatement.executeQuery();
            results.next();
            return results.getInt("stats");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
