package edu.ndsu.cs.estimate.model;

import java.util.ArrayList;

public class ReportInfo
{
	private ArrayList<String> nameList = new ArrayList<String>();
	private ArrayList<Long> Estimated = new ArrayList<Long>();
	private ArrayList<Long> Actual = new ArrayList<Long>();
	private ArrayList<String> Status = new ArrayList<String>();
	private ArrayList<Integer> TimeTaken = new ArrayList<Integer>();	
	
	public ReportInfo(ArrayList<String> nameList,  ArrayList<Long> Estimated,  ArrayList<Long> Actual, ArrayList<String> Status, ArrayList<Integer> TimeTaken) {
		this.nameList = nameList;
		this.Estimated = Estimated;
		this.Actual = Actual;
		this.Status = Status;
		this.TimeTaken = TimeTaken;
	}
	public ArrayList<String> returnNameArray()
	{
		return nameList;
	}
	public ArrayList<Long> returnEstimated()
	{
		return Estimated;
	}
	public ArrayList<Long> returnActual()
	{
		return Actual;
	}
	public ArrayList<String> returnStatus()
	{
		return Status;
	}
	public ArrayList<Integer> returnTime()
	{
		return TimeTaken;
	}
}