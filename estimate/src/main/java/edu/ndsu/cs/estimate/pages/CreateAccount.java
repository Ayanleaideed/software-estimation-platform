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
    private String passWord;
    
    @InjectComponent
    private Form createAccountForm;
    
    void setupRender() {
        if (userAccount == null || userAccount.getPK() == null || userAccount.getPK() == -1) {
            userAccount = userAccountDatabaseService.getNewUserAccount();
        }
    }
    
    void onValidateFromCreateAccountForm() {
        // Check if username already exists
        if (isUsernameExists(userName)) {
            createAccountForm.recordError("Username already exists. Please choose a different username.");
            return;
        }

        // Password validation
        if (passWord == null || passWord.length() < 10) {
            createAccountForm.recordError("Password must be at least 10 characters long");
            return;
        }

        if (!isPasswordStrong(passWord)) {
            createAccountForm.recordError("Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character");
            return;
        }
        
        userAccount.setUserName(userName);
        userAccount.setPasswordSalt(new SecureRandomNumberGenerator().nextBytes().toHex());
        userAccount.setPassword(passWord);
        
        List<String> errors = userAccount.validate();
        for(String error : errors) {
            createAccountForm.recordError(error);
        }   
    }

    private boolean isUsernameExists(String username) {
        try {
            UserAccount existingUser = userAccountDatabaseService.getUserAccount(username);
            return existingUser != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isPasswordStrong(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            else if (Character.isLowerCase(c)) hasLowercase = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        
        return hasUppercase && hasLowercase && hasNumber && hasSpecial;
    }
    
    Object onSuccessFromCreateAccountForm() {
        if(!createAccountForm.getHasErrors()) {
            try {
            	// check if the UserName is taken if so then let the end-user know 
                if (!isUsernameExists(userName)) {
                    userAccountDatabaseService.updateUserAccount(userAccount);
                    return performAutoLogin(userName, passWord, true);
                } else {
                    alertManager.alert(Duration.SINGLE, Severity.ERROR, "Username already exists. Please choose a different username.");
                }
            } catch (Exception e) {
                alertManager.alert(Duration.SINGLE, Severity.ERROR, "Error creating account. Please try again.");
                e.printStackTrace();
            }
        }
        return null;
    }
    
    private Object performAutoLogin(String username, String password, boolean rememberMe) {
        Subject currentUser = securityService.getSubject();
        if (currentUser == null) {
            throw new IllegalStateException("Subject can't be null");
        }
        
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(rememberMe);
        
        try {
            currentUser.login(token);
            alertManager.alert(Duration.SINGLE, Severity.SUCCESS, "Account created and logged in successfully.");
            return Index.class;
        } catch (AuthenticationException e) {
            alertManager.alert(Duration.SINGLE, Severity.ERROR, "Automatic login failed. Please try to log in manually.");
            return null;
        }
    }
}