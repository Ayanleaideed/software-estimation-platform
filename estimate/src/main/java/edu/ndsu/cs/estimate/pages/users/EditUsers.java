package edu.ndsu.cs.estimate.pages.users;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.cs.estimate.entities.interfaces.RoleInterface;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.database.interfaces.UserAccountDatabaseService;

@RequiresRoles("admin")
public class EditUsers {
	
	@Inject
	private UserAccountDatabaseService userAccountDatabaseService; 
	
	@Inject
	private AlertManager alertManager;
	
	@InjectComponent
	private Form userForm;
	
	@InjectComponent
	private Form passwordForm;
	
	@InjectComponent
	private Form addRoleForm;
	
	@Property
	private String newPassword;
	
	@Property
	private String repeatPassword;
	
	@Property
	@Persist
	private SelectModel roleSelectModel;
	
	@Property
	@Persist
	private ValueEncoder<RoleInterface> roleValueEncoder;
	
	@Property
	@Persist
	private UserAccount userAccount; 
	
	@Property
	private Integer userAccountPK;
	
	@Property
	private List<? extends RoleInterface> roles;
	
	@Property
	private RoleInterface role;
	
	@Property
	private RoleInterface roleToAdd;
	
	void onActivate(Integer userAccountPK) {
		this.userAccountPK = userAccountPK;
	}
	
	Integer onPassivate() {
		return userAccountPK; 
	}
	
	void setupRender() {
		userAccount = userAccountDatabaseService.getUserAccount(userAccountPK);
		if(userAccount != null) {
			roles = userAccount.getRoles();
			roleSelectModel = userAccountDatabaseService.getRoleSelectModel(userAccount);
			roleValueEncoder = userAccountDatabaseService.getRoleValueEncoder(userAccount); 
		}
	}
	
	void onDeleteRole(int PK) {
		RoleInterface role = userAccountDatabaseService.getRole(PK);
		userAccount.deleteRole(role);
		userAccountDatabaseService.updateUserAccount(userAccount);
	}
	
	void onValidateFromUserForm() {
		
		List<String> errors = userAccountDatabaseService.validate(userAccount);
		for(String error : errors) {
			userForm.recordError(error);
		}
		
		if(!userForm.getHasErrors()) {
			userAccountDatabaseService.updateUserAccount(userAccount);
		}
	}
	
	void onValidateFromPasswordForm() {
		if(!newPassword.equals(repeatPassword)) {
			passwordForm.recordError("Passwords do not match.");
		}
		
		if(!passwordForm.getHasErrors()) {
			userAccount.setPassword(newPassword);
			userAccountDatabaseService.updateUserAccount(userAccount);
		}
	}
	
	void onValidateFromAddRoleForm() {
		List<? extends RoleInterface> roles = userAccount.getRoles(); 
		if(roles.contains(roleToAdd)) {
			addRoleForm.recordError("User already has the \""+ roleToAdd.getName() + "\" role.");
		}
		if(roleToAdd == null) {
			addRoleForm.recordError("Please select a role to add.");
		}
		if(!addRoleForm.getHasErrors()) {
			userAccount.addRole(roleToAdd);
			userAccountDatabaseService.updateUserAccount(userAccount);
		}
	}
	
	void onSuccess() {
		alertManager.alert(Duration.SINGLE, Severity.SUCCESS, "Update successful.");
	}
}
