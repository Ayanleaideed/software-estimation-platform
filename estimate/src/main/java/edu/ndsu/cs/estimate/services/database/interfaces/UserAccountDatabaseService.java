package edu.ndsu.cs.estimate.services.database.interfaces;

import java.util.List;

import org.apache.cayenne.ObjectContext;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;

import edu.ndsu.cs.estimate.entities.interfaces.RoleInterface;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;

public interface UserAccountDatabaseService {

	public List<? extends UserAccount> 	getUserAccounts(); 
	public List<? extends RoleInterface>			getRoles();
	
	public UserAccount					getUserAccount(int PK); 
	public UserAccount					getUserAccount(String userID);
	public UserAccount					getNewUserAccount();
	public UserAccount 					getUserAccountFromContext(ObjectContext newContext, String userID);
	public void							deleteUserAccount(int PK);
	public void							updateUserAccount(UserAccount userAccount); 
	
	public RoleInterface							getRole(int PK);
	public RoleInterface							getRole(String name);
	public RoleInterface							getNewRole();
	public void							deleteRole(int PK);
	public void							updateRole(RoleInterface role);
	
	public SelectModel					getRoleSelectModel(UserAccount userAccount);
	public ValueEncoder<RoleInterface>			getRoleValueEncoder(UserAccount userAccount);
	
	

	/* Method to perform validation on a UserAccount. It first validates any of the
	 *  values for the account specifically to ensure that they're not improper. 
	 * Afterwards it performs validation across all accounts to ensure that the 
	 *  UserAccount isn't attempting to change to an ID or email address that's 
	 *  already in use by another UserAccount. 
	 */
	public default List<String>			validate(UserAccount userAccount) {
		List<String> errors = userAccount.validate();
		
		UserAccount otherAccount = getUserAccount(userAccount.getUserID());
		if(otherAccount != null && !otherAccount.equals(userAccount)) {
			errors.add("User ID is already in use.");
		}
		
		return errors; 
	}
	
	/* Method to perform validation on a Role. It first validates any of the values
	 *  for the role specifically to ensure that they're not improper. 
	 * Afterwards it performs validation across all roles to ensure that the Role
	 *  isn't attempting to change name that's already in use by another Role. 
	 */
	public default List<String>			validate(RoleInterface role) {
		List<String> errors = role.validate();
		
		RoleInterface otherRole = getRole(role.getName());
		if(otherRole != null && !otherRole.equals(role)) {
			errors.add("Name is already in use.");
		}
		
		return errors; 
	}
	
}
