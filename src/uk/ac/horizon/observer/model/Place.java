package uk.ac.horizon.observer.model;

import java.util.Date;
import java.util.Stack;

/**
 * An observable place
 * @author Jesse
 */
public class Place extends Observation {
	private Stack<Task> tasks = new Stack<Task>();
	private Integer[] myDisabledTaskList;
	
	public Place(String name, Stack<Task> tasks, Integer[] disabledTaskList){
		super(name, "place");
		this.tasks = tasks;
		myDisabledTaskList = disabledTaskList;
	}
	
	public Integer[] getDisabledTasks(){
		return myDisabledTaskList;
	}
	
	public Stack<Task> getTasks(){
		return tasks;
	}
	
	public void pushTask(Task task){
		tasks.push(task);
	}
	
	public Task popTask(){
		return tasks.pop();
	}
	
	public Task peekTask(){
		return tasks.peek();
	}
	
	public int size(){
		return tasks.size();
	}
	
	public void setDate(long date){
		this.observationTime = new Date(date);
	}
}
