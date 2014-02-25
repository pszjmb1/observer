package uk.ac.horizon.observer.model;

import java.util.Date;
import java.util.Stack;

/**
 * An observable place
 * @author Jesse
 */
public class Place extends Observation {
	private Stack<Task> tasks = new Stack<Task>();
	
	public Place(String name, Stack<Task> tasks){
		super(name, "place");
		this.tasks = tasks;
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
