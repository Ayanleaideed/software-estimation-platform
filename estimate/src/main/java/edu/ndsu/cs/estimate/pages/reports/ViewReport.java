package edu.ndsu.cs.estimate.pages.reports;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

import java.util.ArrayList;

import edu.ndsu.cs.estimate.model.ReportInfo;

public class ViewReport {
    
	@Property
    @SessionState
    private ReportInfo returnReport;
    
    @Property 
    private int size;
    
    @Property
    private int index;
    
    @Property
    private ArrayList<String> taskNames;
    
    @Property
    private ArrayList<Long> estimatedTimes;
    
    @Property
    private ArrayList<Long> actualTimes;
    
    @Property
    private ArrayList<String> statuses;
    
    @Property
    private ArrayList<Integer> hoursLogged;
    
    @Property
    private ArrayList<Long> net = new ArrayList<Long>();;
    
    @Property
    private String taskName; 
    
    @Property
    private double meanEstimated;
    
    @Property
    private double meanActual;
    
    @Property
    private double meanHoursLogged;
    
    @Property
    private double meanNet;
    

    void setupRender() {
        if (returnReport != null) {
            taskNames = returnReport.returnNameArray();
            estimatedTimes = returnReport.returnEstimated();
            actualTimes = returnReport.returnActual();
            statuses = returnReport.returnStatus();
            hoursLogged = returnReport.returnTime();
            
            for(int i=0;i<taskNames.size();i++)
            {
            	long a = estimatedTimes.get(i) - actualTimes.get(i);
            	net.add(a);
            }
            
            meanEstimated = estimatedTimes.stream().mapToDouble(Long::valueOf).sum() / estimatedTimes.size();
            meanActual = actualTimes.stream().mapToDouble(Long::valueOf).sum() / actualTimes.size();
            meanHoursLogged = hoursLogged.stream().mapToDouble(Integer::valueOf).average().orElse(0.0);
            meanNet = net.stream().mapToDouble(Long::valueOf).sum() / net.size();
            
            size = taskNames.size() -1;
        } else {
            // Handle the case when returnReport is null
        }
    }
}
