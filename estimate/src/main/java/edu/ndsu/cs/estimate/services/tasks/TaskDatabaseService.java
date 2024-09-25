package edu.ndsu.cs.estimate.services.tasks;

import java.util.Date;
import java.util.List;

import edu.ndsu.cs.estimate.cayenne.persistent.Task;
import edu.ndsu.cs.estimate.cayenne.persistent.User;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.database.interfaces.CayenneService;



public interface TaskDatabaseService {
	public List<? extends Task> listAllTasks(Date start, Date end, UserAccount user);  
	public List<? extends Task> listCompleted(User user);
	public boolean isTaskNameValidEditing(String name, int PK);
	boolean isTaskNameValidAdding(String taskName);
	public List<String> getDeadlineNotifications();
	
	public Task	getTask(int PK);
	
	public Task	getNewTask(); 
	
	public void deleteTask(int PK); 
	
	public void	updateTask(Task task);
	
	public CayenneService getCayenneService();
	
}

