package uk.ac.horizon.observer.model;

import java.util.Date;

/**
 * An observation (such as a Place or Task)
 * @author Jesse
 *
 */
public abstract class Observation {
	private String name;
	private Date observationTime = null;
	public Observation(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}

	public String getName() {
		return name;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	/**
	 * ObservationTime may only be set once.
	 * @param observationTime
	 */
	public void setObservationTime(Date observationTime) {
		if(null == observationTime){
			this.observationTime = observationTime;
		}
	}
	
}
