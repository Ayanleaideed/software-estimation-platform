package edu.ndsu.cs.estimate.pages;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.beanmodel.BeanModel;
import org.apache.tapestry5.beanmodel.services.BeanModelSource;
import org.apache.tapestry5.commons.Messages;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

import edu.ndsu.cs.estimate.cayenne.persistent.User;
import edu.ndsu.cs.estimate.entities.interfaces.RoleInterface;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.pages.Index;
import edu.ndsu.cs.estimate.services.database.interfaces.UserAccountDatabaseService;

public class CreateAccount
{
	@Inject
	private SecurityService securityService;
	
	@Inject
	private UserAccountDatabaseService userAccountDatabaseService;

    @Inject
	AlertManager alertManager;
    
	@Property
	@Persist
	private UserAccount userAccount; 

	@Property
	private List<? extends RoleInterface> roles;
	
	@Property
	private RoleInterface role;
	
	@Inject
	private Messages message;
	
	@Inject
	private BeanModelSource beanModelSource;
	
	@Property
	private BeanModel<UserAccount> userAccountBeanModel;
	
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
	
    void onValidateFromCreateAccountForm()
    {
		
    	userAccount.setUserName(userName);
		userAccount.setPasswordSalt(new SecureRandomNumberGenerator().nextBytes().toHex());
		userAccount.setPassword(passWord);
			List<String> errors = userAccount.validate();
			for(String error : errors)
			{
				createAccountForm.recordError(error);
			}	
			if(!createAccountForm.getHasErrors())
			{
				
				userAccountDatabaseService.updateUserAccount(userAccount);
			}
		
	}

    Object onSuccessFromCreateAccountForm()
    {
    	if(!createAccountForm.getHasErrors()) {
            userAccountDatabaseService.updateUserAccount(userAccount);
            return performAutoLogin(userName, passWord, true);
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
