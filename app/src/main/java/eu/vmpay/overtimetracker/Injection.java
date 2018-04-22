package eu.vmpay.overtimetracker;

import android.content.Context;
import android.support.annotation.Nullable;

import eu.vmpay.overtimetracker.repository.CalendarRepository;

/**
 * Created by Andrew on 22/04/2018.
 */

class Injection
{
	@Nullable
	public static CalendarRepository provideCalendarRepository(@Nullable Context applicationContext)
	{
		if(applicationContext != null)
		{
			return CalendarRepository.getInstance(applicationContext);
		}
		else
		{
			return null;
		}
	}
}
