package eu.vmpay.overtimetracker.calendars;

import android.Manifest;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import eu.vmpay.overtimetracker.R;
import eu.vmpay.overtimetracker.repository.CalendarModel;
import eu.vmpay.overtimetracker.repository.CalendarRepository;
import eu.vmpay.overtimetracker.utils.SingleLiveEvent;
import eu.vmpay.overtimetracker.utils.SnackbarMessage;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by Andrew on 13/04/2018.
 */

public class CalendarsViewModel extends ViewModel
{
	public final ObservableList<CalendarModel> items = new ObservableArrayList<>();
	public final ObservableBoolean isPermissionGranted = new ObservableBoolean(false);

	private final SnackbarMessage mSnackbarText = new SnackbarMessage();
	private final SingleLiveEvent<Long> mOpenCalendarEvent = new SingleLiveEvent<>();

	private final CalendarRepository calendarRepository;
	private final Application mApplication;
	private final CompositeDisposable compositeDisposable;

	public CalendarsViewModel(CalendarRepository calendarRepository, Application mApplication)
	{
		this.calendarRepository = calendarRepository;
		this.mApplication = mApplication;
		this.compositeDisposable = new CompositeDisposable();
	}

	public void start(@Nullable RxPermissions rxPermissions)
	{
		if(rxPermissions != null)
		{
			compositeDisposable.add(
					rxPermissions.request(Manifest.permission.READ_CALENDAR)
							.subscribe(granted -> {
								if(granted)
								{
									loadCalendars();

								}
								isPermissionGranted.set(granted);
							}));
		}
		else
		{
			isPermissionGranted.set(false);
		}
	}

	public void checkPermissionGranted(@Nullable RxPermissions rxPermissions)
	{
		if(rxPermissions != null)
		{
			if(rxPermissions.isGranted(Manifest.permission.READ_CALENDAR))
			{
				isPermissionGranted.set(true);
				loadCalendars();
			}
			else
			{
				isPermissionGranted.set(false);
			}
		}
	}

	private void loadCalendars()
	{
		compositeDisposable.add(calendarRepository.getCalendarList()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(calendarModels -> {
							items.clear();
							items.addAll(calendarModels);
						},
						throwable -> {
							throwable.printStackTrace();
							Toast.makeText(mApplication, R.string.loading_calendar_error, LENGTH_LONG).show();
						}
				)
		);
	}

	SnackbarMessage getSnackbarMessage()
	{
		return mSnackbarText;
	}

	SingleLiveEvent<Long> getmOpenCalendarEvent()
	{
		return mOpenCalendarEvent;
	}

	public void onOpenSettingsClick()
	{
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		Uri uri = Uri.fromParts("package", mApplication.getPackageName(), null);
		intent.setData(uri);
		mApplication.startActivity(intent);
	}

	public void stop()
	{
		compositeDisposable.clear();
	}
}
