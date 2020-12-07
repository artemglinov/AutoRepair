package my.vaadin;

import com.vaadin.ui.Component;

interface CRUD extends Component {
    void setObject(Object obj);

    void insertOrUpdate();
    void delete(Object obj);
}
