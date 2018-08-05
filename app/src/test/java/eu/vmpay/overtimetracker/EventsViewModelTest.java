package eu.vmpay.overtimetracker;

import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import eu.vmpay.overtimetracker.events.EventsViewModel;
import eu.vmpay.overtimetracker.repository.CalendarRepository;
import eu.vmpay.overtimetracker.repository.EventModel;
import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link EventsViewModel}
 */
public class EventsViewModelTest
{
	// Executes each task synchronously using Architecture Components.
	@Rule
	public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

	@Mock
	private CalendarRepository calendarRepository;
	@Mock
	private Application application;

	private TestScheduler testScheduler;
	private EventsViewModel eventsViewModel;
	private ArrayList<EventModel> eventsModelList;

	@Before
	public void setUp()
	{
		// Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
		// inject the mocks in the test the initMocks method needs to be called.
		MockitoAnnotations.initMocks(this);

		// Mock scheduler using RxJava TestScheduler.
		testScheduler = new TestScheduler();

		// Get a reference to the class under test
		eventsViewModel = new EventsViewModel(calendarRepository, application, testScheduler, testScheduler);

		// We initialise the events to 3
		eventsModelList = new ArrayList<>();
		eventsModelList.add(new EventModel(1, 1, "Organizer1",
				"Title1", 0, 3600001, null, "rRule", "rDate"));
		eventsModelList.add(new EventModel(2, 1, "Organizer2",
				"Title1", 0, 7200002, null, "rRule", "rDate"));
		eventsModelList.add(new EventModel(3, 1, "Organizer3",
				"Title3", 0, 28800008, null, "rRule", "rDate"));

		setUpRules();
	}

	private void setUpRules()
	{
		when(application.getApplicationContext()).thenReturn(application);
		// Make repository return mocked list
		doReturn(Observable.just(eventsModelList.get(0), eventsModelList.get(1), eventsModelList.get(2)))
				.when(calendarRepository).getEvents(anyLong(), anyLong(), anyLong());
	}

	@Test
	public void startTest()
	{
		// Prepare mock data
		ArrayList<EventModel> expectedList = new ArrayList<>();//
		EventModel eventModel = new EventModel(1, 1, "Organizer1",
				"Title1", 0, 3600001, null, "rRule", "rDate");
		eventModel.setDurationHours(3.000000833333334);
		expectedList.add(eventModel);
		eventModel = new EventModel(3, 1, "Organizer3",
				"Title3", 0, 28800008, null, "rRule", "rDate");
		eventModel.setDurationHours(8.000002222222223);
		expectedList.add(eventModel);
		long mockCalendarId = 1;

		// Sets calendar id in eventsViewModel
		eventsViewModel.setCalendarId(mockCalendarId);

		// Starts viewModel
		eventsViewModel.start();

		// Trigger loading calendar action
		testScheduler.triggerActions();

		// Check calendar id
		verify(calendarRepository).getEvents(eq(mockCalendarId), anyLong(), anyLong());
		// Check if arrays are equal
		for(int i = 0; i < expectedList.size(); i++)
		{
			assertTrue(expectedList.get(i).equals(eventsViewModel.items.get(i)));
		}
	}

	@After
	public void tearDown()
	{
		eventsModelList = null;
		eventsViewModel = null;
		testScheduler = null;
	}
}
