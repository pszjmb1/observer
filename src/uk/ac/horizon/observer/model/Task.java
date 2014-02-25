package uk.ac.horizon.observer.model;

import java.util.Date;

import uk.ac.horizon.observer.model.Observation.ObservationDBHelper;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * An observable task
 * 
 * @author Jesse
 * 
 */
public class Task extends Observation {
	private Place myPlace;
	private long myBin;

	public Task(String name) {
		super(name, "task");
		myPlace = null;
		myBin = Places.getCurrentBin();
	}

	public Task(String name, Place placeIn) {
		super(name, "task");
		myPlace = placeIn;
		myBin = Places.getCurrentBin();
	}

	/**
	 * Inserts an observation into the database
	 * 
	 * @param context
	 */
	public long addObservation(Context context) {
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();

		values.put(ObservationDBHelper.ObservationColumns.COLUMN_NAME_SESSION,
				Places.getSession());
		values.put(ObservationDBHelper.ObservationColumns.COLUMN_NAME_OBS_TIME,
				this.getObservationTime().getTime());
		values.put(ObservationDBHelper.ObservationColumns.COLUMN_NAME_OBS_NAME,
				this.getName());
		values.put(ObservationDBHelper.ObservationColumns.COLUMN_NAME_OBS_TYPE,
				this.getType());
		values.put(ObservationDBHelper.ObservationColumns.COLUMN_NAME_OBS_BIN,
				myBin);
		if (null != myPlace) {
			values.put(
					ObservationDBHelper.ObservationColumns.COLUMN_NAME_RELATED_OBS_TIME,
					this.myPlace.getObservationTime().getTime());
			values.put(
					ObservationDBHelper.ObservationColumns.COLUMN_NAME_RELATED_OBS_NAME,
					this.myPlace.getName());
		}

		lastobs = ObservationDBHelper
				.getInstance(context)
				.getDb()
				.insert(ObservationDBHelper.ObservationColumns.TABLE_NAME,
						null, values);
		// Log the insertion of the new row
		//Log.i(this.getClass().getName(), "" + lastobs, null);
		//Toast.makeText(context, "lastobs: " + lastobs, Toast.LENGTH_SHORT)
		//.show();
		return lastobs;
	}
}
