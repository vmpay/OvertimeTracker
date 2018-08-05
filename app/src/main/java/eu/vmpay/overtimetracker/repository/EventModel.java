package eu.vmpay.overtimetracker.repository;

import android.provider.CalendarContract;

import java.util.Locale;

/**
 * Created by Andrew on 25/04/2018.
 */

public class EventModel
{
	private final long eventId;
	private final long calendarId;
	private final String organizer;
	private final String title;
	private final long dtStart;
	private final long dtEnd;
	private final String duration;
	private final String rRule;
	private final String rDate;
	private double durationHours;

	static final String[] EVENT_PROJECTION = new String[] {
			CalendarContract.Events._ID,            // 0
			CalendarContract.Events.CALENDAR_ID,    // 1
			CalendarContract.Events.ORGANIZER,      // 2
			CalendarContract.Events.TITLE,          // 3
			CalendarContract.Events.DTSTART,        // 4
			CalendarContract.Events.DTEND,          // 5
			CalendarContract.Events.DURATION,       // 6
			CalendarContract.Events.RRULE,          // 7
			CalendarContract.Events.RDATE,          // 8
	};

	static final int PROJECTION_ID_INDEX = 0;
	static final int PROJECTION_CALENDAR_ID_INDEX = 1;
	static final int PROJECTION_ORGANIZER_INDEX = 2;
	static final int PROJECTION_TITLE_INDEX = 3;
	static final int PROJECTION_DTSTART_INDEX = 4;
	static final int PROJECTION_DTEND_INDEX = 5;
	static final int PROJECTION_DURATION_INDEX = 6;
	static final int PROJECTION_RRULE_INDEX = 7;
	static final int PROJECTION_RDATE_INDEX = 8;

	public EventModel(long eventId, long calendarId, String organizer, String title, long dtStart, long dtEnd, String duration, String rRule, String rDate)
	{
		this.eventId = eventId;
		this.calendarId = calendarId;
		this.organizer = organizer;
		this.title = title;
		this.dtStart = dtStart;
		this.dtEnd = dtEnd;
		this.duration = duration;
		this.rRule = rRule;
		this.rDate = rDate;
	}

	public long getEventId()
	{
		return eventId;
	}

	public long getCalendarId()
	{
		return calendarId;
	}

	public String getOrganizer()
	{
		return organizer;
	}

	public String getTitle()
	{
		return title;
	}

	public long getDtStart()
	{
		return dtStart;
	}

	public long getDtEnd()
	{
		return dtEnd;
	}

	public String getDuration()
	{
		return duration;
	}

	public String getrRule()
	{
		return rRule;
	}

	public String getrDate()
	{
		return rDate;
	}

	public String getDurationHours()
	{
		return String.format(Locale.US, "%.2f h", durationHours);
	}

	public void setDurationHours(double durationHours)
	{
		this.durationHours = durationHours;
	}

	public double getDurationDouble()
	{
		return durationHours;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		EventModel that = (EventModel) o;

		if(eventId != that.eventId) return false;
		if(calendarId != that.calendarId) return false;
		if(dtStart != that.dtStart) return false;
		if(dtEnd != that.dtEnd) return false;
		if(Double.compare(that.durationHours, durationHours) != 0) return false;
		if(organizer != null ? !organizer.equals(that.organizer) : that.organizer != null)
		{
			return false;
		}
		if(title != null ? !title.equals(that.title) : that.title != null) return false;
		if(duration != null ? !duration.equals(that.duration) : that.duration != null) return false;
		if(rRule != null ? !rRule.equals(that.rRule) : that.rRule != null) return false;
		return rDate != null ? rDate.equals(that.rDate) : that.rDate == null;
	}

	@Override
	public int hashCode()
	{
		int result;
		long temp;
		result = (int) (eventId ^ (eventId >>> 32));
		result = 31 * result + (int) (calendarId ^ (calendarId >>> 32));
		result = 31 * result + (organizer != null ? organizer.hashCode() : 0);
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (int) (dtStart ^ (dtStart >>> 32));
		result = 31 * result + (int) (dtEnd ^ (dtEnd >>> 32));
		result = 31 * result + (duration != null ? duration.hashCode() : 0);
		result = 31 * result + (rRule != null ? rRule.hashCode() : 0);
		result = 31 * result + (rDate != null ? rDate.hashCode() : 0);
		temp = Double.doubleToLongBits(durationHours);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}
