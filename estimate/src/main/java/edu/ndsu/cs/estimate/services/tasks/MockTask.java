package edu.ndsu.cs.estimate.services.tasks;

import java.time.LocalDate;
import java.util.Date;

import org.apache.cayenne.ObjectContext;

import edu.ndsu.cs.estimate.cayenne.persistent.Task;
import edu.ndsu.cs.estimate.cayenne.persistent.User;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;

public class MockTask implements TaskInterface {


	private Integer	PK; 
	private LocalDate actualEndDate;
	private boolean completed;
    private boolean dropped;
    private Date estEndDate;
    private String name;
    private Date startDate;
    private Integer timeTaken;
    private UserAccount user;
    private boolean willNotComplete;
	
	private static int nextPK = 1; 
	
	private MockTask(int PK, String name, int timeTaken, Date startDate, Date estEndDate, LocalDate actualEndDate, boolean completed, boolean dropped, UserAccount user, boolean willNotComplete) {
		super();
		this.PK 				= PK; 
		this.name 				= name;
		this.timeTaken			= timeTaken;
		this.startDate			= startDate;
		this.estEndDate 		= estEndDate;
		this.actualEndDate 		= actualEndDate;
		this.completed			= completed;
		this.dropped 			= dropped;
		this.user 				= user;
		this.willNotComplete 	= willNotComplete;
	}
	
	public MockTask() {
		this(nextPK++, "Unknown", 0, new Date(0), new Date(0),LocalDate.now(),false,false, new User(), false);
	}
	
	public MockTask(String name, int timeTaken, Date startDate, Date estEndDate, LocalDate actualEndDate, boolean completed, boolean dropped, UserAccount user, boolean willNotComplete) {
		this(nextPK++, name, timeTaken, startDate, estEndDate, actualEndDate, completed, dropped, user, willNotComplete);
	}
	
	public Integer getPK() {
		return PK;
	} 
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public TaskInterface clone() {
		return new MockTask(PK, name, timeTaken, startDate, estEndDate, actualEndDate, completed, dropped, user, willNotComplete);
	}
	
	@Override
	public String toString() {
		return name; 
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof MockTask) {
			MockTask t = (MockTask)o; 
			return this.PK == t.PK;
		}
		return false; 
	}

	@Override
	public LocalDate getActualEndDate() {
		return this.actualEndDate;
	}

	@Override
	public void setActualEndDate(LocalDate actualEndDate) {
		this.actualEndDate = actualEndDate;
		
	}

	@Override
	public boolean getCompleted() {
		return this.completed;
	}

	@Override
	public void setCompleted(boolean completed) {
		this.completed = completed;
		
	}

	@Override
	public boolean getDropped() {
		return this.dropped;
	}

	@Override
	public void setDropped(boolean dropped) {
		this.dropped = dropped;
		
	}

	@Override
	public Date getEstEndDate() {
		return this.estEndDate;
	}

	@Override
	public void setEstEndDate(Date estEndDate) {
		this.estEndDate = estEndDate;
		
	}

	@Override
	public Date getStartDate() {
		return this.startDate;
	}

	@Override
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		
	}

	@Override
	public int getTimeTaken() {
		return this.timeTaken;
	}

	@Override
	public void setTimeTaken(int timeTaken) {
		this.timeTaken = timeTaken;
	}

	@Override
	public UserAccount getUser() {
		return this.user;
	}

	@Override
	public void setUser(UserAccount user) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean getWillNotComplete() {
		return this.willNotComplete;
	}
	
	@Override
	public void setWillNotComplete(boolean willNotComplete) {
		this.willNotComplete = willNotComplete;
	}

	@Override
	public void setObjectContext(ObjectContext obj) {
		// TODO Auto-generated method stub	
	}

	@Override
	public ObjectContext getObjectContext() {
		return null;
	}

	
}

