package my.vaadin;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;


abstract class BaseLayout extends HorizontalLayout {
    Button insertButton = new Button("Добавить");
    Button deleteButton = new Button("Удалить");
    Button updateButton = new Button("Изменить");
    VerticalLayout optionsLayout = new VerticalLayout(insertButton, updateButton, deleteButton);

    abstract void updateGrid();
}
