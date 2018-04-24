package eu.vmpay.overtimetracker.calendarevents;

import eu.vmpay.overtimetracker.repository.CalendarModel;

/**
 * Created by Andrew on 24/04/2018.
 */

public interface CalendarItemUserActionListener
{
	void onCalendarClicked(CalendarModel calendarModel);
}
