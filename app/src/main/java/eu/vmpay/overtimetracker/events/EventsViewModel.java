package eu.vmpay.overtimetracker.events;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

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
		calendarRepository.getEventList(calendarId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(new Observer<List<EventModel>>()
				{
					@Override
					public void onSubscribe(Disposable d)
					{
					}

					@Override
					public void onNext(List<EventModel> eventModels)
					{
						items.clear();
						items.addAll(eventModels);
					}

					@Override
					public void onError(Throwable e)
					{
						e.printStackTrace();
					}

					@Override
					public void onComplete()
					{
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
}
