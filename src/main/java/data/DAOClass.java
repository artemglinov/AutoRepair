package data;

import java.sql.*;

public class DAOClass {
    public static final String DB_NAME = "database";

    /*
         Здесь должен быть указан путь к БД. В моём случае это E:\JavaProjects\StudentsTable\src\students_database.db.
     */

    public static final String CONNECTION_STRING = "jdbc:hsqldb:file:E:\\SpringProjects\\AutoRepair\\Database\\";

    private Connection conn;

    public PreparedStatement queryClients;
    public PreparedStatement insertClient;
    public PreparedStatement deleteClient;
    public PreparedStatement updateClient;
    public PreparedStatement checkClientId;

    public PreparedStatement queryMechanicsStats;
    public PreparedStatement queryMechanics;
    public PreparedStatement insertMechanic;
    public PreparedStatement deleteMechanic;
    public PreparedStatement updateMechanic;
    public PreparedStatement checkMechanicId;

    public PreparedStatement queryOrders;
    public PreparedStatement insertOrder;
    public PreparedStatement deleteOrder;
    public PreparedStatement updateOrder;
    //    public PreparedStatement filterByClient;
//    public PreparedStatement filterByStatus;
//    public PreparedStatement filterByDescr;
    public PreparedStatement checkOrderId;


    private static DAOClass instance = new DAOClass();

    DAOClass() {
    }

    public static DAOClass getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING, "SA", "");

            // Блоки Prepared Statements для клиентов, автомехаников и заказов.

            queryClients = conn.prepareStatement(DAOClients.QUERY_CLIENTS);
            insertClient = conn.prepareStatement(DAOClients.INSERT_CLIENT, Statement.RETURN_GENERATED_KEYS);
            deleteClient = conn.prepareStatement(DAOClients.DELETE_CLIENT);
            updateClient = conn.prepareStatement(DAOClients.UPDATE_CLIENT);
            checkClientId = conn.prepareStatement(DAOClients.CHECK_CLIENT_BY_ID);

            queryMechanics = conn.prepareStatement(DAOMechanics.QUERY_MECHANICS);
            queryMechanicsStats = conn.prepareStatement(DAOMechanics.QUERY_MECHANICS_STATS);
            insertMechanic = conn.prepareStatement(DAOMechanics.INSERT_MECHANIC, Statement.RETURN_GENERATED_KEYS);
            deleteMechanic = conn.prepareStatement(DAOMechanics.DELETE_MECHANIC);
            updateMechanic = conn.prepareStatement(DAOMechanics.UPDATE_MECHANIC);
            checkMechanicId = conn.prepareStatement(DAOMechanics.CHECK_MECHANIC_BY_ID);

            queryOrders = conn.prepareStatement(DAOOrders.QUERY_ORDERS);
            insertOrder = conn.prepareStatement(DAOOrders.INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            deleteOrder = conn.prepareStatement(DAOOrders.DELETE_ORDER);
            updateOrder = conn.prepareStatement(DAOOrders.UPDATE_ORDER);
            checkOrderId = conn.prepareStatement(DAOOrders.CHECK_ORDER_BY_ID);
//            filterByClient = conn.prepareStatement(DAOOrders.BY_CLIENT);
//            filterByStatus = conn.prepareStatement(DAOOrders.BY_STATUS);
//            filterByDescr = conn.prepareStatement(DAOOrders.BY_DESCR);

            return true;

        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

/* METHOD CLOSING RESOURCES

    public void close() {
        try {
            if (queryClients != null) {
                queryClients.close();
            }


            if (queryMechanicsStats != null) {
                queryMechanicsStats.close();
            }

            if (queryMechanics != null) {
                queryMechanics.close();
            }

            if (queryOrders != null) {
                queryOrders.close();
            }

            if (insertClient != null) {
                insertClient.close();
            }

            if (insertMechanic != null) {
                insertMechanic.close();
            }

            if (insertOrder != null) {
                insertOrder.close();
            }

            if (deleteClient != null) {
                deleteClient.close();
            }

            if (deleteMechanic != null) {
                deleteMechanic.close();
            }

            if (deleteOrder != null) {
                deleteOrder.close();
            }

            if (updateClient != null) {
                updateClient.close();
            }

            if (updateMechanic != null) {
                updateMechanic.close();
            }

            if (updateOrder != null) {
                updateOrder.close();
            }

//            if (checkClientName != null){
//                checkClientName.close();
//            }
//
//            if (checkMechanicName != null){
//                checkMechanicName.close();
//            }
//
//            if (filterByClient != null){
//                filterByClient.close();
//            }
//
//            if (filterByStatus != null){
//                filterByStatus.close();
//            }
//
//            if (filterByDescr != null){
//                filterByDescr.close();
//            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

 */
}
