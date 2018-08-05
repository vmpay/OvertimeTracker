package eu.vmpay.overtimetracker;

import android.Manifest;
import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import eu.vmpay.overtimetracker.calendars.CalendarsViewModel;
import eu.vmpay.overtimetracker.repository.CalendarModel;
import eu.vmpay.overtimetracker.repository.CalendarRepository;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.TestScheduler;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link CalendarsViewModel}
 */
public class CalendarsViewModelTest
{
	// Executes each task synchronously using Architecture Components.
	@Rule
	public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

	@Mock
	private CalendarRepository calendarRepository;
	@Mock
	private Application application;
	@Mock
	private RxPermissions rxPermissions;

	private CalendarsViewModel calendarsViewModel;
	private TestScheduler testScheduler;
	private static List<CalendarModel> calendarModelList;

	@Before
	public void setUp()
	{
		// Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
		// inject the mocks in the test the initMocks method needs to be called.
		MockitoAnnotations.initMocks(this);

		// Mock scheduler using RxJava TestScheduler.
		testScheduler = new TestScheduler();

		// Get a reference to the class under test
		calendarsViewModel = new CalendarsViewModel(calendarRepository, application, testScheduler, testScheduler);

		// We initialise the calendars to 3
		calendarModelList = new ArrayList<>();
		calendarModelList.add(new CalendarModel(1, "Title1", "Account1", "Owner1"));
		calendarModelList.add(new CalendarModel(2, "Title2", "Account2", "Owner2"));
		calendarModelList.add(new CalendarModel(3, "Title3", "Account3", "Owner3"));

		setUpRules();
	}

	private void setUpRules()
	{
		when(application.getApplicationContext()).thenReturn(application);
		when(rxPermissions.isGranted(Manifest.permission.READ_CALENDAR)).thenReturn(true);
		doReturn(Observable.just(true)).when(rxPermissions).request(Manifest.permission.READ_CALENDAR);
		// Make repository return mocked list
		doReturn(Single.just(calendarModelList)).when(calendarRepository).getCalendarList();
	}

	@Test
	public void permissionGrantedHappyTest()
	{
		// Check permission
		calendarsViewModel.checkPermissionGranted(rxPermissions);

		// Trigger loading calendar action
		testScheduler.triggerActions();

		// Verify results
		assertTrue(calendarsViewModel.isPermissionGranted.get());
		verify(calendarRepository).getCalendarList();
		for(int i = 0; i < calendarModelList.size(); i++)
		{
			assertEquals(calendarModelList.get(i), calendarsViewModel.items.get(i));
		}
	}

	@Test
	public void permissionDeniedHappyTest()
	{
		// Mock rxPermission object
		RxPermissions rxPermissions = mock(RxPermissions.class);
		when(rxPermissions.isGranted(Manifest.permission.READ_CALENDAR)).thenReturn(false);

		// Check permission
		calendarsViewModel.checkPermissionGranted(rxPermissions);

		// Verify result
		assertFalse(calendarsViewModel.isPermissionGranted.get());
	}

	@Test
	public void permissionCheckFailTest()
	{
		// Pass null argument
		calendarsViewModel.checkPermissionGranted(null);

		// Verify method never called
		verify(calendarRepository, never()).getCalendarList();
	}

	@Test
	public void viewModelStartHappyTest()
	{
		// Starts viewModel
		calendarsViewModel.start(rxPermissions);

		// Trigger loading calendar action
		testScheduler.triggerActions();

		// Verify results
		verify(calendarRepository).getCalendarList();
		for(int i = 0; i < calendarModelList.size(); i++)
		{
			assertEquals(calendarModelList.get(i), calendarsViewModel.items.get(i));
		}
		assertTrue(calendarsViewModel.isPermissionGranted.get());
	}

	@Test
	public void viewModelStartFailTest()
	{
		// Starts viewModel with null argument
		calendarsViewModel.start(null);

		// Verify result
		assertFalse(calendarsViewModel.isPermissionGranted.get());
	}

	@After
	public void tearDown()
	{
		calendarModelList = null;
		calendarsViewModel = null;
		testScheduler = null;
	}

}
