package edu.ndsu.cs.estimate.pages.estimation;

import java.util.List;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

import edu.ndsu.cs.estimate.cayenne.persistent.User;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationEstimate;
import edu.ndsu.cs.estimate.entities.interfaces.EstimationExercise;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.database.interfaces.EstimationEstimateDatabaseService;
import edu.ndsu.cs.estimate.services.database.interfaces.EstimationExerciseDatabaseService;
import edu.ndsu.cs.estimate.services.database.interfaces.UserAccountDatabaseService;

public class CreateEstimate {

	@Property
	private Integer exercisePK;
	
	@Inject 
	EstimationEstimateDatabaseService estimateDatabaseService;
	
	@Inject 
	EstimationExerciseDatabaseService exerciseDatabaseService;
	
	@Property
	@Persist
	EstimationExercise exercise;
	
	@Property
	@Persist
	EstimationEstimate estimate;
	
	@InjectComponent
	private Form estimateForm;

	@Inject
	private SecurityService securityService;

	@Inject
	private UserAccountDatabaseService userAccountDatabaseService;
	
	@Property
	@Persist
	private UserAccount	userAccount;

	@Inject
	AlertManager alertManager;
	
	void onActivate(Integer exercisePK) {
		this.exercisePK = exercisePK;
	}
	Integer onPassivate() {
		return exercisePK;
	}
	
	void setupRender() {
		if(exercisePK != null) {
			exercise = exerciseDatabaseService.getExercise(exercisePK);
		}
		estimate = estimateDatabaseService.getNewEstimateFromContext(exercise.getObjectContext());
		String principal = securityService.getSubject().getPrincipal().toString(); 
		userAccount = userAccountDatabaseService.getUserAccountFromContext(exercise.getObjectContext(), principal);
	}
	
	void onValidateFromEstimateForm() {
		List<String> errors = estimate.validate();
		for(String error : errors) {
			estimateForm.recordError(error);
		}
		
		if(errors.isEmpty()) {
			estimate.setExersise(exercise);
			estimate.setUser((User) userAccount);
			estimateDatabaseService.updateEstimate(estimate);
			exercise.addToEstimates(estimate);
			exerciseDatabaseService.updateExercise(exercise);
			alertManager.alert(Duration.SINGLE, Severity.SUCCESS, "Estimation succesfully saved, you may leave the page");
		}
	}
}
