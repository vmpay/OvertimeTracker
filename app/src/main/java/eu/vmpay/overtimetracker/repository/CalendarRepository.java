package eu.vmpay.overtimetracker.repository;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static eu.vmpay.overtimetracker.repository.CalendarModel.EVENT_PROJECTION;
import static eu.vmpay.overtimetracker.repository.CalendarModel.PROJECTION_ACCOUNT_NAME_INDEX;
import static eu.vmpay.overtimetracker.repository.CalendarModel.PROJECTION_DISPLAY_NAME_INDEX;
import static eu.vmpay.overtimetracker.repository.CalendarModel.PROJECTION_ID_INDEX;
import static eu.vmpay.overtimetracker.repository.CalendarModel.PROJECTION_OWNER_ACCOUNT_INDEX;

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

	public void requestCalendars(Context context)
	{
		// Run query
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();
		Uri uri = Calendars.CONTENT_URI;
		String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
				+ Calendars.ACCOUNT_TYPE + " = ?) AND ("
				+ Calendars.OWNER_ACCOUNT + " = ?))";
		String[] selectionArgs = new String[] { Calendars._ID,
				Calendars.CALENDAR_DISPLAY_NAME };
		// Submit the query and get a Cursor object back.
		if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
		{
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
		if(cur == null) return;
		// Use the cursor to step through the returned records
		while(cur.moveToNext())
		{
			long calID = 0;
			String displayName = null;
			String accountName = null;
			String ownerName = null;

			// Get the field values
			calID = cur.getLong(PROJECTION_ID_INDEX);
			displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
			accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
			ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

			Log.d(TAG, String.format(
					Locale.US, "calID = %d, displayName = %s, accountName = %s, ownerName = %s",
					calID, displayName, accountName, ownerName));
			// Do something with the values...
		}

		cur.close();
	}

	public Observable<List<CalendarModel>> getCalendarList()
	{
		Observable<List<CalendarModel>> observable = Observable.create(new ObservableOnSubscribe<List<CalendarModel>>()
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
				Cursor cursor = contentResolver.query(Calendars.CONTENT_URI, EVENT_PROJECTION, null, null, null);
				if(cursor == null) return;
				// Use the cursor to step through the returned records
				while(cursor.moveToNext())
				{
					long calID = 0;
					String displayName = null;
					String accountName = null;
					String ownerName = null;

					// Get the field values
					calID = cursor.getLong(PROJECTION_ID_INDEX);
					displayName = cursor.getString(PROJECTION_DISPLAY_NAME_INDEX);
					accountName = cursor.getString(PROJECTION_ACCOUNT_NAME_INDEX);
					ownerName = cursor.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

					modelList.add(new CalendarModel(calID, displayName, accountName, ownerName));

				}
				cursor.close();
				emitter.onNext(modelList);
				emitter.onComplete();
			}
		});
		return observable;
	}

}
