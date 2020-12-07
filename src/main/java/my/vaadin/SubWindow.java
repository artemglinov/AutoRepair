package my.vaadin;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

class SubWindow extends Window {
    private Button okButton = new Button("OK");
    private Button cancelButton = new Button("Отменить");
    private ClientCRUD form = new ClientCRUD();
    private VerticalLayout windowContent;

    SubWindow(CRUD form) {
        super("Форма для внесения/изменения данных");

        HorizontalLayout buttons = new HorizontalLayout(okButton, cancelButton);

        okButton.setStyleName(ValoTheme.BUTTON_PRIMARY);

        windowContent = new VerticalLayout(form, buttons);

        windowContent.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
        windowContent.setComponentAlignment(buttons, Alignment.BOTTOM_CENTER);

        okButton.addClickListener(e -> {
            form.insertOrUpdate();
            close();
        });
        cancelButton.addClickListener(e -> close());

        setHeight("520px");
        setWidth("500px");
        setContent(windowContent);
        setModal(true);
        center();
    }
}
