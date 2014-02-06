package uk.ac.horizon.observer.model;

/**
 * An observable task
 * @author Jesse
 *
 */
public class Task {
	private String name;

	public Task(String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
