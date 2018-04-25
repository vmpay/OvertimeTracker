package eu.vmpay.overtimetracker.calendars;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import eu.vmpay.overtimetracker.R;
import eu.vmpay.overtimetracker.ViewModelFactory;
import eu.vmpay.overtimetracker.events.EventsActivity;

public class MainActivity extends AppCompatActivity implements CalendarItemNavigator
{
	private final String TAG = "MainActivity";

	private CalendarsViewModel viewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		viewModel = obtainViewModel(this);

		viewModel.getmOpenCalendarEvent().observe(this, new Observer<Long>()
		{
			@Override
			public void onChanged(@Nullable Long calendarId)
			{
				if(calendarId != null)
				{
					openCalendarDetails(calendarId);
				}
			}
		});
	}

	public static CalendarsViewModel obtainViewModel(FragmentActivity activity)
	{
		// Use a Factory to inject dependencies into the ViewModel
		ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

		return ViewModelProviders.of(activity, factory).get(CalendarsViewModel.class);
	}

	@Override
	public void openCalendarDetails(Long calendarId)
	{
		Intent intent = new Intent(this, EventsActivity.class);
		intent.putExtra(EventsActivity.EXTRA_CALENDAR_ID, calendarId);
		startActivity(intent);
	}
}
