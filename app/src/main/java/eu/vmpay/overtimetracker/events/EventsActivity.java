package eu.vmpay.overtimetracker.events;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.util.Calendar;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.menu_data_pick:
				openDatePicker();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void openDatePicker()
	{
		if(viewModel != null)
		{
			Calendar now = Calendar.getInstance();
			DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
					viewModel,
					now.get(Calendar.YEAR),
					now.get(Calendar.MONTH),
					now.get(Calendar.DAY_OF_MONTH)
			);
			datePickerDialog.show(getFragmentManager(), "DatepickerDialog");
		}
	}
}
