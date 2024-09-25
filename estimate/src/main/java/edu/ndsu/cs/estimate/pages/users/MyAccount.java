package edu.ndsu.cs.estimate.pages.users;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.beanmodel.BeanModel;
import org.apache.tapestry5.beanmodel.services.BeanModelSource;
import org.apache.tapestry5.commons.Messages;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import org.tynamo.security.services.SecurityService;

import edu.ndsu.cs.estimate.entities.interfaces.RoleInterface;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.pages.Index;
import edu.ndsu.cs.estimate.services.database.interfaces.UserAccountDatabaseService;

@RequiresAuthentication
public class MyAccount {
	
	@Inject
	private SecurityService securityService;
	
	@Inject
	private UserAccountDatabaseService userAccountDatabaseService;

	
	@SessionAttribute("USER_CRED")
	@Property
	@Persist
	private UserAccount userAccount; 
	// added things
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
    @Persist
    private String oldPassword;
    
    @Property
    @Persist
    private String newPassword;
    
    @Property
    @Persist
    private String retypePassword;
    
    @Property
    @InjectComponent
    private Form changePasswordForm;
	
    @Inject
	AlertManager alertManager;
	
	void setupRender() {
		String principal = securityService.getSubject().getPrincipal().toString(); 
		userAccount = userAccountDatabaseService.getUserAccount(principal);
		// added things
		if(userAccount != null) {
			roles = userAccount.getRoles();
			userAccountBeanModel = userAccount.getBeanModel(beanModelSource, message);
		}
	}
	
	void onValidateFromChangePasswordForm() {
		
		if(newPassword.equals(retypePassword)) {
			userAccount.changePassword(oldPassword, newPassword, retypePassword);
			userAccountDatabaseService.updateUserAccount(userAccount);
		}

	}
	
	Object onSuccessFromChangePasswordForm() {
		alertManager.alert(Duration.SINGLE, Severity.SUCCESS, "Password changed successfully.");
		return Index.class; 
	}
}
