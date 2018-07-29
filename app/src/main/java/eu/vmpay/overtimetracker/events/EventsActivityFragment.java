package eu.vmpay.overtimetracker.events;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import eu.vmpay.overtimetracker.databinding.FragmentEventsBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventsActivityFragment extends Fragment
{
	private EventsViewModel viewModel;

	public EventsActivityFragment()
	{
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		FragmentEventsBinding fragmentEventsBinding = FragmentEventsBinding.inflate(inflater, container, false);
		fragmentEventsBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		viewModel = EventsActivity.obtainViewModel(getActivity());

		fragmentEventsBinding.setViewmodel(viewModel);

		EventsAdapter mAdapter = new EventsAdapter(new ArrayList<>(0), viewModel);
		fragmentEventsBinding.recyclerView.setAdapter(mAdapter);

		setHasOptionsMenu(true);

		return fragmentEventsBinding.getRoot();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		viewModel.start();
	}

	@Override
	public void onDestroyView()
	{
		viewModel.stop();
		super.onDestroyView();
	}
}
