package edu.ndsu.cs.estimate.services.tasks;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.cs.estimate.cayenne.persistent.Hours;
import edu.ndsu.cs.estimate.cayenne.persistent.Task;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.hours.HoursDatabaseService;
import edu.ndsu.cs.estimate.services.hours.HoursInterface;

import java.time.LocalDate;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public interface TaskInterface {
	public Integer getPK();

	public String getName();

	public void setName(String name);

	public LocalDate getActualEndDate();

	public void setActualEndDate(LocalDate actualEndDate);

	public boolean getCompleted();

	public void setCompleted(boolean completed);

	public boolean getDropped();

	public void setDropped(boolean dropped);

	public boolean getWillNotComplete();

	public void setWillNotComplete(boolean willNotComplete);

	public boolean getCannotComplete();

	public void setCannotComplete(boolean cannotComplete);

	public Date getEstEndDate();

	public void setEstEndDate(Date estEndDate);

	public Date getStartDate();

	public void setStartDate(Date startDate);

	public int getTimeTaken();

	public void setTimeTaken(int timeTaken);

	public UserAccount getUser();

	public void setUser(UserAccount user);

	public int getEstHours();

	public void setEstHours(int estHours);

	public void setObjectContext(ObjectContext obj);

	public ObjectContext getObjectContext();

	@SuppressWarnings("deprecation")
	public default List<String> validate() {
		ArrayList<String> errors = new ArrayList<String>();

		if (getName() == null || getName().trim().length() == 0) {
			errors.add("Name must be included.");
		} else if (getName().length() > 25) {
			errors.add("Name cannot contain more than 25 characters.");
		} else if (getEstEndDate() == null) {
			errors.add("Estimated end date must be included and in the format MM/dd/yyyy.");
		} else if (getStartDate() == null) {
			errors.add("Start date must be included and in the format MM/dd/yyyy.");
		} else if (getEstEndDate().before(getStartDate())) {
			errors.add("Estimated end date must be after start date.");
		} else if (getTimeTaken() > 999999999999L) {
			errors.add("Time cannot contain more than 12 digits.");
		} else if (getStartDate().before(new Date(0, 0, 1)) ||
				getEstEndDate().before(new Date(0, 0, 1))) {
			errors.add("Dates cannot be before 1/1/1900.");
		}

		return errors;
	}

	// Method to get the status of a task
	public default String getStatus() {
		if (getCompleted()) {
			return "Completed";
		} else if (getDropped()) {
			return "Dropped";
		} else if (getWillNotComplete()) {
			return "Will Not Complete";
		} else if (getCannotComplete()) {
			return "Cannot Complete";
		} else {
			return "In Progress";
		}
	}
}
