package uk.ac.horizon.observer;

import uk.ac.horizon.observer.model.Place;
import uk.ac.horizon.observer.model.Places;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
	private static final long DURATION = 30000;
	private static final long INTERVAL = 1000;
	private static int observationCount = 1;
	private static boolean startedTimer = false;

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
		setListAdapter(new ArrayAdapter<Place>(getActivity(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, Places.getPlaces()));

		// For action bar
		setHasOptionsMenu(true);
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
		new ActionTimer();
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
	}

	/**
	 * Notify the active callbacks interface (the activity, if the fragment is attached to one) 
	 * that an item has been selected.
	 */
	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		mCallbacks.onItemSelected("" + position);
		Places.setCurrentPlace(position);
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
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
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
		public ActionTimer() {
			if(!startedTimer){
				startedTimer = true;
				startTimer();
			}
		}
		
		private void startTimer(){
			CountDownTimer timer = new CountDownTimer(DURATION, INTERVAL) {

				/**
				 * When the countdown finishes: 
				 *  - Todo: Store the click data 
				 * 	- increment the observation counter 
				 * 	- reset the interface
				 *  - Restart the countdown
				 */
				@Override
				public void onFinish() {
					observationCount++;
					reset();
					startTimer();
				}

				/**
				 * Update the actionbar text on each tick so the user knows how much 
				 * time is remaining in current observation
				 */
				@Override
				public void onTick(long millisecondsLeft) {
					long numSec = millisecondsLeft / 1000;
					timerText.setText("" + numSec
							+ " second" + ((1 == numSec) ? "" : "s") + 
							" remaining for observation "
							+ observationCount);

				}
			};

			timer.start();
		}

		/**
		 * Reset observer by clearing the selections from the places and tasks fragments
		 */
		private void reset() {
			final ListView lv = getListView();
			lv.clearChoices();
			for (int i = 0; i < lv.getChildCount(); i++) {
				lv.setItemChecked(i, false);
			}
			Places.setCurrentPlace(-1);
		}
	}
}
