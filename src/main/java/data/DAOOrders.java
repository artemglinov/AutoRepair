package data;

import model.Order;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DAOOrders {
    public static final String TABLE_ORDERS = "Orders";

    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CLIENT_ID = "client_id";
    public static final String COLUMN_MECHANIC_ID = "mechanic_id";
    public static final String COLUMN_CREATION = "creation";
    public static final String COLUMN_FINISH = "finish";
    public static final String COLUMN_COST = "cost";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_ID = "id";

    public static final String QUERY_ORDERS = "SELECT * FROM " + TABLE_ORDERS;

    public static final String INSERT_ORDER = "INSERT INTO " + TABLE_ORDERS
            + " (" + COLUMN_DESCRIPTION + ", " + COLUMN_CLIENT_ID + ", "
            + COLUMN_MECHANIC_ID + ", " + COLUMN_CREATION + ", "
            + COLUMN_FINISH + ", " + COLUMN_COST + ", "
            + COLUMN_STATUS + ") VALUES(?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_ORDER = "UPDATE " + TABLE_ORDERS +
            " SET " + COLUMN_DESCRIPTION + " = ?, " + COLUMN_CLIENT_ID + " = ?, "
            + COLUMN_MECHANIC_ID + " = ?, " + COLUMN_CREATION + " = ?, "
            + COLUMN_FINISH + " = ?, " + COLUMN_COST + " = ?, "
            + COLUMN_STATUS + " = ? WHERE " + COLUMN_ID + " = ?";

    public static final String DELETE_ORDER = "DELETE FROM " + TABLE_ORDERS +
            " WHERE " + COLUMN_ID + " = ?";

//    public static final String FILTER_ORDERS = QUERY_ORDERS + " WHERE ";
//
//    public static final String BY_CLIENT = COLUMN_CLIENT_ID + " = ? AND ";
//
//    public static final String BY_STATUS = COLUMN_STATUS + " = ? AND ";
//
//    public static final String BY_DESCR = COLUMN_DESCRIPTION + " LIKE '%?%' AND ";


    public static final String CHECK_ORDER_BY_ID = QUERY_ORDERS + " WHERE " + COLUMN_ID + " = ?";

    public static List<Order> query(PreparedStatement queryStatement) {
        try {
            ResultSet results = queryStatement.executeQuery();

            List<Order> orders = new ArrayList<>();

            while (results.next()) {
                Order order = new Order();
                order.setId(results.getInt(COLUMN_ID));
                order.setDescription(results.getString(COLUMN_DESCRIPTION));
                order.setClient_id(results.getLong(COLUMN_CLIENT_ID));
                order.setMechanic_id(results.getLong(COLUMN_MECHANIC_ID));
                order.setCreation(results.getDate(COLUMN_CREATION) == null
                        ? null : results.getObject(COLUMN_CREATION, LocalDate.class));
                order.setFinish(results.getDate(COLUMN_FINISH) == null
                        ? null : results.getObject(COLUMN_FINISH, LocalDate.class));
                order.setCost(results.getDouble(COLUMN_COST));

                if (results.getString(COLUMN_STATUS) == null) {
                    order.setStatus(null);
                } else {
                    switch (results.getString(COLUMN_STATUS)) {
                        case ("Запланирован"):
                            order.setStatus(Order.Status.PLANNED);
                            break;
                        case ("Выполнен"):
                            order.setStatus(Order.Status.EXECUTED);
                            break;
                        case ("Принят клиентом"):
                            order.setStatus(Order.Status.ACCEPTED);
                            break;
                        default:
                            throw new SQLException("Illegal status of the Order");
                    }
                }

                orders.add(order);
            }

            return orders;

        } catch (SQLException e) {
            System.out.println("Query for Orders failed: " + e.getMessage());
            return null;
        }
    }


    public static boolean insert(Order order, PreparedStatement insertStatement, PreparedStatement checkStatement) throws SQLException {

        checkStatement.setLong(1, order.getId());

        ResultSet results = checkStatement.executeQuery();

        if (results.next()) {
            System.out.println("Order is already present in the table");
            return false;
        } else {
            String description = order.getDescription();
            long client_id = order.getClient_id();
            long mechanic_id = order.getMechanic_id();
            LocalDate creation = order.getCreation();
            LocalDate finish = order.getFinish();
            double cost = order.getCost();
            Order.Status status = order.getStatus();

            insertStatement.setString(1, description);
            insertStatement.setLong(2, client_id);
            insertStatement.setLong(3, mechanic_id);
            if (creation != null) {
                insertStatement.setDate(4, Date.valueOf(creation));
            } else {
                insertStatement.setNull(4, Types.DATE);
            }
            if (finish != null) {
                insertStatement.setDate(5, Date.valueOf(finish));
            } else {
                insertStatement.setNull(5, Types.DATE);
            }
            insertStatement.setDouble(6, cost);
            if (status == null) {
                insertStatement.setString(7, null);
            } else {
                switch (status) {
                    case PLANNED:
                        insertStatement.setString(7, "Запланирован");
                        break;
                    case ACCEPTED:
                        insertStatement.setString(7, "Принят клиентом");
                        break;
                    case EXECUTED:
                        insertStatement.setString(7, "Выполнен");
                        break;
                    default:
                        insertStatement.setString(7, null);
                }
            }
            int affectedRows = insertStatement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert order!");
            }

            ResultSet generatedKeys = insertStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return true;
            } else {
                throw new SQLException("Couldn't get id for order");
            }
        }
    }


    public static boolean delete(long id, PreparedStatement deleteStatement) throws SQLException {

        deleteStatement.setLong(1, id);

        int affectedRows = deleteStatement.executeUpdate();

        if (affectedRows != 1) {
            throw new SQLException("Couldn't delete order!");
        }
        return true;
    }

    public static void update(Order order, PreparedStatement updateStatement) throws SQLException {
        String description = order.getDescription();
        long client_id = order.getClient_id();
        long mechanic_id = order.getMechanic_id();
        double cost = order.getCost();
        LocalDate creation = order.getCreation();
        LocalDate finish = order.getFinish();
        Order.Status status = order.getStatus();
        long id = order.getId();

        updateStatement.setString(1, description);
        updateStatement.setLong(2, client_id);
        updateStatement.setLong(3, mechanic_id);
        if (creation != null) {
            updateStatement.setDate(4, Date.valueOf(creation));
        } else {
            updateStatement.setNull(4, Types.DATE);
        }
        if (finish != null) {
            updateStatement.setDate(5, Date.valueOf(finish));
        } else {
            updateStatement.setNull(5, Types.DATE);
        }
        updateStatement.setDouble(6, cost);
        if (status == null) {
            updateStatement.setString(7, null);
        } else {
            switch (status) {
                case PLANNED:
                    updateStatement.setString(7, "Запланирован");
                    break;
                case ACCEPTED:
                    updateStatement.setString(7, "Принят клиентом");
                    break;
                case EXECUTED:
                    updateStatement.setString(7, "Выполнен");
                    break;
                default:
                    updateStatement.setString(7, null);
            }
        }
        updateStatement.setLong(8, id);
        int affectedRows = updateStatement.executeUpdate();

        if (affectedRows != 1) {
            throw new SQLException("Couldn't update order!");
        }
    }

//    public static List<Order> filterByStatus(String s, PreparedStatement filterStatement){
//        ResultSet results = null;
//        try {
//
//            filterStatement.setString(1, s);
//
//            results = filterStatement.executeQuery();
//        } catch (SQLException e) {
//            System.out.println("filterStatement fail: " + e.getMessage());
//        }
//
//        return resultList(results);
//    }

}
