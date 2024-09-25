package edu.ndsu.cs.estimate.services.hours;
import java.util.ArrayList;

import java.util.List;

import org.apache.cayenne.ObjectContext;

import edu.ndsu.cs.estimate.cayenne.persistent.Task;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface HoursInterface {
	
    public Integer 	getPK();
    
    public Date		getTimestamp();
    public void 	setTimestamp(Date timeStamp);
    
    public int		getHoursLogged();
    public void		setHoursLogged(int hoursLogged);
    
    public Task		getTask();
    public void		setTask(Task task);
	
	public void setObjectContext(ObjectContext obj);
    public ObjectContext getObjectContext();
    
    //validation method
	public default List<String> validate() {
		ArrayList<String> errors = new ArrayList<String>();
		
		Date now = new Date();
		if(getTimestamp() == null) {
			errors.add("Time must be included.");
		} else if (getTimestamp().after(now)) {
			errors.add("Time must be now or before current date.");
		}
		else if (getHoursLogged() == 0 || getHoursLogged() < 0) {
			errors.add("Hours must be valid.");
		}

		return errors; 
	}


	
}
