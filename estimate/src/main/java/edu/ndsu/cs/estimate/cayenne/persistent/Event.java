package edu.ndsu.cs.estimate.cayenne.persistent;

import edu.ndsu.cs.estimate.cayenne.persistent.auto._Event;
import edu.ndsu.cs.estimate.entities.interfaces.EventInterface;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Event extends _Event implements EventInterface{

    private static final long serialVersionUID = 1L;
    private int result;
    
    public Integer getPK()
    {
    	if(getObjectId() != null && !getObjectId().isTemporary())
    	{
    		return (Integer) getObjectId().getIdSnapshot().get(ID_PK_COLUMN);
    	}
    	return null; 
    }

    public String getFormattedEventDate() {
        Date eventDate = this.getEventDate();
        if (eventDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MM/dd/yyyy");
            return formatter.format(eventDate);
        } else {
            return null;
        }
    }

    @Override
    public int getResult() {
        return this.result;
    }

    @Override
    public void setResult(int result) {
        this.result = result;
    }
}
