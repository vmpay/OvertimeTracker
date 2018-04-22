package eu.vmpay.overtimetracker;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.VisibleForTesting;

import eu.vmpay.overtimetracker.calendarevents.EventSearchViewModel;
import eu.vmpay.overtimetracker.repository.CalendarRepository;

/**
 * Created by Andrew on 22/04/2018.
 */

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory
{
	@SuppressLint("StaticFieldLeak")
	private static volatile ViewModelFactory INSTANCE;

	private final Application mApplication;

	private final CalendarRepository calendarRespository;

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
		calendarRespository = repository;
	}

	@Override
	public <T extends ViewModel> T create(Class<T> modelClass)
	{
		if(modelClass.isAssignableFrom(EventSearchViewModel.class))
		{
			//noinspection unchecked
			return (T) new EventSearchViewModel(calendarRespository, mApplication);
		}
		throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
	}
}
