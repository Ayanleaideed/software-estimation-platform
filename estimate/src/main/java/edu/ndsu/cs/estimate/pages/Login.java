package edu.ndsu.cs.estimate.pages;

import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;
import edu.ndsu.cs.estimate.services.database.interfaces.UserAccountDatabaseService;

public class Login {
    @Property
    private String usernameOrEmail;

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

    @Inject
    private UserAccountDatabaseService userAccountDatabaseService;

    // Validate form inputs
    void onValidateFromLoginForm() {
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
            loginForm.recordError("Username or Email cannot be empty.");
        }

        if (password == null || password.trim().isEmpty()) {
            loginForm.recordError("Password cannot be empty.");
        }
    }

    // Handle form submission
    public Object onSuccessFromLoginForm() {
        Subject currentUser = securityService.getSubject();

        if (currentUser == null) {
            throw new IllegalStateException("Subject can't be null");
        }

        // Attempt to find the user by username
        UserAccount account = userAccountDatabaseService.getUserAccount(usernameOrEmail);
        if (account == null) {
            // If not found by username, attempt to find by email
            account = userAccountDatabaseService.getUserAccountByEmail(usernameOrEmail);
        }

        if (account == null) {
            loginForm.recordError("Invalid username/email or password.");
            return null;
        }

        // Create a token with the username and password
        UsernamePasswordToken token = new UsernamePasswordToken(account.getUserName(), password);
        token.setRememberMe(rememberMe);

        try {
            // Attempt to log in
            currentUser.login(token);
            return Index.class; // Redirect to the home page upon successful login
        } catch (AuthenticationException e) {
            // Handle login failure
            loginForm.recordError("Invalid username/email or password.");
            return null;
        }
    }
}
