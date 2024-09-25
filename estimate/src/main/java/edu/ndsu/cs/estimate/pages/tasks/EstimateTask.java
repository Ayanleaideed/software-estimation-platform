package edu.ndsu.cs.estimate.pages.tasks;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.Iterator;
import java.time.LocalDate;
import java.time.ZoneId;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.cs.estimate.cayenne.persistent.Task;
import edu.ndsu.cs.estimate.cayenne.persistent.User;
import edu.ndsu.cs.estimate.services.tasks.TaskDatabaseService;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class EstimateTask {
    
    @Inject
    TaskDatabaseService taskDatabase;

    @Property
	private List<? extends Task> tasks;

    private List<? extends Task> getUsersTasks(User user){
        return taskDatabase.listCompleted(user);
    }

    private int getDays(Date endDate){
        Date now = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        return (int) TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - now.getTime()), TimeUnit.MILLISECONDS);
    } 

    private double getDays(Date endDate, Date startDate){
        return (double) TimeUnit.DAYS.convert(Math.abs(endDate.getTime() - startDate.getTime()), TimeUnit.MILLISECONDS);
    } 

    private double[][] getData(List<? extends Task> Tasks){
        Iterator<? extends Task> iterator = Tasks.iterator();

        double[][] data = new double [Tasks.size()][2];
        int i = 0;
        while(iterator.hasNext()){
            data[i][0] = getDays(iterator.next().getEstEndDate(),iterator.next().getStartDate());
            data[i][1] = getDays(Date.from(iterator.next().getActualEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),iterator.next().getStartDate());
            data[i][2] = iterator.next().getTimeTaken();
        }
        return data;
    }

    public double createEstimate(User user, Date endDate, int endHours){
        tasks = getUsersTasks(user);
        int ExpectedDays = getDays(endDate);
        double data[][] = getData(tasks);
        SimpleRegression regression = new SimpleRegression(true);
        regression.addData(data);
        return regression.predict((double) ExpectedDays);
    }



}
