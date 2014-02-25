package uk.ac.horizon.observer.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

/**
 * A singleton Map of Tasks.
 * @author pszjmb
 *
 */
public class TaskBin {
	private static TaskBin instance = null;
	private Map<String, Task> taskBin;
	private TaskBin(){
		taskBin = new HashMap<String, Task>();
	}
	public static TaskBin getInstance(){
		if(null == instance){
			instance = new TaskBin();
		}
		return instance;
	}
	
	/**
	 * Ensures that only one task with the given name is added. If
	 * a second entry is attempted to be added, then the first is removed,
	 * and none are added.
	 * @param t
	 */
	public void addTask(Task t){
		if(!taskBin.containsKey(t.getName())){
			taskBin.put(t.getName(), t);
		}else{
			removeTask(t);
		}
	}
	
	private void removeTask(Task t){
		taskBin.remove(t.getName());
	}
	
	/**
	 * Adds all task observations and then clears the tasks. 
	 * @param context
	 */
	public void reset(Context context){
		for(Entry<String, Task> entry : taskBin.entrySet()){
			entry.getValue().addObservation(context);
		}
		taskBin.clear();
	}
}
