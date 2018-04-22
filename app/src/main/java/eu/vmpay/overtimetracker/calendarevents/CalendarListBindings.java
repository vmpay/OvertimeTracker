package eu.vmpay.overtimetracker.calendarevents;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import eu.vmpay.overtimetracker.repository.CalendarModel;

/**
 * Created by Andrew on 22/04/2018.
 */

public class CalendarListBindings
{
	@SuppressWarnings("unchecked")
	@BindingAdapter("app:items")
	public static void setItems(RecyclerView recyclerView, List<CalendarModel> items)
	{
		CalendarsAdapter adapter = (CalendarsAdapter) recyclerView.getAdapter();
		if(adapter != null)
		{
			adapter.replaceData(items);
		}
	}
}
