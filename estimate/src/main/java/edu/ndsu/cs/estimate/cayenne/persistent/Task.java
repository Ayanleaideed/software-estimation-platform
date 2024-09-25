package edu.ndsu.cs.estimate.cayenne.persistent;

import edu.ndsu.cs.estimate.cayenne.persistent.auto._Task;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.tasks.TaskInterface;

public class Task extends _Task implements TaskInterface{

    private static final long serialVersionUID = 1L; 
    
    public Integer getPK()
    {
    	if(getObjectId() != null && !getObjectId().isTemporary())
    	{
    		return (Integer) getObjectId().getIdSnapshot().get(TASK_ID_PK_COLUMN);
    	}
    	return null; 
    }
    
    @Override
    public String toString() {
    	return name; 
    }
    
    @Override
    public boolean equals(Object o) {
    	if(o instanceof Task) {
    		Task other = (Task) o;
    		if (this.getPK() == null || other.getPK() == null) {
    			return false;
    		} else {
    			return this.getPK().equals(other.getPK());
    		} 
    	}
    	return false; 
    }

	@Override
	public boolean getCompleted() {
		return false;
	}

	@Override
	public boolean getDropped() {
		return false;
	}
	
	@Override
	public boolean getWillNotComplete() {
		return false;
	}

	@Override
	public void setUser(UserAccount user) {
		setUser((User)user);
		
	}

	


}
