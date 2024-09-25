package edu.ndsu.cs.estimate.cayenne.persistent;

import edu.ndsu.cs.estimate.cayenne.persistent.auto._Event;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event extends _Event {

    private static final long serialVersionUID = 1L;
    
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


}
