package my.vaadin;

import com.vaadin.data.Binder;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import data.DAOClass;
import data.DAOClients;
import model.Client;

import java.sql.SQLException;

public class ClientCRUD extends FormLayout implements CRUD {

    private TextField first_name = new TextField("Имя");
    private TextField last_name = new TextField("Фамилия");
    private TextField patronymic = new TextField("Отчество");
    private TextField phone_number = new TextField("Телефон");


    private DAOClass dataService = MyUI.getDataService();
    private Client client;
    private ClientLayout clientUI;

    private Binder<Client> binder = new Binder<>(Client.class);

    ClientCRUD() {

        binder.forField(phone_number)
                .withValidator(s -> s != null && !s.isEmpty(), "Значение поля не задано")
                .withValidator(s -> s.matches("^\\d+$"), "Номер телефона может содержать только цифры")
                .bind(Client::getPhoneNumber, Client::setPhoneNumber);

        binder.bindInstanceFields(this);

        addComponents(last_name, first_name, patronymic, phone_number);
    }

    public void setClientUI(ClientLayout clientUI) {
        this.clientUI = clientUI;
    }

    @Override
    public void setObject(Object object) {
        this.client = (Client) object;
        binder.setBean(client);

        last_name.selectAll();
    }

    @Override
    public void insertOrUpdate() {
        try {
            if (!DAOClients.insert(client, dataService.insertClient, dataService.checkClientId)) {
                DAOClients.update(client.getId(), client, dataService.updateClient);
            }
        } catch (SQLException e) {
            System.out.println("Insertion from the Form failed: " + e.getMessage());
        }
        clientUI.updateGrid();
    }

    @Override
    public void delete(Object client) {
        Client deletedClient = (Client) client;
        if (client != null) {
            try {
                DAOClients.delete(deletedClient.getId(), dataService.deleteClient);
            } catch (SQLException exc) {
                System.out.println("Deletion failed: " + exc.getMessage());
            }
            clientUI.updateGrid();
        }
    }
}