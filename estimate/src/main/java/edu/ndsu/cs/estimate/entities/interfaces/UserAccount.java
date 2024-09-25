package edu.ndsu.cs.estimate.entities.interfaces;

import java.util.ArrayList;

import java.util.List;

import org.apache.cayenne.ObjectContext;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.util.SimpleByteSource;
import org.apache.tapestry5.beanmodel.BeanModel;
import org.apache.tapestry5.beanmodel.services.BeanModelSource;
import org.apache.tapestry5.commons.Messages;



public interface UserAccount {
	public Integer	getPK();
	
	public String	getUserName();
	public void		setUserName(String name);
	
	public Integer	getUserID(); 
	
	public String	getPasswordHash();
	
	public String	getPasswordSalt();
	public void		setPasswordSalt(String passwordSalt);
	
	public void		setPassword(String password);
	
	public void		addRole(RoleInterface role); 
	public void		deleteRole(RoleInterface role);
	
	public List<? extends RoleInterface> getRoles();
	
	public void setObjectContext(ObjectContext obj);
    public ObjectContext getObjectContext();
	
	/* Create a comma separated list of Roles possessed by a user that is returned
	 *  as a String. This can be used to make the data more presentable if we just
	 *  need to display it quickly in a non-interactive manner. 
	 */
	public default String getFormattedRoles() {
		String formattedRoles = "";
		for(RoleInterface role : getRoles()) {
			formattedRoles += role + ", "; 
		}
		if(formattedRoles.length() >= 2) {
			formattedRoles = formattedRoles.substring(0, formattedRoles.length() - 2); 
		}
		return formattedRoles;
	}
	
	/* Default implementation for changing a password. It assumes that the SHA-512 
	 *  hashing algorithm is being used. It will ensure that the new password being
	 *  supplied matches some retyped password as well, but does not enforce any 
	 *  particular rules concerning the length or make-up of the password. 
	 * If some other hashing algorithm is being used, the sub-class should override
	 *  this method with its own specific implementation. 
	 * Unlike other methods that perform some kind of validation which return a
	 *  list of reasons why validation failed, this method only fails if the new
	 *  password doesn't match a retyped password or if the old password does not
	 *  match the stored hashed credentials. 
	 * Also note that this method does not persist any changes to the UserAccount
	 *  so even after calling this method successfully, those changes would not have
	 *  been written back to any database. 
	 */
	public default boolean changePassword(String oldPassword, String newPassword, String retypePassword) {
		
		if(!newPassword.equals(retypePassword)) {
			return false; 
		}
		
		SimpleByteSource salt = new SimpleByteSource(getPasswordSalt());
		String hash = new Sha512Hash(oldPassword, salt).toHex(); 
		
		if(!hash.equals(getPasswordHash())) {
			return false; 
		}
		
		setPassword(newPassword);		
		return true; 	
	}
	
	/* Default method to validate a UserAccount. Checks if the name, ID, and
	 *  the email address exist and are of appropriate length. It also uses
	 *  the Apache Commons email validator to ensure that the supplied email
	 *  is of an appropriate format. 
	 * Note that this method only checks the values for a UserAccount in 
	 *  isolation and does not ensure that the ID or email address are unique
	 *  across all accounts. That validation is left to classes that interface
	 *  with the database. 
	 */
	public default List<String> validate() {
		ArrayList<String> errors = new ArrayList<String>(); 
		
		if(getUserName() == null || getUserName().trim().isEmpty()) {
			errors.add("Name cannot be empty."); 
		}
		else if(getUserName().length() > 50) {
			errors.add("Name cannot be greater than 50 characters."); 
		}
		return errors; 
	}
	
	/* Static method to determine if a password is valid. The rules hare ensure that
	 *  a password is between 10 and 60 characters and has at least one of each of an
	 *  uppercase letter, lowercase letter, numeric digit, and special character. 
	 * It may be reasonable to modify this method to also accept the UserAccount as
	 *  a parameter in order to ensure that something such as a userID is being used
	 *  as part of the password or that other commonly used information (e.g. date
	 *  of birth) which might be available isn't part of the password either. 
	 * This method could also compare the password against a list of commonly used
	 *  insecure passwords in order to exclude it for those reasons as well. 
	 */
	public static List<String> validatePassword(String password) {
		ArrayList<String> errors = new ArrayList<String>();
		if(password.length() < 10) {
			errors.add("Password must contain at least 10 characters.");
		}
		else if(password.length() > 60) {
			errors.add("Password cannot contain more than 60 characters.");
		}
		
		boolean containsUppercase = false;
		boolean containsLowercase = false;
		boolean containsNumber	  = false; 
		boolean containsSpecial   = false; 
		
		for(char c : password.toCharArray()) {
			if(Character.isUpperCase(c)) {
				containsUppercase = true; 
			}
			if(Character.isLowerCase(c)) {
				containsLowercase = true;
			}
			if(Character.isDigit(c)) {
				containsNumber = true; 
			}
			if("!@#$%^&*".contains(String.valueOf(c))) {
				containsSpecial = true; 
			}
		}
		
		if(!containsUppercase) {
			errors.add("Password must contain at least one uppercase letter.");
		}
		if(!containsLowercase) {
			errors.add("Password must contain at least one lowercase letter.");
		}
		if(!containsNumber) {
			errors.add("Password must contain at least one number.");
		}
		if(!containsSpecial) {
			errors.add("Password must contain at least one special (\"!, @, #, $, %, ^, &, *\") character.");
		}
		
		return errors;
	}
	
	/* Create a basic BeanModel that can be used to display a UserAccount. This is useful
	 *  since the UserAccount class contains a lot of information that shouldn't be displayed
	 *  under any circumstances even if it doesn't pose a major security issue. 
	 */
	
	public default BeanModel<UserAccount> getBeanModel(BeanModelSource beanModelSource, Messages messages) {
		BeanModel<UserAccount> beanModel = beanModelSource.createDisplayModel(UserAccount.class, messages);
	    beanModel.include("name", "userID");
		beanModel.add("formattedRoles");
		beanModel.get("formattedRoles").label("Roles");
		
		return beanModel;
	}
}
