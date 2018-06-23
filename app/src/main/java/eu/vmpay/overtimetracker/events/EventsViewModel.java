package eu.vmpay.overtimetracker.events;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import eu.vmpay.overtimetracker.repository.CalendarRepository;
import eu.vmpay.overtimetracker.repository.EventModel;
import eu.vmpay.overtimetracker.utils.SnackbarMessage;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andrew on 24/04/2018.
 */

public class EventsViewModel extends ViewModel
{
	private final String TAG = "EventsViewModel";

	public final ObservableList<EventModel> items = new ObservableArrayList<>();

	private final CalendarRepository calendarRepository;
	private final Application mApplication;
	private long calendarId = 0;
	private final List<EventModel> groupedList = new ArrayList<>();

	private SnackbarMessage mSnackbarText;

	public EventsViewModel(CalendarRepository calendarRepository, Application mApplication)
	{
		this.calendarRepository = calendarRepository;
		this.mApplication = mApplication;
	}

	public void start(long calendarId)
	{
		loadEvents(calendarId);
	}

	public void start()
	{
		loadEvents(calendarId);
	}

	private void loadEvents(long calendarId)
	{
		// get last month events
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		calendarRepository.getEvents(calendarId, cal.getTimeInMillis(), System.currentTimeMillis())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(new Observer<EventModel>()
				{
					@Override
					public void onSubscribe(Disposable d)
					{
						items.clear();
					}

					@Override
					public void onNext(EventModel item)
					{
						item.setDurationHours(item.getDurationDouble());
						addItem(item);

					}

					@Override
					public void onError(Throwable e)
					{
						e.printStackTrace();
					}

					@Override
					public void onComplete()
					{
						Log.d(TAG, "Queried item list size " + items.size());
					}
				});
	}

	SnackbarMessage getSnackbarMessage()
	{
		return mSnackbarText;
	}

	public void setCalendarId(long calendarId)
	{
		this.calendarId = calendarId;
	}

	private void addItem(EventModel item)
	{
		for(EventModel entry : items)
		{
			if(entry.getTitle().equals(item.getTitle()))
			{
				entry.setDurationHours(entry.getDurationDouble() + item.getDurationDouble());
				return;
			}
		}
		items.add(item);
	}
}
