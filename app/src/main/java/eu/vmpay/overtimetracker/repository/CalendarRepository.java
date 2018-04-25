package eu.vmpay.overtimetracker.repository;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Andrew on 13/04/2018.
 */

public class CalendarRepository
{
	private final String TAG = "CalendarRepository";

	private volatile static CalendarRepository INSTANCE = null;

	private final Context context;

	private CalendarRepository(Context context)
	{
		this.context = context;
	}

	public static CalendarRepository getInstance(Context context)
	{
		if(INSTANCE == null)
		{
			synchronized(CalendarRepository.class)
			{
				if(INSTANCE == null)
				{
					INSTANCE = new CalendarRepository(context);
				}
			}
		}
		return INSTANCE;
	}


	public Observable<List<CalendarModel>> getCalendarList()
	{
		return Observable.create(new ObservableOnSubscribe<List<CalendarModel>>()
		{
			@Override
			public void subscribe(ObservableEmitter<List<CalendarModel>> emitter) throws Exception
			{
				if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
				{
					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
					emitter.onError(new Exception("Permission not granted Manifest.permission.READ_CALENDAR"));
					return;
				}
				List<CalendarModel> modelList = new ArrayList<>();
				ContentResolver contentResolver = context.getContentResolver();
				Cursor cursor = contentResolver.query(Calendars.CONTENT_URI, CalendarModel.EVENT_PROJECTION, null, null, null);
				if(cursor == null) return;
				// Use the cursor to step through the returned records
				while(cursor.moveToNext())
				{
					long calID = 0;
					String displayName = null;
					String accountName = null;
					String ownerName = null;

					// Get the field values
					calID = cursor.getLong(CalendarModel.PROJECTION_ID_INDEX);
					displayName = cursor.getString(CalendarModel.PROJECTION_DISPLAY_NAME_INDEX);
					accountName = cursor.getString(CalendarModel.PROJECTION_ACCOUNT_NAME_INDEX);
					ownerName = cursor.getString(CalendarModel.PROJECTION_OWNER_ACCOUNT_INDEX);

					modelList.add(new CalendarModel(calID, displayName, accountName, ownerName));

				}
				cursor.close();
				emitter.onNext(modelList);
				emitter.onComplete();
			}
		});
	}

	public Observable<List<EventModel>> getEventList(final Long calendarId)
	{
		return Observable.create(new ObservableOnSubscribe<List<EventModel>>()
		{
			@Override
			public void subscribe(ObservableEmitter<List<EventModel>> emitter) throws Exception
			{
				if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
				{
					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
					emitter.onError(new Exception("Permission not granted Manifest.permission.READ_CALENDAR"));
					return;
				}
				String selection = "(" + CalendarContract.Events.CALENDAR_ID + " = ?)";
				String[] selectionArgs = new String[] { String.valueOf(calendarId) };
				List<EventModel> modelList = new ArrayList<>();
				ContentResolver contentResolver = context.getContentResolver();
				Cursor cursor = contentResolver.query(CalendarContract.Events.CONTENT_URI, EventModel.EVENT_PROJECTION, selection, selectionArgs, null);
				if(cursor == null) return;
				// Use the cursor to step through the returned records
				while(cursor.moveToNext())
				{
					modelList.add(
							new EventModel(
									cursor.getLong(EventModel.PROJECTION_ID_INDEX),
									cursor.getLong(EventModel.PROJECTION_CALENDAR_ID_INDEX),
									cursor.getString(EventModel.PROJECTION_ORGANIZER_INDEX),
									cursor.getString(EventModel.PROJECTION_TITLE_INDEX),
									cursor.getLong(EventModel.PROJECTION_DTSTART_INDEX),
									cursor.getLong(EventModel.PROJECTION_DTEND_INDEX),
									cursor.getString(EventModel.PROJECTION_DURATION_INDEX),
									cursor.getString(EventModel.PROJECTION_RRULE_INDEX),
									cursor.getString(EventModel.PROJECTION_RDATE_INDEX)
							)
					);

				}
				cursor.close();
				emitter.onNext(modelList);
				emitter.onComplete();
			}
		});
	}

}
