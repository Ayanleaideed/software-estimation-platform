package edu.ndsu.cs.estimate.cayenne.persistent;

import java.util.Date;

import edu.ndsu.cs.estimate.cayenne.persistent.auto._Hours;
import edu.ndsu.cs.estimate.services.hours.HoursInterface;
import edu.ndsu.cs.estimate.services.tasks.TaskInterface;

public class Hours extends _Hours implements HoursInterface{

    private static final long serialVersionUID = 1L;

    public Integer getPK()
    {
    	if(getObjectId() != null && !getObjectId().isTemporary())
    	{
    		return (Integer) getObjectId().getIdSnapshot().get(HOURS_PK_PK_COLUMN);
    	}
    	return null; 
    }
    
    @Override
    public boolean equals(Object o) {
    	if(o instanceof Hours) {
    		Hours other = (Hours) o;
    		if (this.getPK() == null || other.getPK() == null) {
    			return false;
    		} else {
    			return this.getPK().equals(other.getPK());
    		} 
    	}
    	return false; 
    }

	@Override
    public Date getTimestamp() {
        return getTimeStamp(); 
    }

    @Override
    public void setTimestamp(Date timeStamp) {
        setTimeStamp(timeStamp); 
    }

    @Override
    public TaskInterface getTask() {
        return (TaskInterface) getTasks();
    }

    @Override
    public void setTask(TaskInterface task) {
        setTasks((Task) task);
    }
}
