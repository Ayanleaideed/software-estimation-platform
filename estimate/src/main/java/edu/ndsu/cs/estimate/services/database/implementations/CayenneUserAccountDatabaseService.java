package edu.ndsu.cs.estimate.services.database.implementations;

import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;

import edu.ndsu.cs.estimate.cayenne.persistent.CallibrationCategory;
import edu.ndsu.cs.estimate.cayenne.persistent.CallibrationExercise;
import edu.ndsu.cs.estimate.cayenne.persistent.Role;
import edu.ndsu.cs.estimate.cayenne.persistent.User;
import edu.ndsu.cs.estimate.entities.interfaces.RoleInterface;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.database.interfaces.CayenneService;
import edu.ndsu.cs.estimate.services.database.interfaces.UserAccountDatabaseService;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;

public class CayenneUserAccountDatabaseService implements UserAccountDatabaseService{

	private CayenneService cayenneService;
	
	public CayenneUserAccountDatabaseService(CayenneService cayenneService) {
		this.cayenneService = cayenneService;
		
		// Populate the database with some default accounts and roles
		createDefaultUserAccountsAndRoles();
	}
	
	/* This method is used to populate the database with some default Roles and
	 *  UserAccounts to simplify testing. If connecting to a persistent database
	 *  this method should not be called as it could cause duplicate entries in
	 *  the database for values that should be unique. 
	 */
	private void createDefaultUserAccountsAndRoles() {
		ObjectContext context = cayenneService.newContext();
		
		Role admin = context.newObject(Role.class);
		admin.setName("admin");
		
		Role manager = context.newObject(Role.class);
		manager.setName("manager");
		
		Role moderator = context.newObject(Role.class);
		moderator.setName("moderator");
		
		User regularUser = context.newObject(User.class);
		regularUser.setUserName("JohnDoe");
		regularUser.setPasswordSalt(new SecureRandomNumberGenerator().nextBytes().toHex());
		regularUser.setPassword("pass1234");
		
		User adminUser  = context.newObject(User.class);
		adminUser.setUserName("JaneDoe");
		adminUser.setPasswordSalt(new SecureRandomNumberGenerator().nextBytes().toHex());
		adminUser.setPassword("pass1234");
		adminUser.addRole(admin);
		adminUser.addRole(manager);
		
		context.commitChanges();
	}

	@Override
	public List<? extends UserAccount> getUserAccounts() {
		return ObjectSelect.query(User.class).select(cayenneService.newContext());
	}
	
	private List<? extends RoleInterface> getRoles(ObjectContext context) {
		return ObjectSelect.query(RoleInterface.class).select(context);
	}

	@Override
	public List<? extends RoleInterface> getRoles() {
		return getRoles(cayenneService.newContext());
	}

	@Override
	public UserAccount getUserAccount(int PK) {
		return Cayenne.objectForPK(cayenneService.newContext(), UserAccount.class, PK);
	}
	
	@Override
	public UserAccount getUserAccountFromContext(ObjectContext newContext, String userName) {
		return ObjectSelect.query(User.class)
				.where(User.USER_NAME.eq(userName))
				.selectOne(newContext);
	}

	@Override
	public UserAccount getUserAccount(String userName) {
		return ObjectSelect.query(User.class)
				.where(User.USER_NAME.eq(userName))
				.selectOne(cayenneService.newContext());
	}
	

	@Override
	public UserAccount getNewUserAccount() {
		User account = cayenneService.newContext().newObject(User.class);
		account.setPasswordSalt(new SecureRandomNumberGenerator().nextBytes().toHex());
		return account; 
	}

	@Override
	public void deleteUserAccount(int PK) {
		ObjectContext context = cayenneService.newContext();
		User account = Cayenne.objectForPK(context, User.class, PK);
		context.deleteObject(account);
		context.commitChanges();
	}

	@Override
	public void updateUserAccount(UserAccount userAccount) {
		((User)userAccount).getObjectContext().commitChanges();		
	}

	@Override
	public RoleInterface getRole(int PK) {
		return getRole(PK, cayenneService.newContext());
	}
	
	private RoleInterface getRole(int PK, ObjectContext context) {
		return Cayenne.objectForPK(context, RoleInterface.class, PK);
	}
	
	@Override
	public RoleInterface getRole(String name) {
		return ObjectSelect.query(RoleInterface.class)
				.where(Role.NAME.eq(name))
				.selectOne(cayenneService.newContext());
	}

	@Override
	public RoleInterface getNewRole() {
		return cayenneService.newContext().newObject(RoleInterface.class);
	}

	@Override
	public void deleteRole(int PK) {
		ObjectContext context = cayenneService.newContext();
		Role role = Cayenne.objectForPK(context, Role.class, PK);
		context.deleteObject(role);
		context.commitChanges();
	}

	@Override
	public void updateRole(RoleInterface RoleInterface) {
		((Role)RoleInterface).getObjectContext().commitChanges();
		
	}
	
	public SelectModel getRoleSelectModel(UserAccount userAccount) {
		return new RoleSelectModel(userAccount);
	}
	
	public ValueEncoder<RoleInterface> getRoleValueEncoder(UserAccount userAccount) {
		return new RoleValueEncoder(userAccount);
	}
	
	/* This class is responsible for populating the drop-down list with choices. It's
	 *  fairly simplistic, but could have it's functionality extended so that it only
	 *  populates the menu with roles that the user does not already have.
	 * The reason that this exists as a private class in the DatabaseService implementation
	 *  class is to avoid excess coupling in the UserAccount interface, which would 
	 *  otherwise need to have access to the full list of roles. If that were fixed, 
	 *  there would be less of a reason to put this code here, but since we need to 
	 *  access the list of roles from the database, it fits better here. 
	 */
	private class RoleSelectModel extends AbstractSelectModel {

		private List<? extends RoleInterface> roles; 
		
		public RoleSelectModel(UserAccount userAccount) {
			roles = getRoles(((User)userAccount).getObjectContext());
		}
		
		/* This method can return null since this is intended to be used only with a 
		 *  a single-select drop-down menu. 
		 */
		@Override
		public List<OptionGroupModel> getOptionGroups() {
			return null;
		}
		
		/* Create and return a List<OptionModel> which Tapestry will use. This just
		 *  controls what is displayed in the drop-down menu. Here we use the name.  
		 */
		@Override
		public List<OptionModel> getOptions() {
			ArrayList<OptionModel> options = new ArrayList<OptionModel>(); 
			for(RoleInterface role : roles) {
				options.add(new OptionModelImpl(role.getName(), role));
			}
			return options; 
  		}
	}
	
	/* This class is responsible for mapping a selected choice from a drop-down menu
	 *  onto an actual Role object and the corresponding database entry. Here the 
	 *  UserAccount parameter used by the constructor method is actually necessary
	 *  in order to ensure that the Role object returned by the toValue method has
	 *  the same ObjectContext as the UserAccount since Cayenne will throw exceptions
	 *  if you attempt to commit changes involving mixed contexts.  
	 * This could exist as a separate class and have the DatabaseService injected into
	 *  that class, but it's cleaner to put it here so that it can call private methods
	 *  of the CayenneUserAccountDatabaseService class that aren't exposed by the 
	 *  interface and some of the code that needs to be written is specific to the 
	 *  Cayenne ORM framework and would be difficult to abstract out to a generalized 
	 *  encoder class. 
	 */
	private class RoleValueEncoder implements ValueEncoder<RoleInterface> {

		private ObjectContext context; 
		
		public RoleValueEncoder(UserAccount userAccount) {
			context = ((User)userAccount).getObjectContext();
		}
		
		/* Convert a Role into a value used by the client. Although the return type
		 *  of the method is String, the PK is used. Since Role names are required
		 *  to be unique, it would be okay to use the name, but that's less likely
		 *  to be true of other types of Objects and many of their attributes.
		 */
		@Override
		public String toClient(RoleInterface value) {
			return String.valueOf(value.getPK());
		}

		/* Converts the client value back into a Role. The stored context is used
		 *  to ensure that the Role is created with the same ObjectContext that the
		 *  UserAccount was by Cayenne so that it won't cause errors when committing
		 *  any changes. 
		 */
		@Override
		public RoleInterface toValue(String clientValue) {
			return getRole(Integer.parseInt(clientValue), context);
		}
	}
}

