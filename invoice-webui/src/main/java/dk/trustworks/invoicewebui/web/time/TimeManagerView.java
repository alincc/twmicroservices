package dk.trustworks.invoicewebui.web.time;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontIcon;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.VerticalLayout;
import dk.trustworks.invoicewebui.model.RoleType;
import dk.trustworks.invoicewebui.security.AccessRules;
import dk.trustworks.invoicewebui.security.Authorizer;
import dk.trustworks.invoicewebui.web.mainmenu.components.MainTemplate;
import dk.trustworks.invoicewebui.web.mainmenu.components.TopMenu;
import dk.trustworks.invoicewebui.web.time.components.TimeManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.alump.materialicons.MaterialIcons;

import javax.annotation.PostConstruct;

/**
 * Created by hans on 16/08/2017.
 */
@AccessRules(roleTypes = {RoleType.USER})
@SpringView(name = TimeManagerView.VIEW_NAME)
public class TimeManagerView extends VerticalLayout implements View {

    protected static Logger logger = LoggerFactory.getLogger(TimeManagerView.class.getName());

    @Autowired
    private Authorizer authorizer;

    @Autowired
    private TopMenu topMenu;

    @Autowired
    private MainTemplate mainTemplate;

    @Autowired
    private TimeManagerImpl timeManager;

    //@Autowired
    //private TimeCardImpl timeCard;

    public static final String VIEW_NAME = "timeregistration";
    public static final String MENU_NAME = "Time Sheet";
    public static final String VIEW_BREADCRUMB = "TimeManager / Time Sheet";
    public static final FontIcon VIEW_ICON = MaterialIcons.ACCESS_TIME;

    @PostConstruct
    void init() {
        this.setMargin(false);
        this.setSpacing(false);
        this.addComponent(topMenu);
        this.addComponent(mainTemplate);
        mainTemplate.setMainContent(timeManager.init(), VIEW_ICON, MENU_NAME, "You are probably doing this late...", VIEW_BREADCRUMB);
        //mainTemplate.setMainContent(timeCard.init(), VIEW_ICON, MENU_NAME, "You are probably doing this late...", VIEW_BREADCRUMB);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        authorizer.authorize(this, RoleType.USER);
    }
}
