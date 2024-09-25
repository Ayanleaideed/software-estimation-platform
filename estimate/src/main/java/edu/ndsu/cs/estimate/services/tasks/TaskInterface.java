package edu.ndsu.cs.estimate.services.tasks;
import java.util.ArrayList;

import java.util.List;

import org.apache.cayenne.ObjectContext;
import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.cs.estimate.cayenne.persistent.User;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.tasks.TaskDatabaseService;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface TaskInterface {
	
    public Integer 	getPK(); 

    public String 	getName();
	public void	  	setName(String name);

    public LocalDate getActualEndDate();
	public void   	 setActualEndDate(LocalDate actualEndDate); 

    public boolean 	getCompleted();
	public void   	setCompleted(boolean completed); 

    public boolean 	getDropped();
	public void   	setDropped(boolean dropped);
	
	public boolean 	getWillNotComplete();
	public void 	setWillNotComplete(boolean willNotComplete);

    public Date      getEstEndDate();
	public void   	 setEstEndDate(Date estEndDate); 

    public Date      getStartDate();
	public void   	 setStartDate(Date startDate); 

    public int       getTimeTaken();
	public void   	 setTimeTaken(int timeTaken);

    public UserAccount     getUser();
	public void   			setUser(UserAccount user); 
	
	public void setObjectContext(ObjectContext obj);
    public ObjectContext getObjectContext();

	@SuppressWarnings("deprecation")
	public default List<String> validate() {		
		ArrayList<String> errors = new ArrayList<String>();
		
		if(getName() == null || getName().trim().length() == 0) {
			errors.add("Name must be included.");
		} else if(getName().length() > 25) {
			errors.add("Name cannot contain more than 25 characters."); 
		} else if(getEstEndDate().before(getStartDate())) {
			errors.add("Estimated end date must be after start date.");
		} else if(getTimeTaken() > 999999999999L) {
			errors.add("Time cannot contain more than 12 digits.");
		} else if(getStartDate().before(new Date(0, 0, 1))|| 
				getEstEndDate().before(new Date(0, 0, 1))) {
			errors.add("Dates cannot be before 1/1/1900.");
		}

		return errors; 
	}


	
}
