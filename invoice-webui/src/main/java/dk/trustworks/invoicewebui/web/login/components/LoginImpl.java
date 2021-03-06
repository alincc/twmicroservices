package dk.trustworks.invoicewebui.web.login.components;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import dk.trustworks.invoicewebui.model.User;
import dk.trustworks.invoicewebui.network.clients.LoginClient;
import dk.trustworks.invoicewebui.web.contexts.UserSession;
import io.sentry.Sentry;
import io.sentry.event.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;

/**
 * Created by hans on 12/08/2017.
 */
@SpringComponent
@SpringUI
public class LoginImpl extends LoginDesign {

    private static String NAME_COOKIE = "trustworks_login";

    @Autowired
    public LoginImpl(LoginClient loginClient) {
        System.out.println("LoginImpl.LoginImpl");

        getImgTop().setSource(new ThemeResource("images/password-card.jpg"));
        getBtnLogin().addClickListener(clickEvent -> {
            User user = loginClient.login(getTxtUsername().getValue(), getTxtPassword().getValue());
            if(user == null) {
                Notification.show("Login failed",
                        "Wrong username or password!",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }
            if(user.getRoleList().size()==0) {
                Notification.show("Login failed",
                        "No valid roles!",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }
            UserSession userSession = new UserSession(user, user.getRoleList());
            Sentry.getContext().setUser(
                    new UserBuilder().setId(user.getUuid()).setUsername(user.getUsername()).setEmail(user.getEmail()).build()
            );
            VaadinSession.getCurrent().setAttribute(UserSession.class, userSession);
            Cookie newCookie = new Cookie(NAME_COOKIE, user.getUuid());
            newCookie.setMaxAge(2592000);
            newCookie.setPath(VaadinService.getCurrentRequest() .getContextPath());
            VaadinService.getCurrentResponse().addCookie(newCookie);
            getUI().getNavigator().navigateTo("mainmenu");
        });
        getBtnLogin().setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }




}
