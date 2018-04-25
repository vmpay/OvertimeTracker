package eu.vmpay.overtimetracker.events;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import eu.vmpay.overtimetracker.repository.EventModel;

/**
 * Created by Andrew on 25/04/2018.
 */

public class EventListBindings
{
	@SuppressWarnings("unchecked")
	@BindingAdapter("app:items")
	public static void setItems(RecyclerView recyclerView, List<EventModel> items)
	{
		EventsAdapter adapter = (EventsAdapter) recyclerView.getAdapter();
		if(adapter != null)
		{
			adapter.replaceData(items);
		}
	}
}
