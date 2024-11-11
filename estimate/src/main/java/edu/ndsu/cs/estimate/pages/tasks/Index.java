package edu.ndsu.cs.estimate.pages.tasks;

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.services.SecurityService;

import edu.ndsu.cs.estimate.services.tasks.TaskInterface;
import edu.ndsu.cs.estimate.entities.interfaces.UserAccount;
import edu.ndsu.cs.estimate.services.database.interfaces.UserAccountDatabaseService;
import edu.ndsu.cs.estimate.services.hours.HoursDatabaseService;
import edu.ndsu.cs.estimate.services.hours.HoursInterface;
import edu.ndsu.cs.estimate.services.tasks.CayenneTaskFactory;
import edu.ndsu.cs.estimate.services.tasks.TaskDatabaseService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Index {

    @Inject
    private AlertManager alertManager;

    @Property
    @Persist
    private String startDate; // Start date for filtering

    @Property
    @Persist
    private String finishDate; // Finish date for filtering

    @InjectComponent
    private Form dateForm;

    @Persist
    @Property
    private int hours;

    @Inject
    private TaskDatabaseService taskDBS;

    @Inject
    private HoursDatabaseService hoursDBS;

    @Property
    private List<? extends TaskInterface> tasks;

    @Property
    private TaskInterface task;

    @InjectComponent
    private Form addHourForm;

    @InjectComponent
    private Form add1HourForm;

    @InjectComponent
    private Form add2HourForm;

    @InjectComponent
    private Form add3HourForm;

    @InjectComponent
    private Form add5HourForm;

    @InjectComponent
    private Form add10HourForm;

    @Inject
    private SecurityService securityService;

    @Inject
    private UserAccountDatabaseService userAccountDatabaseService;

    @Property
    @Persist
    private UserAccount userAccount;

    @SessionAttribute
    @Property
    private Boolean noTasks = null;

    @SessionAttribute
    @Property
    private Boolean makeExamples = false;

    @SessionAttribute
    @Property
    private String selectedStatus = null;

    @SetupRender
    void setupRender() {
        if (noTasks == null) {
            noTasks = true;
        }
        if (makeExamples == null) {
            makeExamples = false;
        }
        if (selectedStatus == null) {
            selectedStatus = "In Progress"; // Default status to query for 
        }
        if (securityService.getSubject().getPrincipal() == null ) {
            return;  // User needs to log in
        }
        // Alerts if tasks are due within the next week
        List<String> alerts = taskDBS.getDeadlineNotifications();
        if (alerts.size() > 0) {
            String alertString = "";
            for(String alert : alerts) {
                alertString += alert + "\n";
            }
            alertManager.alert(Duration.SINGLE, Severity.INFO, alertString);
        }

        String principal = securityService.getSubject().getPrincipal().toString();
        userAccount = userAccountDatabaseService.getUserAccount(principal);
        getTasks(selectedStatus);
    }

    private void getTasks(String status) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false);
        Date start = new Date(0);  // Default to 1/1/1970 if no start date
        Date end = new Date(2145916800000L);  // Default to 1/1/2038 if no finish date

        // Parse the startDate if provided
        if (startDate != null && !startDate.isEmpty()) {
            try {
                start = dateFormat.parse(startDate);
            } catch (ParseException e) {
                dateForm.recordError("The start date format is invalid. Please use MM/dd/yyyy.");
                return;
            }
        }

        // Parse the finishDate if provided
        if (finishDate != null && !finishDate.isEmpty()) {
            try {
                end = dateFormat.parse(finishDate);
            } catch (ParseException e) {
                dateForm.recordError("The finish date format is invalid. Please use MM/dd/yyyy.");
                return;
            }
        }

        if (makeExamples) makeExampleTasks();
        tasks = taskDBS.listAllTasks(start, end, userAccount, selectedStatus);
        noTasks = tasks.isEmpty();
    }

    @OnEvent(component = "filterAll")
    Object onFilterStatusAll() {
        selectedStatus = "All";
        getTasks(selectedStatus);
        return Index.class;
    }

    @OnEvent(component = "filterInProgress")
    Object onFilterStatusInProgress() {
        selectedStatus = "In Progress";
        getTasks(selectedStatus);
        return Index.class;
    }

    @OnEvent(component = "filterCompleted")
    Object onFilterStatusCompleted() {
        selectedStatus = "Completed";
        getTasks(selectedStatus);
        return Index.class;
    }

    @OnEvent(component = "filterDropped")
    Object onFilterStatusDropped() {
        selectedStatus = "Dropped";
        getTasks(selectedStatus);
        return Index.class;
    }

    @OnEvent(component = "filterWillNotComplete")
    Object onFilterStatusWillNotComplete() {
        selectedStatus = "Will Not Complete";
        getTasks(selectedStatus);
        return Index.class;
    }

    void onValidateFromDateForm() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat.setLenient(false);

        // Validate the startDate
        if (startDate != null && !startDate.isEmpty()) {
            try {
                dateFormat.parse(startDate);
            } catch (ParseException e) {
                dateForm.recordError("The start date format is invalid. Please use MM/dd/yyyy.");
            }
        }

        // Validate the finishDate
        if (finishDate != null && !finishDate.isEmpty()) {
            try {
                dateFormat.parse(finishDate);
            } catch (ParseException e) {
                dateForm.recordError("The finish date format is invalid. Please use MM/dd/yyyy.");
            }
        }
    }

    void onSuccessFromDateForm() {
        getTasks(selectedStatus); // Update displayed tasks with the selected date range
    }

    void resetDateRange() {
        startDate = "";
        finishDate = "";
        getTasks(selectedStatus); // Update displayed tasks to show all tasks
    }

    void onDelete(int PK) {
        taskDBS.deleteTask(PK);
        getTasks(selectedStatus); // Update displayed tasks
    }

    void onSubmitFromAddHourForm(int pk) {
        TaskInterface tempTask = taskDBS.getTask(pk);

        if (tempTask.getPK() == null) {
            taskDBS.updateTask(tempTask);  // Commit task to the database to ensure PK is set
        }

        HoursInterface newHour = hoursDBS.getNewHours(tempTask); // Pass tempTask to ensure context consistency
        newHour.setHoursLogged(this.hours);
        newHour.setTimestamp(new Date());
        newHour.setTask(tempTask);  // Link Hours to Task

        hoursDBS.updateHours(newHour);
        int updatedTimeTaken = tempTask.getTimeTaken() + this.hours;
        tempTask.setTimeTaken(updatedTimeTaken);
        taskDBS.updateTask(tempTask);  // Commit updated task with incremented hours

        getTasks(selectedStatus);
    }

    void onSubmitFromAdd1HourForm(int pk) {
        TaskInterface tempTask = taskDBS.getTask(pk);

        if (tempTask.getPK() == null) {
            taskDBS.updateTask(tempTask);  // Commit task to the database to ensure PK is set
        }

        HoursInterface newHour = hoursDBS.getNewHours(tempTask); // Pass tempTask to ensure context consistency
        newHour.setHoursLogged(1);
        newHour.setTimestamp(new Date());
        newHour.setTask(tempTask);  // Link Hours to Task

        hoursDBS.updateHours(newHour);
        int updatedTimeTaken = tempTask.getTimeTaken() + 1;
        tempTask.setTimeTaken(updatedTimeTaken);
        taskDBS.updateTask(tempTask);  // Commit updated task with incremented hours

        getTasks(selectedStatus);
    }

    void onSubmitFromAdd2HourForm(int pk) {
        TaskInterface tempTask = taskDBS.getTask(pk);

        if (tempTask.getPK() == null) {
            taskDBS.updateTask(tempTask);  // Commit task to the database to ensure PK is set
        }

        HoursInterface newHour = hoursDBS.getNewHours(tempTask); // Pass tempTask to ensure context consistency
        newHour.setHoursLogged(2);
        newHour.setTimestamp(new Date());
        newHour.setTask(tempTask);  // Link Hours to Task

        hoursDBS.updateHours(newHour);
        int updatedTimeTaken = tempTask.getTimeTaken() + 2;
        tempTask.setTimeTaken(updatedTimeTaken);
        taskDBS.updateTask(tempTask);  // Commit updated task with incremented hours

        getTasks(selectedStatus);
    }

    void onSubmitFromAdd3HourForm(int pk) {
        TaskInterface tempTask = taskDBS.getTask(pk);

        if (tempTask.getPK() == null) {
            taskDBS.updateTask(tempTask);  // Commit task to the database to ensure PK is set
        }

        HoursInterface newHour = hoursDBS.getNewHours(tempTask); // Pass tempTask to ensure context consistency
        newHour.setHoursLogged(3);
        newHour.setTimestamp(new Date());
        newHour.setTask(tempTask);  // Link Hours to Task

        hoursDBS.updateHours(newHour);
        int updatedTimeTaken = tempTask.getTimeTaken() + 3;
        tempTask.setTimeTaken(updatedTimeTaken);
        taskDBS.updateTask(tempTask);  // Commit updated task with incremented hours

        getTasks(selectedStatus);
    }

    void onSubmitFromAdd5HourForm(int pk) {
        TaskInterface tempTask = taskDBS.getTask(pk);

        if (tempTask.getPK() == null) {
            taskDBS.updateTask(tempTask);  // Commit task to the database to ensure PK is set
        }

        HoursInterface newHour = hoursDBS.getNewHours(tempTask); // Pass tempTask to ensure context consistency
        newHour.setHoursLogged(5);
        newHour.setTimestamp(new Date());
        newHour.setTask(tempTask);  // Link Hours to Task

        hoursDBS.updateHours(newHour);
        int updatedTimeTaken = tempTask.getTimeTaken() + 5;
        tempTask.setTimeTaken(updatedTimeTaken);
        taskDBS.updateTask(tempTask);  // Commit updated task with incremented hours

        getTasks(selectedStatus);
    }

    void onSubmitFromAdd10HourForm(int pk) {
        TaskInterface tempTask = taskDBS.getTask(pk);

        if (tempTask.getPK() == null) {
            taskDBS.updateTask(tempTask);  // Commit task to the database to ensure PK is set
        }

        HoursInterface newHour = hoursDBS.getNewHours(tempTask); // Pass tempTask to ensure context consistency
        newHour.setHoursLogged(10);
        newHour.setTimestamp(new Date());
        newHour.setTask(tempTask);  // Link Hours to Task

        hoursDBS.updateHours(newHour);
        int updatedTimeTaken = tempTask.getTimeTaken() + 10;
        tempTask.setTimeTaken(updatedTimeTaken);
        taskDBS.updateTask(tempTask);  // Commit updated task with incremented hours

        getTasks(selectedStatus);
    }

    void makeExampleTasks() {
        makeExamples = false;
        for (int i = 0; i < 5; i++) {
            CayenneTaskFactory.generateInstance(taskDBS.getCayenneService().newContext(), userAccount);
        }
        getTasks(selectedStatus); // Update displayed tasks after adding
        System.err.println("Amount of tasks now " + tasks.size());
    }

    @OnEvent(component = "complete")
    Object onClickCloseComplete(int pk) {
        TaskInterface tempTask = taskDBS.getTask(pk);
        tempTask.setCompleted(true);
        tempTask.setDropped(false);
        tempTask.setWillNotComplete(false);
        taskDBS.updateTask(tempTask);

        // Re-fetch tasks to ensure updated status
        getTasks(selectedStatus);
        return Index.class;
    }

    @OnEvent(component = "drop")
    Object onClickCloseDropped(int pk) {
        TaskInterface tempTask = taskDBS.getTask(pk);
        tempTask.setCompleted(false);
        tempTask.setDropped(true);
        tempTask.setWillNotComplete(false);
        taskDBS.updateTask(tempTask);

        // Re-fetch tasks to ensure updated status
        getTasks(selectedStatus);
        return Index.class;
    }

    @OnEvent(component = "willNotComplete")
    Object onClickCloseWillNotComplete(int pk) {
        TaskInterface tempTask = taskDBS.getTask(pk);
        tempTask.setCompleted(false);
        tempTask.setDropped(false);
        tempTask.setWillNotComplete(true);
        taskDBS.updateTask(tempTask);

        // Re-fetch tasks to ensure updated status
        getTasks(selectedStatus);
        return Index.class;
    }
}
