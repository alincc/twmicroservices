package dk.trustworks.invoicewebui.web.faq.components;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.Button;
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
public class FaqCardDesign extends VerticalLayout {
    private Label lblTitle;
    private TextField txtTitle;
    private Button btnEdit;
    private Panel panel;
    private VerticalLayout cardHolder;
    private Label lblContent;
    private RichTextArea txtContent;
    private Button btnSave;
    private Button btnDelete;

    public FaqCardDesign() {
        Design.read(this);
    }

    public Label getLblTitle() {
        return lblTitle;
    }

    public TextField getTxtTitle() {
        return txtTitle;
    }

    public Button getBtnEdit() {
        return btnEdit;
    }

    public Panel getPanel() {
        return panel;
    }

    public VerticalLayout getCardHolder() {
        return cardHolder;
    }

    public Label getLblContent() {
        return lblContent;
    }

    public RichTextArea getTxtContent() {
        return txtContent;
    }

    public Button getBtnSave() {
        return btnSave;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

}
