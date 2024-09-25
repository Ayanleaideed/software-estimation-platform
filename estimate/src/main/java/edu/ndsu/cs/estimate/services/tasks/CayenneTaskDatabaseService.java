package edu.ndsu.cs.estimate.services.tasks;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;

import edu.ndsu.cs.estimate.cayenne.persistent.Task;
import edu.ndsu.cs.estimate.cayenne.persistent.User;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.database.interfaces.CayenneService;



public class CayenneTaskDatabaseService implements TaskDatabaseService{

	private CayenneService cayenneService; 
	
	
	public CayenneTaskDatabaseService(CayenneService cayenneService) {
		this.cayenneService = cayenneService;
	}
	
	public CayenneService getCayenneService() {
		return cayenneService;
	}
	
	@Override
	public List<? extends Task> listAllTasks(Date start, Date end, UserAccount user) {
		return ObjectSelect.query(Task.class)
				.where(Task.COMPLETED.eq(false).andExp(Task.DROPPED.eq(false).andExp(Task.WILL_NOT_COMPLETE.eq(false)).andExp(Task.EST_END_DATE.between(start, end).andExp(Task.USER.eq((User)user)))))
				.select(cayenneService.newContext());
	}

	@Override
	public List<? extends Task> listCompleted(User user){
		return ObjectSelect.query(Task.class)
				.where(Task.COMPLETED.eq(true).andExp(Task.DROPPED.eq(false)).andExp(Task.WILL_NOT_COMPLETE.eq(false)))
				.select(cayenneService.newContext());
	}
	
	//checks if a submitted task name is unique, and if the name is submitted, it's valid
	@Override
	public boolean isTaskNameValidEditing(String taskName, int PK) {
		String targetTaskName = getTask(PK).getName();
		List<Task> pkList = ObjectSelect.query(Task.class)
							.where(Task.NAME.eq(targetTaskName))
							.select(cayenneService.newContext());
		//list to find if the name submitted is equal to the old name
		if (pkList.isEmpty()) {
			List<Task> query = ObjectSelect.query(Task.class)
					.where(Task.NAME.eq(taskName))
					.select(cayenneService.newContext());
			if (query.isEmpty()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}	
	}
	
	@Override
	public boolean isTaskNameValidAdding(String taskName) {
			List<Task> query = ObjectSelect.query(Task.class)
					.where(Task.COMPLETED.eq(false).andExp(Task.DROPPED.eq(false).andExp(Task.WILL_NOT_COMPLETE.eq(false)).andExp(Task.NAME.eq(taskName))))
					.select(cayenneService.newContext());
			if (query.isEmpty()) {
				return true;
			} else {
				return false;
			}	
	}
	
	//gets list of tasks with estEndDate within the next week, and returns a list of errors
	@Override
	public List<String> getDeadlineNotifications(){
		ArrayList<String> alerts = new ArrayList<String>();
		
		Date start = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 7);
		Date end = cal.getTime();
		List<Task> tasksList = ObjectSelect.query(Task.class)
								.where(Task.COMPLETED.eq(false).andExp(Task.DROPPED.eq(false).andExp(Task.WILL_NOT_COMPLETE.eq(false)).andExp(Task.EST_END_DATE.between(start, end))))
								.select(cayenneService.newContext());
		for (Task task : tasksList) {
			alerts.add("The task " +task.getName() + " is due within the week!");
		}
		return alerts;
		
	}

	@Override
	public Task getTask(int PK) {
		return Cayenne.objectForPK(cayenneService.newContext(), Task.class, PK);
	}

	@Override
	public Task getNewTask() {
		return cayenneService.newContext().newObject(Task.class); 
	}

	@Override
	public void deleteTask(int PK) {
		ObjectContext context = cayenneService.newContext();
		Task task = Cayenne.objectForPK(context, Task.class, PK);
		context.deleteObject(task);
		context.commitChanges();
	}

	@Override
	public void updateTask(Task task) {
		// Typecast to access the context for the Cayenne object to commit changes made using it
		((Task)task).getObjectContext().commitChanges();
		
	}

}

