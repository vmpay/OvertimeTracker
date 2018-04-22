package eu.vmpay.overtimetracker.calendarevents;

import android.databinding.BindingAdapter;
import android.widget.ListView;

import java.util.List;

import eu.vmpay.overtimetracker.repository.CalendarModel;

/**
 * Created by Andrew on 22/04/2018.
 */

public class CalendarListBindings
{
	@SuppressWarnings("unchecked")
	@BindingAdapter("binding:items")
	public static void setItems(ListView listView, List<CalendarModel> items)
	{
		CalendarsAdapter adapter = (CalendarsAdapter) listView.getAdapter();
		if(adapter != null)
		{
			adapter.replaceData(items);
		}
	}
}
