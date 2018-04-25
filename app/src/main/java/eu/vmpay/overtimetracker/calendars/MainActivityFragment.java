package eu.vmpay.overtimetracker.calendars;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import eu.vmpay.overtimetracker.databinding.FragmentMainBinding;
import eu.vmpay.overtimetracker.repository.CalendarModel;
import eu.vmpay.overtimetracker.utils.SnackbarMessage;
import eu.vmpay.overtimetracker.utils.SnackbarUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{

	private final String TAG = "MainActivityFragment";

	private CalendarsViewModel viewModel;
	private FragmentMainBinding fragmentMainBinding;

	public MainActivityFragment()
	{
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false);
		fragmentMainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		viewModel = MainActivity.obtainViewModel(getActivity());

		fragmentMainBinding.setViewmodel(viewModel);

		CalendarsAdapter mAdapter = new CalendarsAdapter(new ArrayList<CalendarModel>(0), viewModel);
		fragmentMainBinding.recyclerView.setAdapter(mAdapter);

		setHasOptionsMenu(true);

		return fragmentMainBinding.getRoot();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		setupSnackbar();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		viewModel.start();
	}

//	private void requestPermission()
//	{
//		rxPermissions
//				.request(Manifest.permission.READ_CALENDAR)
//				.subscribeWith(new io.reactivex.Observer<Boolean>()
//				{
//					@Override
//					public void onSubscribe(Disposable d)
//					{
//					}
//
//					@Override
//					public void onNext(Boolean granted)
//					{
//						Log.d(TAG, "onNext granted = " + granted);
//						if(!granted)
//						{
//							textView.setText(R.string.permission_not_granted_error);
//						}
//					}
//
//					@Override
//					public void onError(Throwable e)
//					{
//						Log.d(TAG, "onError " + e.toString());
//					}
//
//					@Override
//					public void onComplete()
//					{
//						Log.d(TAG, "onComplete");
//					}
//				});
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
