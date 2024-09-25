package edu.ndsu.cs.estimate.pages;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

public class Logout {
	
	@Inject
	SecurityService securityService;
	
	Object onActivate() {
		securityService.getSubject().logout();
		return Index.class;
	}

}
