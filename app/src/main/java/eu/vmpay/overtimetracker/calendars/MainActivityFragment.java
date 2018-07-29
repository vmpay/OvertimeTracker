package eu.vmpay.overtimetracker.calendars;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import eu.vmpay.overtimetracker.databinding.FragmentMainBinding;
import eu.vmpay.overtimetracker.utils.SnackbarMessage;
import eu.vmpay.overtimetracker.utils.SnackbarUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{
	private CalendarsViewModel viewModel;
	private RxPermissions rxPermissions;

	public MainActivityFragment()
	{
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		FragmentMainBinding fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false);
		fragmentMainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		viewModel = MainActivity.obtainViewModel(getActivity());

		fragmentMainBinding.setViewmodel(viewModel);

		CalendarsAdapter mAdapter = new CalendarsAdapter(new ArrayList<>(0), viewModel);
		fragmentMainBinding.recyclerView.setAdapter(mAdapter);

		setHasOptionsMenu(true);

		rxPermissions = new RxPermissions(getActivity());
		viewModel.start(rxPermissions);

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
		viewModel.checkPermissionGranted(rxPermissions);
	}

	@Override
	public void onDestroyView()
	{
		viewModel.stop();
		super.onDestroyView();
	}

	private void setupSnackbar()
	{
		viewModel.getSnackbarMessage().observe(this, (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId ->
				SnackbarUtils.showSnackbar(getView(), getString(snackbarMessageResourceId)));
	}
}
