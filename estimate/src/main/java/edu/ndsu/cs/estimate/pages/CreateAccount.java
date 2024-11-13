package edu.ndsu.cs.estimate.pages;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import java.util.List;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.beanmodel.BeanModel;
import org.apache.tapestry5.beanmodel.services.BeanModelSource;
import org.apache.tapestry5.commons.Messages;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;
import edu.ndsu.cs.estimate.entities.interfaces.RoleInterface;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.database.interfaces.UserAccountDatabaseService;

public class CreateAccount {

    @Property
    private String tempEmail; // Temporary email field to collect email during account creation

    @Inject
    private SecurityService securityService; // Service to manage authentication and security

    @Inject
    private UserAccountDatabaseService userAccountDatabaseService; // Service to manage user accounts in the database

    @Inject
    private AlertManager alertManager; // Service to manage alert messages

    @Property
    @Persist
    private UserAccount userAccount; // UserAccount object that will hold user account data

    @Property
    private String userName; // Stores the username entered by the user

    @Property
    private String passWord; // Stores the password entered by the user

    @InjectComponent
    private Form createAccountForm; // Form component for creating a user account

    // This method sets up the user account if it is not already initialized or set
    void setupRender() {
        if (userAccount == null || userAccount.getPK() == null || userAccount.getPK() == -1) {
            userAccount = userAccountDatabaseService.getNewUserAccount();
        }
    }

    // Validates the fields in the form before submitting the account creation request
    void onValidateFromCreateAccountForm() {
        // Check if username already exists
        if (isUsernameExists(userName)) {
            createAccountForm.recordError("Username already exists. Please choose a different username.");
            return;
        }

        // Password validation: ensure the password is long enough
        if (passWord == null || passWord.length() < 10) {
            createAccountForm.recordError("Password must be at least 10 characters long");
            return;
        }

        // Check if the password is strong (contains uppercase, lowercase, number, and special character)
        if (!isPasswordStrong(passWord)) {
            createAccountForm.recordError("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character");
            return;
        }

        // Set the username and password salt for the user account
        userAccount.setUserName(userName);
        userAccount.setPasswordSalt(new SecureRandomNumberGenerator().nextBytes().toHex());
        userAccount.setPassword(passWord);

        // Validate the user account object (e.g., check for any other missing information or errors)
        List<String> errors = userAccount.validate();
        for (String error : errors) {
            createAccountForm.recordError(error);
        }
    }

    // Checks if the username already exists in the database
    private boolean isUsernameExists(String username) {
        try {
            UserAccount existingUser = userAccountDatabaseService.getUserAccount(username);
            return existingUser != null; // Return true if user exists, false otherwise
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false in case of any error
        }
    }

    // Validates the strength of the password (at least one uppercase, one lowercase, one number, and one special character)
    private boolean isPasswordStrong(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        // Loop through each character in the password to check the conditions
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            else if (Character.isLowerCase(c)) hasLowercase = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        return hasUppercase && hasLowercase && hasNumber && hasSpecial; // Return true if all conditions are met
    }

    // Handles the success case when the user successfully submits the account creation form
    Object onSuccessFromCreateAccountForm() {
        if (!createAccountForm.getHasErrors()) { // Check if there are any form validation errors
            try {
                // Check if the UserName is taken. If not, create the user account.
                if (!isUsernameExists(userName)) {
                    userAccountDatabaseService.updateUserAccount(userAccount); // Save the user account to the database
                    return performAutoLogin(userName, passWord, true); // Automatically log the user in after creating the account
                } else {
                    // If username exists, show an error alert
                    alertManager.alert(Duration.SINGLE, Severity.ERROR, "Username already exists. Please choose a different username.");
                }
            } catch (Exception e) {
                // Handle any error that occurs during account creation
                alertManager.alert(Duration.SINGLE, Severity.ERROR, "Error creating account. Please try again.");
                e.printStackTrace();
            }
        }
        return null; // Return null to stay on the account creation page if there were errors
    }

    // Attempts to log the user in automatically after account creation
    private Object performAutoLogin(String username, String password, boolean rememberMe) {
        Subject currentUser = securityService.getSubject(); // Get the current user (subject) for authentication
        if (currentUser == null) {
            throw new IllegalStateException("Subject can't be null");
        }

        // Create a token for the provided username and password
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(rememberMe); // Set the rememberMe option for the token

        try {
            // Attempt to log in with the provided credentials
            currentUser.login(token);
            alertManager.alert(Duration.SINGLE, Severity.SUCCESS, "Account created and logged in successfully.");
            return Index.class; // Redirect to the Index page on successful login
        } catch (AuthenticationException e) {
            // If login fails, show an error alert
            alertManager.alert(Duration.SINGLE, Severity.ERROR, "Automatic login failed. Please try to log in manually.");
            return null; // Stay on the current page if login fails
        }
    }
}
