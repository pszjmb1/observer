package uk.ac.horizon.observer.vc;

import java.util.Date;
import java.util.Stack;

import uk.ac.horizon.observer.R;
import uk.ac.horizon.observer.model.Observation;
import uk.ac.horizon.observer.model.Place;
import uk.ac.horizon.observer.model.Places;
import uk.ac.horizon.observer.model.Stop;
import uk.ac.horizon.observer.model.Task;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A list fragment representing a list of Places. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link TaskFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 * 
 * @author Jesse
 */
public class PlacesFragment extends ListFragment {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * For the timer in the action bar
	 */
	private static TextView timerText;
	private static final long DURATION = 10000;
	private static final long INTERVAL = 1000;
	private static int observationCount = 1;
	// private static boolean startedTimer = false;
	private static ActionTimer at = null;
	final int MENUITEMSTART = 0;
	final int MENUITEMSTOP = 1;
	Menu myMenu;

	/**
	 * For data handling
	 */
	// private MobileServiceClient mClient;

	private Activity getContext() {
		return this.getActivity();
	}

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public PlacesFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// For action bar
		setHasOptionsMenu(true);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);

		// For Data handling
		/*
		 * try { mClient = new MobileServiceClient(
		 * "https://wayward.azure-mobile.net/",
		 * "roguTRplNWWHyuhEhMhOIeLENGQBLB58", this.getActivity()); } catch
		 * (MalformedURLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}

	/**
	 * For action bar
	 * 
	 * @author Jesse
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu items for use in the action bar

		inflater.inflate(R.menu.main_actions, menu);

		// For timer
		MenuItem timerItem = menu.findItem(R.id.break_timer);
		timerText = (TextView) MenuItemCompat.getActionView(timerItem);
		timerText.setPadding(10, 0, 10, 0);
		// at = new ActionTimer();
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		myMenu = menu;
		if (null == at) {
			menu.getItem(MENUITEMSTOP).setEnabled(false);
			menu.getItem(MENUITEMSTART).setEnabled(true);
		} else {
			menu.getItem(MENUITEMSTOP).setEnabled(true);
			menu.getItem(MENUITEMSTART).setEnabled(false);
		}
	}

	/**
	 * The system calls this method When the user presses one of the action
	 * buttons or another item in the action overflow.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_start:
			actionStart();
			myMenu.getItem(MENUITEMSTOP).setEnabled(true);
			myMenu.getItem(MENUITEMSTART).setEnabled(false);
			return true;
		case R.id.action_stop:
			actionStop();
			myMenu.getItem(MENUITEMSTOP).setEnabled(false);
			myMenu.getItem(MENUITEMSTART).setEnabled(true);
			return true;
			/*
			 * case R.id.action_settings: Intent settingsIntent = new
			 * Intent(this.getActivity(), SettingsActivity.class);
			 * startActivity(settingsIntent); return true;
			 */
		case R.id.action_backup:
			actionBackup();
			return true;
			/*
			 * case R.id.action_undo: actionUndo(); return true;
			 */
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void actionStart() {
		at = new ActionTimer();
		setListAdapter(new ArrayAdapter<Place>(getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, Places.getPlaces()));
		Toast.makeText(getContext(), "Started Observer", Toast.LENGTH_SHORT)
				.show();
	}

	private void actionStop() {
		if (null != at) {
			at.cancel();
			new Stop().addObservation(this.getActivity());
			at = null;
		}
		setTaskListAdapter(null);
		setListAdapter(null);

		Toast.makeText(getContext(), "Stopped Observer", Toast.LENGTH_SHORT)
				.show();

	}

	private void setTaskListAdapter(ListAdapter adapter) {
		TaskFragment frg = (TaskFragment) this.getActivity()
				.getSupportFragmentManager()
				.findFragmentById(R.id.place_detail_container);
		if (null != frg) {
			frg.setListAdapter(adapter);
		}
	}

	/**
	 * On undo: Deselect last selection Pop item from selection stack
	 * 
	 * private void actionUndo() { Toast.makeText(getContext(), "Undo",
	 * Toast.LENGTH_SHORT).show(); }
	 */

	/**
	 * On backup: Backup database to text file. If the preference for resetting
	 * the db is toggled on then empty the DB tables.
	 */
	private void actionBackup() {
		Toast.makeText(getContext(), "Backing up database", Toast.LENGTH_SHORT)
				.show();
		Observation.dumpDB(this.getContext());
	}

	@Override
	public void onPause() {
		super.onPause();
		//Toast.makeText(getContext(), "onPause", Toast.LENGTH_SHORT)
		//.show();
		onDetach();
	}

	@Override
	public void onResume() {
		super.onResume();
		actionStop();
		myMenu.getItem(MENUITEMSTOP).setEnabled(false);
		myMenu.getItem(MENUITEMSTART).setEnabled(true);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}

		Toast.makeText(getContext(), "Click Start to begin Observation",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
		actionStop();
	}

	/**
	 * Notify the active callbacks interface (the activity, if the fragment is
	 * attached to one) that an item has been selected.
	 */
	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		if (null != at) {
			super.onListItemClick(listView, view, position, id);

			mCallbacks.onItemSelected("" + position);
			Places.setCurrentPlace(position);
			Place aplace = new Place(Places.getCurrentPlaceName(),
					new Stack<Task>());
			aplace.addObservation(getContext());
		} else {
			Toast.makeText(getContext(), "Click Start to begin Observation",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 * 
	 * @author Jesse
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				(activateOnItemClick) ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	/**
	 * Timer for the ActionBar
	 * 
	 * @author Jesse
	 */
	private class ActionTimer {
		private CountDownTimer timer = null;

		public ActionTimer() {
			startTimer();
		}

		public void cancel() {
			if (null != this.timer) {
				timerText.setText("");
				this.timer.cancel();
				this.timer = null;
			}
		}

		private void startTimer() {
			if (null != timer) {
				cancel();
			}
			Places.setCurrentBin(new Date().getTime());
			timer = new CountDownTimer(DURATION, INTERVAL) {

				/**
				 * When the countdown finishes: increment the observation
				 * counter - reset the interface - Restart the countdown
				 */
				@Override
				public void onFinish() {
					// dummyDataHandler();
					observationCount++;
					reset();
					this.start();
				}

				/**
				 * Update the actionbar text on each tick so the user knows how
				 * much time is remaining in current observation
				 */
				@Override
				public void onTick(long millisecondsLeft) {
					long numSec = millisecondsLeft / 1000;
					timerText.setText("" + numSec + " second"
							+ ((1 == numSec) ? "" : "s")
							+ " remaining for observation " + observationCount);

				}
			};

			timer.start();
		}

		/**
		 * Reset observer by clearing the selections from the places and tasks
		 * fragments
		 */
		private void reset() {
			try {
				mCallbacks.onItemSelected(null);
			} catch (IllegalStateException e) {
				// Do nothing
			}
		}
	}
}
