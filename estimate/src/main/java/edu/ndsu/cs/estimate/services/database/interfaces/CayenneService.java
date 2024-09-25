package edu.ndsu.cs.estimate.services.database.interfaces;

import org.apache.cayenne.ObjectContext;

public interface CayenneService {
	ObjectContext newContext();
}

