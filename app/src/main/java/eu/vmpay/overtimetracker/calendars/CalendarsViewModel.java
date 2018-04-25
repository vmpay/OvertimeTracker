package eu.vmpay.overtimetracker.calendars;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.widget.Toast;

import java.util.List;

import eu.vmpay.overtimetracker.R;
import eu.vmpay.overtimetracker.repository.CalendarModel;
import eu.vmpay.overtimetracker.repository.CalendarRepository;
import eu.vmpay.overtimetracker.utils.SingleLiveEvent;
import eu.vmpay.overtimetracker.utils.SnackbarMessage;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by Andrew on 13/04/2018.
 */

public class CalendarsViewModel extends ViewModel
{
	private final String TAG = "CalendarsViewModel";

	public final ObservableList<CalendarModel> items = new ObservableArrayList<>();
	public final ObservableField<String> textView = new ObservableField<>();

	private final SnackbarMessage mSnackbarText = new SnackbarMessage();
	private final SingleLiveEvent<Long> mOpenCalendarEvent = new SingleLiveEvent<>();

	private final CalendarRepository calendarRepository;
	private final Application mApplication;

	public CalendarsViewModel(CalendarRepository calendarRepository, Application mApplication)
	{
		this.calendarRepository = calendarRepository;
		this.mApplication = mApplication;
	}

	public void start()
	{
		loadCalendars();
	}

	private void loadCalendars()
	{
		calendarRepository.getCalendarList()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(new Observer<List<CalendarModel>>()
				{

					@Override
					public void onSubscribe(Disposable d)
					{
						// TODO: show loading indicator
					}

					@Override
					public void onNext(List<CalendarModel> calendarModels)
					{
						items.clear();
						items.addAll(calendarModels);
					}

					@Override
					public void onError(Throwable e)
					{
						e.printStackTrace();
						Toast.makeText(mApplication, R.string.loading_calendar_error, LENGTH_LONG).show();
					}

					@Override
					public void onComplete()
					{
						// TODO: hide loading indicator
					}
				});
	}

	public void onFabClick()
	{
		textView.set("Button clicked");
	}

	SnackbarMessage getSnackbarMessage()
	{
		return mSnackbarText;
	}

	SingleLiveEvent<Long> getmOpenCalendarEvent()
	{
		return mOpenCalendarEvent;
	}
}
