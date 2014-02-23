package uk.ac.horizon.observer.vc;

import uk.ac.horizon.observer.R;
import uk.ac.horizon.observer.model.Observation;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * An activity representing a list of Places. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a {@link TasksActivity}
 * representing item details. On tablets, the activity presents the list of
 * items and item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link PlacesFragment} and the item details (if present) is a
 * {@link TaskFragment}.
 * <p>
 * This activity also implements the required {@link PlacesFragment.Callbacks}
 * interface to listen for item selections.
 */
public class PlacesActivity extends FragmentActivity implements
		PlacesFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private boolean rotated = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_list);

		if (findViewById(R.id.place_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((PlacesFragment) getSupportFragmentManager().findFragmentById(
					R.id.place_list)).setActivateOnItemClick(true);
		}
		Observation.initDB(this);
	}

	/**
	 * Callback method from {@link PlacesFragment.Callbacks} indicating that the
	 * item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(TaskFragment.ARG_ITEM_ID, id);
			TaskFragment fragment = new TaskFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.place_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, TasksActivity.class);
			detailIntent.putExtra(TaskFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		finish();
	}
}
