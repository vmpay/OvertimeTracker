package eu.vmpay.overtimetracker.repository;

import android.provider.CalendarContract;

/**
 * Created by Andrew on 21/04/2018.
 */

public class CalendarModel
{
	private final long calID;
	private final String displayName;
	private final String accountName;
	private final String ownerName;

	// Projection array. Creating indices for this array instead of doing
	// dynamic lookups improves performance.
	static final String[] EVENT_PROJECTION = new String[] {
			CalendarContract.Calendars._ID,                           // 0
			CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
			CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
			CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
	};

	// The indices for the projection array above.
	static final int PROJECTION_ID_INDEX = 0;
	static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
	static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
	static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

	public CalendarModel(long calID, String displayName, String accountName, String ownerName)
	{
		this.calID = calID;
		this.displayName = displayName;
		this.accountName = accountName;
		this.ownerName = ownerName;
	}

	public long getCalID()
	{
		return calID;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public String getAccountName()
	{
		return accountName;
	}

	public String getOwnerName()
	{
		return ownerName;
	}
}
