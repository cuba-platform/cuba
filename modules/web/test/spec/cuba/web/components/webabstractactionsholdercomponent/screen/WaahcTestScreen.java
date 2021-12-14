package spec.cuba.web.components.webabstractactionsholdercomponent.screen;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import javax.inject.Named;

@UiController
@UiDescriptor("waahc-test-screen.xml")
public class WaahcTestScreen extends Screen {
    @Inject
    public Table<User> table;

    @Named("table.test")
    public Action test;
}
