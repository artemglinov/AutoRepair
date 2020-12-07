package my.vaadin;


import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import data.DAOClass;
import data.DAOClients;
import model.Client;

public class ClientLayout extends BaseLayout {

    private DAOClass dataService = MyUI.getDataService();
    private Grid<Client> clientGrid = new Grid<>();

    private ClientCRUD form = new ClientCRUD();

    ClientLayout() {

        form.setClientUI(this);

        clientGrid.addColumn(Client::getId).setCaption("ID");
        clientGrid.addColumn(Client::getLast_name).setCaption("Фамилия");
        clientGrid.addColumn(Client::getFirst_name).setCaption("Имя");
        clientGrid.addColumn(Client::getPatronymic).setCaption("Отчество");
        clientGrid.addColumn(Client::getPhoneNumber).setCaption("Номер телефона");

        clientGrid.setWidthFull();
        clientGrid.setWidth("80ex");

        clientGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        optionsLayout.addComponents(insertButton, deleteButton, updateButton);

        String size = "15ex";
        insertButton.setWidth(size);
        deleteButton.setWidth(size);
        updateButton.setWidth(size);

        insertButton.addClickListener(e -> {
            UI.getCurrent().addWindow(new SubWindow(form));
            form.setObject(new Client());
        });

        updateButton.addClickListener(e -> {
            Client client = clientGrid.asSingleSelect().getValue();

            if (client != null) {
                UI.getCurrent().addWindow(new SubWindow(form));
                form.setObject(client);
            }
        });

        deleteButton.addClickListener(e -> {
            Client client = clientGrid.asSingleSelect().getValue();
            if (client != null) {
                form.delete(client);
            }
        });


        addComponents(clientGrid, optionsLayout);

        updateGrid();
    }

    @Override
    void updateGrid() {
        clientGrid.setItems(DAOClients.query(dataService.queryClients));
    }
}


