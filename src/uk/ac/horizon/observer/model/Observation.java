package uk.ac.horizon.observer.model;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * An observation (such as a Place or Task)
 * 
 * @author Jesse
 * 
 */
public abstract class Observation {
	private String name;
	private String type;
	private Date observationTime = new Date();
	// The id of the last inserted observation; -2 means the value has not been set
	protected static long lastobs = -2;		

	public Observation(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public String getType(){
		return type;
	}

	public Date getObservationTime() {
		return observationTime;
	}

	public static void initDB(Context context){
		ObservationDBHelper.getInstance(context).onCreate(
			ObservationDBHelper.getInstance(context).getDb());
	}
	
	/**
	 * Inserts an observation into the database
	 * @param context
	 */
	public long addObservation(Context context){
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		
		values.put(ObservationDBHelper.ObservationColumns.
				COLUMN_NAME_OBS_TIME, this.observationTime.getTime());
		values.put(ObservationDBHelper.ObservationColumns.
				COLUMN_NAME_OBS_NAME, this.name);
		values.put(ObservationDBHelper.ObservationColumns.
				COLUMN_NAME_OBS_TYPE, type);
		
		lastobs = ObservationDBHelper.getInstance(context).getDb().
				insert(ObservationDBHelper.ObservationColumns.TABLE_NAME, 
						null, values);
		// Log the insertion of the new row
		Log.i(this.getClass().getName(), ""+ lastobs, null);
		return lastobs;
	}

	/**
	 * Note a -2 means this value is not set and a -1 means an 
	 * error occured in the last insertion
	 * @return the id of the last insertion
	 */
	public static long getLastObs(){
		return lastobs;
	}

	/**
	 * Helper class to write Observations to the SQLite DB
	 * 
	 * @author Jesse
	 * 
	 */
	protected static class ObservationDBHelper extends SQLiteOpenHelper {
		// If the database schema changes increment the database version.
		public static final int DATABASE_VERSION = 1;
		public static final String DATABASE_NAME = "Observer.db";

		private static ObservationDBHelper instance = null;
		private static SQLiteDatabase db = null;

		public static ObservationDBHelper getInstance(Context context) {
			if (null == instance) {
				instance = new ObservationDBHelper(context);
				db = instance.getWritableDatabase();
			}
			return instance;
		}
		
		public SQLiteDatabase getDb(){
			return db;
		}

		protected ObservationDBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_CONFIG);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// This database is only a cache for online data, so its upgrade
			// policy is
			// to simply to discard the data and start over
			db.execSQL(SQL_DELETE_CONFIG);
			onCreate(db);
		}

		public void onDowngrade(SQLiteDatabase db, int oldVersion,
				int newVersion) {
			onUpgrade(db, oldVersion, newVersion);
		}

		/**
		 * methods that create and maintain the database and tables
		 */
		private static final String TEXT_TYPE = " TEXT";
		private static final String SEP = ",";
		private static final String SQL_CREATE_CONFIG = "CREATE TABLE IF NOT EXISTS "
				+ ObservationColumns.TABLE_NAME + "("
				+ ObservationColumns.COLUMN_NAME_OBS_TIME + " INTEGER NOT NULL" + SEP
				+ ObservationColumns.COLUMN_NAME_OBS_NAME + " TEXT NOT NULL" + SEP
				+ ObservationColumns.COLUMN_NAME_OBS_TYPE + " TEXT" + SEP
				+ ObservationColumns.COLUMN_NAME_OBS_BIN + " INTEGER" + SEP
				+ ObservationColumns.COLUMN_NAME_RELATED_OBS_TIME + " INTEGER" + SEP
				+ ObservationColumns.COLUMN_NAME_RELATED_OBS_NAME + " TEXT" + SEP
				+ " PRIMARY KEY (" + ObservationColumns.COLUMN_NAME_OBS_TIME
				+ SEP + ObservationColumns.COLUMN_NAME_OBS_NAME + "))";

		private static final String SQL_DELETE_CONFIG = "DROP TABLE IF EXISTS "
				+ ObservationColumns.TABLE_NAME;
		/**
		 * SQLite Database contract for storing observations
		 * 
		 * @author Jesse
		 * 
		 */
		protected static abstract class ObservationColumns implements BaseColumns {
			public static final String TABLE_NAME = "Observations";
			public static final String COLUMN_NAME_OBS_TIME = "obs_time";
			public static final String COLUMN_NAME_OBS_NAME = "obs_name";
			public static final String COLUMN_NAME_OBS_TYPE = "obs_type";
			public static final String COLUMN_NAME_OBS_BIN = "obs_bin";
			public static final String COLUMN_NAME_RELATED_OBS_TIME = "related_obs_time";
			public static final String COLUMN_NAME_RELATED_OBS_NAME = "related_obs_name";
		}
	}
}
