package dk.trustworks.invoicewebui.web.dashboard.cards;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Button;

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
public class CateringCardDesign extends VerticalLayout {
    private VerticalLayout cardHolder;
    private Image imgTop;
    private Label lblHeading;
    private TextField meetingName;
    private ComboBox<dk.trustworks.invoicewebui.model.User> contact;
    private DateTimeField start;
    private DateTimeField end;
    private TextField people;
    private CheckBoxGroup<String> orderTypes;
    private TextArea details;
    private ComboBox<String> quality;
    private ComboBox<String> account;
    private Button btnOrder;

    public CateringCardDesign() {
        Design.read(this);
    }

    public VerticalLayout getCardHolder() {
        return cardHolder;
    }

    public Image getImgTop() {
        return imgTop;
    }

    public Label getLblHeading() {
        return lblHeading;
    }

    public TextField getMeetingName() {
        return meetingName;
    }

    public ComboBox<dk.trustworks.invoicewebui.model.User> getContact() {
        return contact;
    }

    public DateTimeField getStart() {
        return start;
    }

    public DateTimeField getEnd() {
        return end;
    }

    public TextField getPeople() {
        return people;
    }

    public CheckBoxGroup<String> getOrderTypes() {
        return orderTypes;
    }

    public TextArea getDetails() {
        return details;
    }

    public ComboBox<String> getQuality() {
        return quality;
    }

    public ComboBox<String> getAccount() {
        return account;
    }

    public Button getBtnOrder() {
        return btnOrder;
    }

}