package uk.ac.horizon.observer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Singleton list of Places
 * @author Jesse
 *
 */
public class Places {
	private static List<Place> places = null;
	private static int currentPlace = -1;
	private static long currentBin;
	private static long currentSession;
	public static List<Place> getPlaces(){
		if(null == places){
			init();
		}
		return places;
	}
	/**
	 * 
	 * @return null if Places.getPlaces() has not been called, or if currentPlace < 0; otherwise
	 * returns the Task list associated with the currently selected Palce
	 */
	public static List<Task> getTasksforCurrentPlace(){
		if(null != places && currentPlace > -1){
			return places.get(currentPlace).getTasks();
		} else{
			return null;
		}
	}	
	public static void setCurrentPlace(int i, long date){
		currentPlace = i;
		places.get(currentPlace).setDate(date);
	}	
	public static String getCurrentPlaceName(){
		if(null != places && currentPlace > -1){
			return places.get(currentPlace).getName();
		} else{
			return null;
		}
	}	
	public static Place getCurrentPlace(){
		if(null != places && currentPlace > -1){
			return places.get(currentPlace);
		} else{
			return null;
		}
	}	
	public static long getCurrentBin(){
		return currentBin;
	}	
	public static void setCurrentBin(long binIn){
		currentBin = binIn;
	}
	public static long getSession(){
		return currentSession;
	}
	public static void setSession(long sessionIn){
		currentSession = sessionIn;
	}
	
	private Places(){
	}
	
	private static void init(){
		places =  new ArrayList<Place>();
		Stack<Task> main = new Stack<Task>();
		main.add(new Task("Walking/Running"));
		main.add(new Task("Talking Face-to-face"));
		main.add(new Task("Talking on Landline -- to Registrar"));
		main.add(new Task("Talking on Landline -- to Other"));
		main.add(new Task("Talking on Blackberry -- to Registrar"));
		main.add(new Task("Talking on Blackberry -- to Other"));
		main.add(new Task("Using Landline"));
		main.add(new Task("Using Blackberry"));
		main.add(new Task("Using PC/Computer on Wheels (COW)"));
		main.add(new Task("Looking at Notes"));
		main.add(new Task("Writing on Notes"));
		main.add(new Task("Adjusting Notes"));
		main.add(new Task("Reviewing Research Materials"));
		main.add(new Task("Searching for Paperwork"));
		main.add(new Task("Searching for a Patient"));
		main.add(new Task("Searching for a Staff Member"));
		main.add(new Task("Searching for Equipment"));
		main.add(new Task("Putting Things Away"));
		main.add(new Task("Point of Care Testing (PoCT)"));
		main.add(new Task("Getting Dressed/Putting on Protective Clothing"));
		main.add(new Task("No Observable Task -- Thinking"));
		main.add(new Task("No Observable Task -- Downtime"));
		main.add(new Task("Interrupted"));
		main.add(new Task("Other"));
		Stack<Task> empty = new Stack<Task>();

		places.add(new Place("In Transit  Within Ward", main));
		places.add(new Place("In Transit  Between Wards", main));
		places.add(new Place("In Ward  Note Trolley", main));
		places.add(new Place("In Ward  Office", main));
		places.add(new Place("In Ward  Nurses Station", main));
		places.add(new Place("In Ward  Stores", main));
		
		places.add(new Place("With Patient  Open Ward", empty));
		places.add(new Place("With Patient  Private Room", empty));
		places.add(new Place("Lab / Pharmacy / etc. (Other)", empty));	
		places.add(new Place("Café / Toilet / Mess", empty));
	}
}
