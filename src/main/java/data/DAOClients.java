package data;

import model.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOClients {
    public static final String TABLE_CLIENTS = "Clients";

    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_PATRONYMIC = "patronymic";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_ID = "id";

    public static final String QUERY_CLIENTS = "SELECT * FROM " + TABLE_CLIENTS;

    public static final String INSERT_CLIENT = "INSERT INTO " + TABLE_CLIENTS
            + " (" + COLUMN_FIRST_NAME + ", " + COLUMN_PATRONYMIC + ", "
            + COLUMN_LAST_NAME + ", " + COLUMN_PHONE_NUMBER + ") "
            + "VALUES(?, ?, ?, ?)";

    public static final String UPDATE_CLIENT = "UPDATE " + TABLE_CLIENTS
            + " SET " + COLUMN_FIRST_NAME + " = ?, " + COLUMN_PATRONYMIC + " = ?, "
            + COLUMN_LAST_NAME + " = ?, " + COLUMN_PHONE_NUMBER + " = ? WHERE " + COLUMN_ID + " = ?";

    public static final String DELETE_CLIENT = "DELETE FROM " + TABLE_CLIENTS +
            " WHERE " + COLUMN_ID + " = ?";

    public static final String CHECK_CLIENT_BY_NAME = QUERY_CLIENTS + " WHERE (" + COLUMN_FIRST_NAME
            + " = ?) AND (" + COLUMN_PATRONYMIC + " = ?) "
            + "AND (" + COLUMN_LAST_NAME + " = ?)";

    public static final String CHECK_CLIENT_BY_ID = QUERY_CLIENTS + " WHERE " + COLUMN_ID + " = ?";


    public static List<Client> query(PreparedStatement queryClients) {
        try {
            ResultSet results = queryClients.executeQuery();

            List<Client> clients = new ArrayList<>();

            while (results.next()) {
                Client client = new Client();
                client.setId(results.getInt(COLUMN_ID));
                client.setFirst_name(results.getString(COLUMN_FIRST_NAME));
                client.setPatronymic(results.getString(COLUMN_PATRONYMIC));
                client.setLast_name(results.getString(COLUMN_LAST_NAME));
                client.setPhoneNumber(results.getString(COLUMN_PHONE_NUMBER));

                clients.add(client);
            }

            return clients;

        } catch (SQLException e) {
            System.out.println("Query for Clients failed: " + e.getMessage());
            return null;
        }
    }


    public static boolean insert(Client client, PreparedStatement insertStatement, PreparedStatement checkStatement) throws SQLException {
        String firstName = client.getFirst_name();
        String patronymic = client.getPatronymic();
        String lastName = client.getLast_name();
        String phoneNumber = client.getPhoneNumber();

        checkStatement.setLong(1, client.getId());

        ResultSet results = checkStatement.executeQuery();

        if (results.next()) {
            System.out.println("Client is already present in the table");
            return false;
        } else {

            insertStatement.setString(1, firstName);
            insertStatement.setString(2, patronymic);
            insertStatement.setString(3, lastName);
            insertStatement.setString(4, phoneNumber);
            int affectedRows = insertStatement.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert client!");
            }

            ResultSet generatedKeys = insertStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                return true;
            } else {
                throw new SQLException("Couldn't get id for client");
            }
        }
    }


    public static boolean delete(long id, PreparedStatement deleteStatement) throws SQLException {

        deleteStatement.setLong(1, id);

        int affectedRows = deleteStatement.executeUpdate();

        if (affectedRows != 1) {
            throw new SQLException("Couldn't delete client!");
        }
        return true;
    }

    public static void update(long id, Client client, PreparedStatement updateStatement) throws SQLException {
        String firstName = client.getFirst_name();
        String patronymic = client.getPatronymic();
        String lastName = client.getLast_name();
        String phoneNumber = client.getPhoneNumber();

        updateStatement.setString(1, firstName);
        updateStatement.setString(2, patronymic);
        updateStatement.setString(3, lastName);
        updateStatement.setString(4, phoneNumber);
        updateStatement.setLong(5, id);
        int affectedRows = updateStatement.executeUpdate();

        if (affectedRows != 1) {
            throw new SQLException("Couldn't update client!");
        }
    }
}
