package edu.ndsu.cs.estimate.pages.estimation;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.cs.estimate.entities.interfaces.EstimationExercise;
import edu.ndsu.cs.estimate.services.database.interfaces.EstimationExerciseDatabaseService;

@RequiresRoles("admin")
public class ReportOutcome {
	
	@Inject
	AlertManager alertManager;

	@Property
	private Integer exercisePK;
	
	@Inject 
	EstimationExerciseDatabaseService exerciseDatabaseService;
	
	@Property
	@Persist
	EstimationExercise exercise;
	
	
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
	}
	
	void onEventDidOccur() {
		exercise.setOutcome(true);
		exerciseDatabaseService.updateExercise(exercise);
		alertManager.alert(Duration.SINGLE, Severity.SUCCESS, "Event set as occured, you may leave the page.");
	}
	
	void onEventDidNotOccur() {
		exercise.setOutcome(false);
		exerciseDatabaseService.updateExercise(exercise);
		alertManager.alert(Duration.SINGLE, Severity.SUCCESS, "Event set as not-occured, you may leave the page.");
	}
}
