package dk.trustworks.invoicewebui.web.dashboard.cards;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;

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
public class NewsDesign extends VerticalLayout {
    private VerticalLayout cardHolder;
    private Image imgTop;
    private VerticalLayout container;
    private Label lblHeading;
    private Panel panelContentHolder;
    private GridLayout eventGrid;
    private Label lblBirthdayGreeting;

    public NewsDesign() {
        Design.read(this);
    }

    public VerticalLayout getCardHolder() {
        return cardHolder;
    }

    public Image getImgTop() {
        return imgTop;
    }

    public VerticalLayout getContainer() {
        return container;
    }

    public Label getLblHeading() {
        return lblHeading;
    }

    public Panel getPanelContentHolder() {
        return panelContentHolder;
    }

    public GridLayout getEventGrid() {
        return eventGrid;
    }

    public Label getLblBirthdayGreeting() {
        return lblBirthdayGreeting;
    }

}
