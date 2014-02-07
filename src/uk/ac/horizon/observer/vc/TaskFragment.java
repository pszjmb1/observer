package uk.ac.horizon.observer.vc;

import java.util.List;
import java.util.Stack;

import uk.ac.horizon.observer.model.Observations;
import uk.ac.horizon.observer.model.Place;
import uk.ac.horizon.observer.model.Places;
import uk.ac.horizon.observer.model.Task;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A fragment representing a single Place's tasks. This fragment is either
 * contained in a {@link PlacesActivity} in two-pane mode (on tablets) or a
 * {@link TasksActivity} on handsets.
 */
public class TaskFragment extends ListFragment {
	/**
	 * The fragment argument representing the Place ID that the tasks are for.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The tasks
	 */
	private List<Task> myTasks = null;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TaskFragment() {
	}

	/*
	 * @Override public void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState);
	 * 
	 * if (getArguments().containsKey(ARG_ITEM_ID)) { // Load the dummy content
	 * specified by the fragment // arguments. In a real-world scenario, use a
	 * Loader // to load content from a content provider. mItem =
	 * DummyContent.ITEM_MAP.get(getArguments().getString( ARG_ITEM_ID)); } }
	 * 
	 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
	 * container, Bundle savedInstanceState) { View rootView =
	 * inflater.inflate(R.layout.fragment_place_detail, container, false);
	 * 
	 * // Show the dummy content as text in a TextView. if (mItem != null) {
	 * ((TextView) rootView.findViewById(R.id.place_detail))
	 * .setText(mItem.content); }
	 * 
	 * return rootView; }
	 */

	/**
	 * On create set the tasks to the ones for the corresponding Place
	 * @author Jesse
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myTasks = Places.getTasksforCurrentPlace();	// can be null
	}

	/**
	 * Display a multipe choice selection list of the tasks available for the given Place
	 * @author Jesse
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if(null != myTasks){
			if(myTasks.isEmpty()){
				Toast.makeText(this.getActivity(),
						String.valueOf("No tasks to select for " + Places.getCurrentPlaceName()),
						Toast.LENGTH_LONG).show();			
					}
			else{	
				ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(
					this.getActivity(),
					android.R.layout.simple_list_item_activated_1, myTasks);
			setListAdapter(adapter);
			getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			}
		} 
	}

	/**
	 * @todo: Add values of clicked items to the click queue 
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Observations.peek().pushTask(myTasks.get(position));
		Toast.makeText(this.getActivity(),
				//String.valueOf("Number of tasks: " + getListView().getCheckedItemCount()),
				String.valueOf("Number of observations: " + Observations.numObservations()),
				//Observations.peek().peekTask().toString(),
				Toast.LENGTH_LONG).show();
	}
}
