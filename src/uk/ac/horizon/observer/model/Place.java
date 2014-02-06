package uk.ac.horizon.observer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An observable place
 * @author Jesse
 */
public class Place {
	private String name;
	private List<Task> tasks = new ArrayList<Task>();
	
	public Place(String name, List<Task> tasks){
		this.name = name;
		this.tasks = tasks;
	}
	
	public String getName(){
		return name;
	}
	
	public List<Task> getTasks(){
		return tasks;
	}
	
	public String toString(){
		return name;
	}
}
