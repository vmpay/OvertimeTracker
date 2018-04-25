package eu.vmpay.overtimetracker.calendars;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eu.vmpay.overtimetracker.databinding.CalendarItemViewBinding;
import eu.vmpay.overtimetracker.repository.CalendarModel;

/**
 * Created by Andrew on 21/04/2018.
 */

public class CalendarsAdapter extends RecyclerView.Adapter<CalendarsAdapter.ViewHolder>
{
	private List<CalendarModel> calendarModelList;
	private final CalendarsViewModel viewModel;

	public CalendarsAdapter(List<CalendarModel> calendarModelList, CalendarsViewModel viewModel)
	{
		this.viewModel = viewModel;
		setList(calendarModelList);
	}

	private void setList(List<CalendarModel> calendarModelList)
	{
		this.calendarModelList = calendarModelList;
		notifyDataSetChanged();
	}

	public void replaceData(List<CalendarModel> calendarModelList)
	{
		setList(calendarModelList);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		CalendarItemViewBinding binding = CalendarItemViewBinding.inflate(inflater, parent, false);
		CalendarItemUserActionListener listener = new CalendarItemUserActionListener()
		{
			@Override
			public void onCalendarClicked(CalendarModel calendarModel)
			{
				viewModel.getmOpenCalendarEvent().setValue(calendarModel.getCalID());
			}
		};
		binding.setListener(listener);

		return new ViewHolder(binding.getRoot());
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		holder.binding.setCalendar(viewModel.items.get(position));
	}

	@Override
	public int getItemCount()
	{
		return calendarModelList != null ? calendarModelList.size() : 0;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder
	{
		private final CalendarItemViewBinding binding;

		public ViewHolder(View v)
		{
			super(v);
			binding = DataBindingUtil.bind(v);
		}
	}
}
