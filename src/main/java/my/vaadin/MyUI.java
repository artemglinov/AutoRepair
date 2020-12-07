package my.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import data.DAOClass;

import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    //    private Grid<Client> clientGrid = new Grid<>();
    private static final DAOClass dataService = DAOClass.getInstance();
    private HorizontalLayout typeButtons;
    private BaseLayout currentUI;
    VerticalLayout layout = new VerticalLayout();
    private ClientLayout clientUI;
    private MechanicLayout mechanicUI;
    private OrderLayout orderUI;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        Button clientButton = new Button("Клиенты");
        Button mechanicButton = new Button("Автомеханики");
        Button orderButton = new Button("Заказы");

        typeButtons = new HorizontalLayout(clientButton, mechanicButton, orderButton);

        dataService.open();

        clientUI = new ClientLayout();
        mechanicUI = new MechanicLayout();
        orderUI = new OrderLayout();

        clientButton.addClickListener(e -> makeVisible(clientUI));
        mechanicButton.addClickListener(e -> makeVisible(mechanicUI));
        orderButton.addClickListener(e -> makeVisible(orderUI));

        layout.addComponents(typeButtons, clientUI);

        layout.setComponentAlignment(typeButtons, Alignment.TOP_CENTER);

        currentUI = clientUI;

        setContent(layout);
    }

    public static DAOClass getDataService() {
        return dataService;
    }

    private void makeVisible(BaseLayout ui) {
        layout.removeComponent(currentUI);
        layout.addComponent(ui);
        currentUI = ui;
        setContent(layout);
    }


    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
