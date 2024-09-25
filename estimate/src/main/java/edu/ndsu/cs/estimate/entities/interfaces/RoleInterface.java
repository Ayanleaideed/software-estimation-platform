package edu.ndsu.cs.estimate.entities.interfaces;

import java.util.ArrayList;
import java.util.List;

public interface RoleInterface {

	public Integer	getPK();
	
	public String	getName();
	public void		setName(String name); 
	
	/* Default method to validate a Role. Checks if the name exists and that it
	 *  is not greater than 20 characters. This corresponds to the size of the 
	 *  database attribute and prevents it from getting cut off. 
	 * Note that this method only checks the values for a UserAccount in 
	 *  isolation and does not ensure that the ID or email address are unique
	 *  across all accounts. That validation is left to classes that interface
	 *  with the database. 
	 */
	public default List<String> validate() {
		ArrayList<String> errors = new ArrayList<String>();
		if(getName() == null || getName().trim().isEmpty()) {
			errors.add("Name must be included");
		}
		else if(getName().length() > 20) {
			errors.add("Name cannot be greater than 20 characters");
		}
		
		return errors; 
	}
}
