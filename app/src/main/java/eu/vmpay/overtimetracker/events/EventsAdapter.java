package eu.vmpay.overtimetracker.events;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eu.vmpay.overtimetracker.databinding.EventItemViewBinding;
import eu.vmpay.overtimetracker.repository.EventModel;

/**
 * Created by Andrew on 25/04/2018.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>
{
	private final EventsViewModel viewModel;
	private List<EventModel> eventModelList;

	public EventsAdapter(List<EventModel> eventModelList, EventsViewModel viewModel)
	{
		this.viewModel = viewModel;
		setList(eventModelList);
	}

	private void setList(List<EventModel> eventModelList)
	{
		this.eventModelList = eventModelList;
		notifyDataSetChanged();
	}

	public void replaceData(List<EventModel> eventModelList)
	{
		setList(eventModelList);
	}

	@Override
	public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		EventItemViewBinding binding = EventItemViewBinding.inflate(inflater, parent, false);

		return new ViewHolder(binding.getRoot());
	}

	@Override
	public void onBindViewHolder(EventsAdapter.ViewHolder holder, int position)
	{
		holder.binding.setEvent(viewModel.items.get(position));
	}

	@Override
	public int getItemCount()
	{
		return eventModelList != null ? eventModelList.size() : 0;
	}

	public class ViewHolder extends RecyclerView.ViewHolder
	{
		private final EventItemViewBinding binding;

		public ViewHolder(View itemView)
		{
			super(itemView);
			binding = DataBindingUtil.bind(itemView);
		}
	}
}
