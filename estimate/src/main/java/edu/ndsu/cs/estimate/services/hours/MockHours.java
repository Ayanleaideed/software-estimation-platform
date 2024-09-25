package edu.ndsu.cs.estimate.services.hours;

import java.util.Date;

import org.apache.cayenne.ObjectContext;

import edu.ndsu.cs.estimate.cayenne.persistent.Task;

public class MockHours implements HoursInterface {

	private Integer	PK; 
	private Task task;
    private Date timestamp;
    private int hoursLogged;
	
	private static int nextPK = 1; 
	
	private MockHours(int PK, Task task, Date timestamp, int hoursLogged) {
		super();
		this.PK 				= PK; 
		this.task 				= task;
		this.timestamp			= timestamp;
		this.hoursLogged		= hoursLogged;
	}
	
	public MockHours() {
		this(nextPK++, new Task(), new Date(0), 0);
	}
	
	public MockHours(Task task, Date timestamp, int hoursLogged) {
		this(nextPK++, task, timestamp, hoursLogged);
	}
	
	public Integer getPK() {
		return PK;
	} 
	
	public Task getTask() {
		return this.task;
	}
	
	public void setTask(Task task) {
		this.task = task;
	}
	
	
	@Override
	public HoursInterface clone() {
		return new MockHours(PK, task, timestamp, hoursLogged);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof MockHours) {
			MockHours t = (MockHours)o; 
			return this.PK == t.PK;
		}
		return false; 
	}

	@Override
	public Date getTimestamp() {
		return this.timestamp;
	}

	@Override
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
		
	}

	@Override
	public int getHoursLogged() {
		return this.hoursLogged;
	}

	@Override
	public void setHoursLogged(int hoursLogged) {
		this.hoursLogged = hoursLogged;
		
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

