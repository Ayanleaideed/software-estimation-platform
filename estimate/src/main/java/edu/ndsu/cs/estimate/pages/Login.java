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
    private String username; // Stores the username entered by the user

    @Property
    private String password; // Stores the password entered by the user

    @Property
    private boolean rememberMe; // Stores the "Remember me" option selected by the user

    @InjectComponent
    private Form loginForm; // Form component for managing the login form and its validation

    @Inject
    private SecurityService securityService; // Service for managing security and authentication

    @Inject
    private AlertManager alertManager; // Manages alert messages to be shown to the user

    // Validates the form fields before submission
    void onValidateFromLoginForm() {
        // Check if the username is empty and record an error if it is
        if (username == null || username.trim().isEmpty()) {
            loginForm.recordError("Username cannot be empty.");
        }

        // Check if the password is empty and record an error if it is
        if (password == null || password.trim().isEmpty()) {
            loginForm.recordError("Password cannot be empty.");
        }
    }

    // Method called upon successful form submission
    public Object onSuccessFromLoginForm() {
        Subject currentUser = securityService.getSubject(); // Get the current user (subject) for authentication

        // Ensure the subject is not null (a safeguard check)
        if (currentUser == null) {
            throw new IllegalStateException("Subject can’t be null");
        }

        // Create a token with the entered username and password for authentication
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(rememberMe); // Set the rememberMe property on the token

        try {
            // Attempt to log in with the provided credentials
            currentUser.login(token);
            return Index.class; // Redirect to the Index page on successful login
        } catch (AuthenticationException e) {
            // Catch authentication errors and show an error message if login fails
            loginForm.recordError("Invalid username or password.");
            return null; // Stay on the login page if authentication fails
        }
    }
}
