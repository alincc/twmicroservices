package dk.trustworks.invoicewebui.web.profile.components;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.DateField;

/**
 * !! DO NOT EDIT THIS FILE !!
 * <p>
 * This class is generated by Vaadin Designer and will be overwritten.
 * <p>
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class UserDetailsCardDesign extends VerticalLayout {
    private VerticalLayout cardHolder;
    private Label lblTitle;
    private VerticalLayout container;
    private FormLayout formLayout;
    private TextField txtName;
    private DateField dfBirthday;
    private PasswordField txtPassword;
    private PasswordField txtPasswordRepeat;

    public UserDetailsCardDesign() {
        Design.read(this);
    }

    public VerticalLayout getCardHolder() {
        return cardHolder;
    }

    public Label getLblTitle() {
        return lblTitle;
    }

    public VerticalLayout getContainer() {
        return container;
    }

    public FormLayout getFormLayout() {
        return formLayout;
    }

    public TextField getTxtName() {
        return txtName;
    }

    public DateField getDfBirthday() {
        return dfBirthday;
    }

    public PasswordField getTxtPassword() {
        return txtPassword;
    }

    public PasswordField getTxtPasswordRepeat() {
        return txtPasswordRepeat;
    }

}
