package eu.vmpay.overtimetracker.events;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import eu.vmpay.overtimetracker.repository.CalendarRepository;
import eu.vmpay.overtimetracker.repository.EventModel;
import eu.vmpay.overtimetracker.utils.SnackbarMessage;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Andrew on 24/04/2018.
 */

public class EventsViewModel extends ViewModel implements DatePickerDialog.OnDateSetListener
{
	public final ObservableList<EventModel> items = new ObservableArrayList<>();
	public final ObservableField<String> textView = new ObservableField<>();

	private final CalendarRepository calendarRepository;
	private final Application mApplication;
	private final Scheduler processScheduler;
	private final Scheduler androidScheduler;
	private final CompositeDisposable compositeDisposable;
	private long calendarId = 0;

	private SnackbarMessage mSnackbarText;

	private Date startDate;
	private Date endDate = new Date();
	private final DateFormat dateInstance = DateFormat.getDateInstance();

	public EventsViewModel(CalendarRepository calendarRepository, Application mApplication)
	{
		this(calendarRepository, mApplication, Schedulers.io(), AndroidSchedulers.mainThread());
	}

	public EventsViewModel(CalendarRepository calendarRepository, Application mApplication,
	                       Scheduler processScheduler, Scheduler androidScheduler)
	{
		this.calendarRepository = calendarRepository;
		this.mApplication = mApplication;
		this.processScheduler = processScheduler;
		this.androidScheduler = androidScheduler;
		this.compositeDisposable = new CompositeDisposable();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 0);
		startDate = cal.getTime();
	}

	public void start()
	{
		showDateRange();
		loadEvents(calendarId, startDate.getTime(), endDate.getTime());
	}

	public void stop()
	{
		compositeDisposable.dispose();
	}

	private void loadEvents(long calendarId, long startTimestamp, long endTimestamp)
	{
		compositeDisposable.add(calendarRepository.getEvents(calendarId, startTimestamp, endTimestamp)
				.subscribeOn(processScheduler)
				.observeOn(androidScheduler)
				.doOnSubscribe(disposable -> items.clear())
				.subscribe(item -> {
							item.setDurationHours(item.getDuration() == null ? ((double) (item.getDtEnd() - item.getDtStart())) / 1000 / 60 / 60 : 0);
							addItem(item);
						},
						Throwable::printStackTrace
				)
		);
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

	@Override
	public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(year, monthOfYear, dayOfMonth);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		startDate = cal.getTime();

		cal.set(yearEnd, monthOfYearEnd, dayOfMonthEnd);
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		endDate = cal.getTime();

		showDateRange();
		loadEvents(calendarId, startDate.getTime(), endDate.getTime());
	}

	private void showDateRange()
	{
		String date = String.format(Locale.US, "%s - %s",
				dateInstance.format(startDate), dateInstance.format(endDate));
		textView.set(date);
	}
}
