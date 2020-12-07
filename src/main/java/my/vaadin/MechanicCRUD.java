package my.vaadin;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import data.DAOClass;
import data.DAOMechanics;
import model.Mechanic;

import java.sql.SQLException;

public class MechanicCRUD extends FormLayout implements CRUD {
    private TextField first_name = new TextField("Имя");
    private TextField last_name = new TextField("Фамилия");
    private TextField patronymic = new TextField("Отчество");
    private TextField hourly_payment = new TextField("Почасовая оплата");

    private static final DAOClass dataService = MyUI.getDataService();
    private Mechanic mechanic;
    private MechanicLayout mechanicUI;
    private Binder<Mechanic> binder = new Binder<>(Mechanic.class);

    MechanicCRUD(){
//            this.dataService = myUI.getDataService();

//        FormLayout form = new FormLayout();

//        setSizeUndefined();

//        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        binder.forField(hourly_payment)
//                .withValidator(s -> s != null && !s.isEmpty(), "Не указана почасовая оплата")
                .withConverter(new StringToIntegerConverter("Почасовая оплата должна быть целым числом"))
                .withValidator(integer -> integer >= 0, "Почасовая оплата не может быть отрицательной")
                .bind(Mechanic::getHourly_payment, Mechanic::setHourly_payment);


        binder.bindInstanceFields(this);

        addComponents(last_name, first_name, patronymic, hourly_payment);

//        setContent(form);

//        layout = new VerticalLayout(form, buttons);

//        okButton.addClickListener(e -> this.insertOrUpdate());
//        cancelButton.addClickListener(e -> this.cancel());
    }

    public void setMechanicUI(MechanicLayout mechanicUI) {
        this.mechanicUI = mechanicUI;
    }

    @Override
    public void setObject(Object object){
        this.mechanic = (Mechanic) object;
        binder.setBean(mechanic);

        setVisible(true);
        last_name.selectAll();
    }

    @Override
    public void insertOrUpdate(){
        try {
            if (!DAOMechanics.insert(mechanic, dataService.insertMechanic, dataService.checkMechanicId)) {
                DAOMechanics.update(mechanic, dataService.updateMechanic);
            }
        } catch (SQLException e) {
            System.out.println("Insertion from the Form failed: " + e.getMessage());
        }
        mechanicUI.updateGrid();
//        setVisible(false);
    }

    @Override
    public void delete(Object obj){
        this.mechanic = (Mechanic) obj;
        if (mechanic != null) {
            try {
                DAOMechanics.delete(mechanic.getId(), dataService.deleteMechanic);
            } catch (SQLException exc) {
                System.out.println("Deletion failed: " + exc.getMessage());
            }
            mechanicUI.updateGrid();
        }
    }

//    private int intParser(){
//        if ((hourly_payment_field.getValue().trim().isEmpty()) || (hourly_payment_field.getValue() == null)) {
//            return 0;
//        } else {
//            return Integer.parseInt(hourly_payment_field.getValue());
//        }
//    }

}
