package edu.ndsu.cs.estimate.pages;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

public class Login {
    @Property
    private String username;

    @Property
    private String password;

    @Property
    private boolean rememberMe;

    @InjectComponent
    private Form loginForm;

    @Inject
    private SecurityService securityService;

    @Inject
    private AlertManager alertManager;

    public Object onSuccessFromLoginForm() {
        Subject currentUser = securityService.getSubject();

        if (currentUser == null) {
            throw new IllegalStateException("Subject can’t be null");
        }

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(rememberMe);

        try {
            currentUser.login(token);
            return Index.class;
        } catch (AuthenticationException e) {
            loginForm.recordError("Invalid username or password.");
            return null;
        }
    }
}
