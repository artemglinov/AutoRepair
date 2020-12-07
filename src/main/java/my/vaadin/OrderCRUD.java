package my.vaadin;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.server.SerializableFunction;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import data.DAOClass;
import data.DAOOrders;
import model.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderCRUD extends FormLayout implements CRUD {

    private TextField description = new TextField("Описание");
    private TextField client_id = new TextField("Клиент");
    private TextField mechanic_id = new TextField("Механик");
    private DateField creation = new DateField("Дата создания");
    private DateField finish = new DateField("Дата окончания работ");
    private TextField cost = new TextField("Стоимость");
    private NativeSelect<String> status = new NativeSelect<>("Статус");

    private DAOClass dataService = MyUI.getDataService();
    private Order order;
    private OrderLayout orderUI;
    private MyUI myUI;
    private Binder<Order> binder = new Binder<>(Order.class);

    OrderCRUD() {
        setSizeUndefined();

        addComponents(description, client_id, mechanic_id, creation, finish, cost, status);

        status.setItems("Запланирован", "Выполнен", "Принят клиентом");

        binder.forField(cost)
                .withConverter(new StringToDoubleConverter("Стоимость должна быть целым числом"))
                .withValidator(integer -> integer >= 0, "Стоимость не может быть отрицательной")
                .bind(Order::getCost, Order::setCost);

        binder.forField(client_id)
                .withValidator(s -> s != null && !s.isEmpty(), "Не указан ID клиента")
                .withConverter(new StringToLongConverter("ID клиента не типа long"))
                .withValidator(id -> id > 0, "Неположительные значения ID недопустимы")
                .withValidator(id -> checkPerson(id, "Client"), "Клиент не найден в базе данных")
                .bind(Order::getClient_id, Order::setClient_id);

        binder.forField(mechanic_id)
                .withValidator(s -> s != null && !s.isEmpty(), "Не указан ID механика")
                .withConverter(new StringToLongConverter("ID механика не типа long"))
                .withValidator(id -> id > 0, "Неположительные значения ID недопустимы")
                .withValidator(id -> checkPerson(id, "Mechanic"), "Механик не найден в базе данных")
                .bind(Order::getMechanic_id, Order::setMechanic_id);

        binder.forField(status)
                .withConverter((SerializableFunction<String, Order.Status>) Order::stringToStatus,
                        (SerializableFunction<Order.Status, String>) Order::statusToString,
                        "Неконвертируемый статус")
                .bind(Order::getStatus, Order::setStatus);

        binder.forField(finish)
                .withValidator(s -> {
                    if (s == null) {
                        return true;
                    } else if (creation.getValue() != null && !creation.isEmpty()) {
                        return s.compareTo(creation.getValue()) >= 0;
                    }
                    return true;
                }, "Дата окончания работ не может быть раньше даты создания")
                .bind(Order::getFinish, Order::setFinish);

        binder.forField(creation)
                .withValidator(s -> {
                    if (s == null) {
                        return true;
                    } else if (finish.getValue() != null && !finish.isEmpty()) {
                        return s.compareTo(finish.getValue()) <= 0;
                    }
                    return true;
                }, "Дата создания не может быть позже даты окончания работ")
                .bind(Order::getCreation, Order::setCreation);

        binder.bindInstanceFields(this);
    }

    public void setOrderUI(OrderLayout orderUI) {
        this.orderUI = orderUI;
    }

    @Override
    public void setObject(Object object) {
        this.order = (Order) object;
        binder.setBean(order);

        description.selectAll();
    }

    @Override
    public void insertOrUpdate() {
        try {
            if (!DAOOrders.insert(order, dataService.insertOrder, dataService.checkOrderId)) {
                DAOOrders.update(order, dataService.updateOrder);
            }
        } catch (SQLException e) {
            System.out.println("Insertion from the Form failed: " + e.getMessage());
        }
        orderUI.updateGrid();
    }

    @Override
    public void delete(Object obj) {
        Order deletedOrder = (Order) obj;
        if (deletedOrder != null) {
            try {
                DAOOrders.delete(deletedOrder.getId(), myUI.getDataService().deleteOrder);
            } catch (SQLException exc) {
                System.out.println("Deletion failed: " + exc.getMessage());
            }
            orderUI.updateGrid();
        }
    }

    private boolean checkPerson(long id, String type) {
        if (type.equals("Client")) {
            PreparedStatement checkStatement = dataService.checkClientId;

            try {
                checkStatement.setLong(1, id);

                ResultSet results = checkStatement.executeQuery();

                return results.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (type.equals("Mechanic")) {
            PreparedStatement checkStatement = dataService.checkMechanicId;

            try {
                checkStatement.setLong(1, id);

                ResultSet results = checkStatement.executeQuery();

                return results.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
