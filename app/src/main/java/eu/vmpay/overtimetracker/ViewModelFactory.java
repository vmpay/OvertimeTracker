package eu.vmpay.overtimetracker;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import eu.vmpay.overtimetracker.calendars.CalendarsViewModel;
import eu.vmpay.overtimetracker.events.EventsViewModel;
import eu.vmpay.overtimetracker.repository.CalendarRepository;

/**
 * Created by Andrew on 22/04/2018.
 */

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory
{
	@SuppressLint("StaticFieldLeak")
	private static volatile ViewModelFactory INSTANCE;

	private final Application mApplication;

	private final CalendarRepository calendarRepository;

	public static ViewModelFactory getInstance(Application application)
	{

		if(INSTANCE == null)
		{
			synchronized(ViewModelFactory.class)
			{
				if(INSTANCE == null)
				{
					INSTANCE = new ViewModelFactory(application,
							Injection.provideCalendarRepository(application.getApplicationContext()));
				}
			}
		}
		return INSTANCE;
	}

	@VisibleForTesting
	public static void destroyInstance()
	{
		INSTANCE = null;
	}

	private ViewModelFactory(Application application, CalendarRepository repository)
	{
		mApplication = application;
		calendarRepository = repository;
	}

	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
	{
		if(modelClass.isAssignableFrom(CalendarsViewModel.class))
		{
			//noinspection unchecked
			return (T) new CalendarsViewModel(calendarRepository, mApplication);
		}
		else if(modelClass.isAssignableFrom(EventsViewModel.class))
		{
			//noinspection unchecked
			return (T) new EventsViewModel(calendarRepository, mApplication);
		}
		throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
	}
}
