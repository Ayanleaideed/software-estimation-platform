package edu.ndsu.cs.estimate.pages;

import java.util.List;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;
import edu.ndsu.cs.estimate.services.database.interfaces.UserAccountDatabaseService;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;

public class CreateAccount {
    @Inject
    private SecurityService securityService;

    @Inject
    private UserAccountDatabaseService userAccountDatabaseService;

    @Inject
    private AlertManager alertManager;

    @Property
    @Persist
    private UserAccount userAccount;

    @Property
    private String userName;

    @Property
    private String userEmail;

    @Property
    private String passWord;

    @InjectComponent
    private Form createAccountForm;


    // This method sets up the user account if it is not already initialized or set
    void setupRender() {
        if (userAccount == null || userAccount.getPK() == null || userAccount.getPK() == -1) {
            userAccount = userAccountDatabaseService.getNewUserAccount();
        }
    }

    void onValidateFromCreateAccountForm() {
        System.out.println("Validating account creation:");
        System.out.println("Username: " + userName);
        System.out.println("Email: " + userEmail);
        System.out.println("Password: " + (passWord != null ? "Provided" : "Not Provided"));

        // Check if username already exists
        if (isUsernameExists(userName)) {
            createAccountForm.recordError("Username already exists. Please choose a different username.");
            return;
        }

        // Check if email already exists (if email is provided)
        if (userEmail != null && !userEmail.trim().isEmpty() && isEmailExists(userEmail)) {
            createAccountForm.recordError("An account with this email already exists. Please use a different email.");
            return;
        }

        // Validate password length
        if (passWord == null || passWord.length() < 10) {
            createAccountForm.recordError("Password must be at least 10 characters long.");
            return;
        }

        // Validate password strength
        if (!isPasswordStrong(passWord)) {
            createAccountForm.recordError("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.");
            return;
        }

        // Set user details
        userAccount.setUserName(userName);
        userAccount.setUserEmail(userEmail); // Set the email
        userAccount.setPasswordSalt(new SecureRandomNumberGenerator().nextBytes().toHex());
        userAccount.setPassword(passWord);

        // Perform additional validations from UserAccount
        List<String> errors = userAccount.validate();
        for (String error : errors) {
            createAccountForm.recordError(error);
        }
    }
    // Handles the success case when the user successfully submits the account creation form
    Object onSuccessFromCreateAccountForm() {
        if (!createAccountForm.getHasErrors()) {
            try {
                userAccountDatabaseService.updateUserAccount(userAccount);
                return performAutoLogin(userName, passWord, true);
            } catch (Exception e) {
                alertManager.alert(Duration.SINGLE, Severity.ERROR, "Error creating account. Please try again.");
                e.printStackTrace();
            }
        }
        return null;
    }
    // Checks if the username already exists in the database
    private boolean isUsernameExists(String username) {
        try {
            UserAccount existingUser = userAccountDatabaseService.getUserAccount(username);
            return existingUser != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // Checks if the email already exists in the database
    private boolean isEmailExists(String email) {
        try {
            UserAccount existingUser = userAccountDatabaseService.getUserAccountByEmail(email);
            return existingUser != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // Validates the strength of the password (at least one uppercase, one lowercase, one number, and one special character)
    private boolean isPasswordStrong(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber      = false; 
        boolean hasSpecial     = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            else if (Character.isLowerCase(c)) hasLowercase = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        return hasUppercase && hasLowercase && hasNumber && hasSpecial;
    }
    // Attempts to log the user in automatically after account creation
    private Object performAutoLogin(String username, String password, boolean rememberMe) {
        Subject currentUser = securityService.getSubject();
        if (currentUser == null) {
            throw new IllegalStateException("Subject can't be null");
        }

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(rememberMe);

        try {
            currentUser.login(token);  // Attempt to log in with the provided credentials
            alertManager.alert(Duration.SINGLE, Severity.SUCCESS, "Account created and logged in successfully.");
            return Index.class;
        } catch (AuthenticationException e) {  // Catch authentication errors and show an error message if login fails
            createAccountForm.recordError("Automatic login failed. Please try to log in manually.");
            alertManager.alert(Duration.SINGLE, Severity.ERROR, "Automatic login failed. Please try to log in manually.");
            return null;
        }
    }
}
