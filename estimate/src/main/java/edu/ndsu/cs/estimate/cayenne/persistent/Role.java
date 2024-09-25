package edu.ndsu.cs.estimate.cayenne.persistent;

import edu.ndsu.cs.estimate.cayenne.persistent.auto._Role;
import edu.ndsu.cs.estimate.entities.interfaces.RoleInterface;

public class Role extends _Role implements RoleInterface {

    private static final long serialVersionUID = 1L; 

    public Integer getPK()
    {
    	if(getObjectId() != null && !getObjectId().isTemporary())
    	{
    		return (Integer) getObjectId().getIdSnapshot().get(PK_PK_COLUMN);
    	}
    	return null; 
    }
	
	public String toString() {
		return getName();
	}
	
	public boolean equals(Object o) {
		if(o instanceof Role) {
			Role other = (Role) o;
			return this.getName().equals(other.getName());
		}
		return false; 
	}
}
