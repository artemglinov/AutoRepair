package my.vaadin;

import com.vaadin.ui.*;
import data.DAOClass;
import data.DAOMechanics;
import model.Mechanic;

public class MechanicLayout extends BaseLayout {

    private DAOClass dataService = MyUI.getDataService();
    private Grid<Mechanic> mechanicGrid = new Grid<>();

    private MechanicCRUD form = new MechanicCRUD();

    private Button statsButton = new Button("Показать статистику");

    MechanicLayout(){

        form.setMechanicUI(this);

        mechanicGrid.addColumn(Mechanic::getId).setCaption("ID");
        mechanicGrid.addColumn(Mechanic::getLast_name).setCaption("Фамилия");
        mechanicGrid.addColumn(Mechanic::getFirst_name).setCaption("Имя");
        mechanicGrid.addColumn(Mechanic::getPatronymic).setCaption("Отчество");
        mechanicGrid.addColumn(Mechanic::getHourly_payment).setCaption("Почасовая оплата");

        mechanicGrid.setWidthFull();
        mechanicGrid.setWidth("80ex");

        mechanicGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        optionsLayout.addComponents(insertButton, deleteButton, updateButton, statsButton);

        String size = "21ex";
        insertButton.setWidth(size);
        deleteButton.setWidth(size);
        updateButton.setWidth(size);
        statsButton.setWidth(size);

        insertButton.addClickListener(e -> {
            UI.getCurrent().addWindow(new SubWindow(form));
            form.setObject(new Mechanic());
        });
        updateButton.addClickListener(e -> {
            Mechanic mechanic = mechanicGrid.asSingleSelect().getValue();
            if (mechanic != null) {
                UI.getCurrent().addWindow(new SubWindow(form));
                form.setObject(mechanic);
            }
        });
        deleteButton.addClickListener(e -> {
            Mechanic mechanic = mechanicGrid.asSingleSelect().getValue();
            if (mechanic != null) {
                form.delete(mechanic);
            }
        });

        updateGrid();

        statsButton.addClickListener(e -> showStats());

        addComponents(mechanicGrid, optionsLayout);

        updateGrid();

    }

    void showStats(){
        if (mechanicGrid.getColumn("stats") == null) {
            mechanicGrid.addColumn(s -> DAOMechanics.queryStats(s.getId(), dataService.queryMechanicsStats))
                    .setId("stats").setCaption("Количество заказов");
        }
    }

    @Override
    void updateGrid(){
        if (mechanicGrid.getColumn("stats") != null) {
            mechanicGrid.removeColumn("stats");
        }
        mechanicGrid.setItems(DAOMechanics.query(dataService.queryMechanics));
    }

}