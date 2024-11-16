package edu.ndsu.cs.estimate.services.hours;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.List;

import org.apache.cayenne.ObjectContext;

import java.util.Date;
import edu.ndsu.cs.estimate.services.tasks.TaskInterface;

public interface HoursInterface {
	
    public Integer 	getPK();
    
    public Date		getTimestamp();
    public void 	setTimestamp(Date timeStamp);
    
    public int		getHoursLogged();
    public void		setHoursLogged(int hoursLogged);
    
    public TaskInterface	getTask();
    public void				setTask(TaskInterface task);
	
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

	public default String getFormattedDate() {
		Date date = getTimestamp();
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String dateString = formatter.format(date); // value is : 3/17/2014
		return dateString;
	}
	
}
