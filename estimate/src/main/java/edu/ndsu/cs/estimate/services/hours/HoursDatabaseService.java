package edu.ndsu.cs.estimate.services.hours;

import java.util.List;

import edu.ndsu.cs.estimate.services.tasks.TaskInterface;
import edu.ndsu.cs.estimate.services.database.interfaces.CayenneService;

public interface HoursDatabaseService {
	public List<? extends HoursInterface> listAllHoursByTask(TaskInterface newTask);
	
	public HoursInterface getHours(int PK);
	
	HoursInterface getNewHours(TaskInterface task);
	
	public void deleteHours(int PK);
	
	public void	updateHours(HoursInterface hours);
	
	public CayenneService getCayenneService();
	
	public int sumOfHoursForTask(TaskInterface task);
}

