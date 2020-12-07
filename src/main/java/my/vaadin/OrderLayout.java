package my.vaadin;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import data.DAOClass;
import data.DAOOrders;
import model.Order;

public class OrderLayout extends BaseLayout {

    private DAOClass dataService = MyUI.getDataService();
    private Grid<Order> orderGrid = new Grid<>();

    private HorizontalLayout orderLayout;

    private TextField clientFilter = new TextField();
    private TextField descrFilter = new TextField();
    private NativeSelect<String> statusFilter;

    private OrderCRUD form = new OrderCRUD();

    OrderLayout() {

        form.setOrderUI(this);

        HorizontalLayout filters = new HorizontalLayout();

        clientFilter.setCaption("Фильтр по ID клиента:");
        clientFilter.setValueChangeMode(ValueChangeMode.EAGER);

        descrFilter.setCaption("Фильтр по описанию:");
        descrFilter.setValueChangeMode(ValueChangeMode.EAGER);

        statusFilter = new NativeSelect<>("Фильтр по статусу:");
        statusFilter.setItems("Запланирован", "Выполнен", "Принят клиентом");

        Button applyButton = new Button("Применить фильтры");
        applyButton.addClickListener(e -> applyFilters());

        filters.addComponents(clientFilter, descrFilter, statusFilter, applyButton);

        orderGrid.addColumn(Order::getId).setCaption("ID");
        orderGrid.addColumn(Order::getDescription).setCaption("Описание");
        orderGrid.addColumn(Order::getClient_id).setCaption("ID клиента");
        orderGrid.addColumn(Order::getMechanic_id).setCaption("ID механика");
        orderGrid.addColumn(Order::getCreation).setCaption("Дата создания");
        orderGrid.addColumn(Order::getFinish).setCaption("Дата окончания работ");
        orderGrid.addColumn(Order::getCost).setCaption("Стоимость");
        orderGrid.addColumn(Order::getStatusFromString).setCaption("Статус");

        orderGrid.setWidthFull();
        orderGrid.setWidth("80ex");

        orderGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        optionsLayout.addComponents(insertButton, deleteButton, updateButton);

        String size = "15ex";
        insertButton.setWidth(size);
        deleteButton.setWidth(size);
        updateButton.setWidth(size);

        insertButton.addClickListener(e -> {
            UI.getCurrent().addWindow(new SubWindow(form));
            form.setObject(new Order());
        });
        updateButton.addClickListener(e -> {
            Order order = orderGrid.asSingleSelect().getValue();
            if (order != null) {
                UI.getCurrent().addWindow(new SubWindow(form));
                form.setObject(order);
            }
        });
        deleteButton.addClickListener(e -> {
            Order order = orderGrid.asSingleSelect().getValue();
            if (order != null) {
                form.delete(order);
            }
        });

        orderLayout = new HorizontalLayout(orderGrid, optionsLayout);

        VerticalLayout layout = new VerticalLayout(filters, orderLayout);

        addComponents(layout);

        updateGrid();
    }

    private void applyFilters() {
        ListDataProvider<Order> dataProvider = (ListDataProvider<Order>) orderGrid.getDataProvider();
        dataProvider.addFilter(Order::getClient_id, this::idParser);
        dataProvider.addFilter(Order::getDescription, this::descrParser);
        dataProvider.addFilter(Order::getStatusFromString, this::statusParser);
        clientFilter.setComponentError(null);
    }

    private boolean idParser(Long s) {
        try {
            if ((clientFilter.getValue().trim().isEmpty()) || (clientFilter.getValue() == null)) {
                return true;
            } else {
                return s.equals(Long.parseLong(clientFilter.getValue().trim()));
            }
        } catch (NumberFormatException e) {
            clientFilter.setComponentError(new UserError("Значение не типа long"));
            return true;
        }
    }

    private boolean descrParser(String s) {
        if (descrFilter.getValue() == null) {
            return true;
        } else {
            return s.contains(descrFilter.getValue().trim());
        }
    }

    private boolean statusParser(String s) {
        if (statusFilter.getValue() == null) {
            return true;
        } else {
            return s.equals(statusFilter.getValue().trim());
        }
    }

    @Override
    void updateGrid() {
        orderGrid.setItems(DAOOrders.query(dataService.queryOrders));
    }

//    FILTER VIA DATABASE CONNECTION
//
//    private void updateList() {
//        List<Order> items = new ArrayList<>();
//        PreparedStatement filterStatement = null;
//        StringBuilder sb = new StringBuilder(DAOOrders.FILTER_ORDERS);
//
//        if (!clientFilter.isEmpty()) {
//            sb.append(DAOOrders.BY_CLIENT);
//        } else {
//            sb.append(clientFilter.getValue());
//            sb.append(" = ? AND ");
//        }
//        if (!descrFilter.isEmpty()) {
//            sb.append(DAOOrders.BY_DESCR);
//        } else {
//            sb.append(descrFilter.getValue());
//            sb.append(" = ? AND ");
//        }
//
//        if (statusFilter.getValue() != null) {
//            sb.append(DAOOrders.BY_STATUS);
//        } else {
//            sb.append(statusFilter.getValue());
//            sb.append(" = ? AND ");
//        }
//
//        sb.delete(sb.length() - 5, sb.length());
//
//        String FILTER_ORDERS = sb.toString();
//
//        try {
//            filterStatement = dataService.getConn().prepareStatement(FILTER_ORDERS);
//
//            filterStatement.setString(1, clientFilter.getValue());
//            filterStatement.setString(2, descrFilter.getValue());
//            filterStatement.setString(3, statusFilter.getValue());
//
//            ResultSet results = filterStatement.executeQuery();
//
//            items = DAOOrders.resultList(results);
//
//            filterStatement.close();
//
//        } catch (SQLException e) {
//            System.out.println("Filtering orders failed: " + e.getMessage());
//        }
//
//        orderGrid.setItems(items);
//    }

//    private void onClientFilterTextChange(HasValue.ValueChangeEvent<String> event) {
//        ListDataProvider<Order> dataProvider = (ListDataProvider<Order>) orderGrid.getDataProvider();
//        dataProvider.setFilter(Order::getClient_id, s -> s.equals(Long.parseLong(event.getValue())));
//    }
//
//    private void onDescrFilterTextChange(HasValue.ValueChangeEvent<String> event) {
//        ListDataProvider<Order> dataProvider = (ListDataProvider<Order>) orderGrid.getDataProvider();
//        dataProvider.setFilter(Order::getDescription, s -> s.equals(event.getValue()));
//    }
}
