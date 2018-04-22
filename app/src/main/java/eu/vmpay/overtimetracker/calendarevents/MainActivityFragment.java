package eu.vmpay.overtimetracker.calendarevents;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import eu.vmpay.overtimetracker.R;
import eu.vmpay.overtimetracker.databinding.FragmentMainBinding;
import eu.vmpay.overtimetracker.repository.CalendarModel;
import io.reactivex.disposables.Disposable;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{

	private final String TAG = "MainActivityFragment";

	private RxPermissions rxPermissions;

	private TextView textView;
	private RecyclerView recyclerView;

	private EventSearchViewModel viewModel;
	private FragmentMainBinding fragmentMainBinding;

	private CalendarsAdapter mAdapter;

	public MainActivityFragment()
	{
	}

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//	                         Bundle savedInstanceState)
//	{
//		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//		textView = rootView.findViewById(R.id.textView);
//		recyclerView = rootView.findViewById(R.id.recyclerView);
//		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//		recyclerView.setLayoutManager(mLayoutManager);
//
//		// Get the ViewModel.
//		viewModel = MainActivity.obtainViewModel(getActivity());
//
//		mAdapter = new CalendarsAdapter(new ArrayList<CalendarModel>(0), viewModel);
//		recyclerView.setAdapter(mAdapter);
//
//		// Create the observer which updates the UI.
//		final Observer<String> nameObserver = new Observer<String>()
//		{
//			@Override
//			public void onChanged(@Nullable final String newName)
//			{
//				// Update the UI, in this case, a TextView.
//				textView.setText(newName);
//			}
//		};
//		final Observer<List<CalendarModel>> listObserver = new Observer<List<CalendarModel>>()
//		{
//			@Override
//			public void onChanged(@Nullable List<CalendarModel> calendarModels)
//			{
//				// specify an adapter (see also next example)
//				if(mAdapter != null)
//				{
//					mAdapter.replaceData(calendarModels);
//				}
//			}
//		};
//
//		// Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
//		viewModel.getCurrentName().observe(this, nameObserver);
//		viewModel.getCurrentList().observe(this, listObserver);
//
//		FloatingActionButton fab = rootView.findViewById(R.id.fab);
//		fab.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View view)
//			{
//				CalendarRepository service = new CalendarRepository(getActivity());
//				service.requestCalendars(getActivity());
//				viewModel.getCurrentName().setValue("New Name");
//				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//						.setAction("Action", null).show();
//			}
//		});
//
//		rxPermissions = new RxPermissions(getActivity());
//		requestPermission();
//
//		return rootView;
//	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false);
		fragmentMainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		viewModel = MainActivity.obtainViewModel(getActivity());

		fragmentMainBinding.setViewmodel(viewModel);

		mAdapter = new CalendarsAdapter(new ArrayList<CalendarModel>(0), viewModel);
		fragmentMainBinding.recyclerView.setAdapter(mAdapter);

		setHasOptionsMenu(true);

		return fragmentMainBinding.getRoot();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		viewModel.start();
	}

	private void requestPermission()
	{
		rxPermissions
				.request(Manifest.permission.READ_CALENDAR)
				.subscribeWith(new io.reactivex.Observer<Boolean>()
				{
					@Override
					public void onSubscribe(Disposable d)
					{
					}

					@Override
					public void onNext(Boolean granted)
					{
						Log.d(TAG, "onNext granted = " + granted);
						if(!granted)
						{
							textView.setText(R.string.permission_not_granted_error);
						}
					}

					@Override
					public void onError(Throwable e)
					{
						Log.d(TAG, "onError " + e.toString());
					}

					@Override
					public void onComplete()
					{
						Log.d(TAG, "onComplete");
					}
				});
	}
}
