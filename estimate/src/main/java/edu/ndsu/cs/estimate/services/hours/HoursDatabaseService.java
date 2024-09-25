package edu.ndsu.cs.estimate.services.hours;

import java.util.List;

import edu.ndsu.cs.estimate.cayenne.persistent.Hours;
import edu.ndsu.cs.estimate.cayenne.persistent.Task;
import edu.ndsu.cs.estimate.services.database.interfaces.CayenneService;


public interface HoursDatabaseService {
	public List<? extends Hours> listAllHoursByTask(Task newTask);
	
	public Hours getHours(int PK);
	
	public Hours getNewHours(); 
	
	public void deleteHours(int PK);
	
	public void	updateHours(Hours hours);
	
	public CayenneService getCayenneService();
}

