package eu.vmpay.overtimetracker.events;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import eu.vmpay.overtimetracker.R;
import eu.vmpay.overtimetracker.ViewModelFactory;

public class EventsActivity extends AppCompatActivity
{

	public static String EXTRA_CALENDAR_ID = "EXTRA_CALENDAR_ID";

	private EventsViewModel viewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		viewModel = obtainViewModel(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		viewModel.setCalendarId(getIntent().getLongExtra(EXTRA_CALENDAR_ID, -1));
	}

	public static EventsViewModel obtainViewModel(FragmentActivity activity)
	{
		// Use a Factory to inject dependencies into the ViewModel
		ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

		return ViewModelProviders.of(activity, factory).get(EventsViewModel.class);
	}
}
