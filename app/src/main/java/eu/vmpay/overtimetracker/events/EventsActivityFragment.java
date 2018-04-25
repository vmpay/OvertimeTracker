package eu.vmpay.overtimetracker.events;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import eu.vmpay.overtimetracker.databinding.FragmentEventsBinding;
import eu.vmpay.overtimetracker.repository.EventModel;
import eu.vmpay.overtimetracker.utils.SnackbarMessage;
import eu.vmpay.overtimetracker.utils.SnackbarUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventsActivityFragment extends Fragment
{

	private EventsViewModel viewModel;
	private FragmentEventsBinding fragmentEventsBinding;

	public EventsActivityFragment()
	{
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		fragmentEventsBinding = FragmentEventsBinding.inflate(inflater, container, false);
		fragmentEventsBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		viewModel = EventsActivity.obtainViewModel(getActivity());

		fragmentEventsBinding.setViewmodel(viewModel);

		EventsAdapter mAdapter = new EventsAdapter(new ArrayList<EventModel>(0), viewModel);
		fragmentEventsBinding.recyclerView.setAdapter(mAdapter);

		setHasOptionsMenu(true);

//		setupSnackbar();

		return fragmentEventsBinding.getRoot();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		viewModel.start();
	}

	//	@Override
//	public void onActivityCreated(@Nullable Bundle savedInstanceState)
//	{
//		super.onActivityCreated(savedInstanceState);
//
//		setupSnackbar();
//	}

	private void setupSnackbar()
	{
		viewModel.getSnackbarMessage().observe(this, new SnackbarMessage.SnackbarObserver()
		{
			@Override
			public void onNewMessage(@StringRes int snackbarMessageResourceId)
			{
				SnackbarUtils.showSnackbar(getView(), getString(snackbarMessageResourceId));

			}
		});
	}
}
