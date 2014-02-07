package uk.ac.horizon.observer.model;

import java.util.Date;
import java.util.Iterator;
import java.util.Stack;

/**
 * A singleton stack of Places.
 * @author Jesse
 *
 */
public class Observations {
	private static Stack<Place> observations = null;
	private static Date startTime;

	protected Observations() {
	}
	
	public static void reset(){
		observations = new Stack<Place>();
		startTime = new Date();
	}
	public static void push(Place anObservation){
		if(null == observations){
			reset();
		}
		observations.push(anObservation);
	}
	public static Place pop(){
		if(null == observations){
			reset();
		}
		return observations.pop();
	}
	public static Place peek(){
		if(null == observations){
			reset();
		}
		return observations.peek();
	}
	public static Date getStartTime(){
		if(null == observations){
			reset();
		}
		return Observations.startTime;
	}
	/**
	 * Return number of places + tasks.
	 */
	public static int numObservations(){
		if(null == observations){
			reset();
			return 0;
		} else if(observations.size() <= 0){
			return 0;
		} else {
			int count = 0;
			Iterator<Place> iter = observations.iterator();

			while (iter.hasNext()){
			    count++;
			    count += iter.next().size();
			}
			return count;
		}
	}
}
