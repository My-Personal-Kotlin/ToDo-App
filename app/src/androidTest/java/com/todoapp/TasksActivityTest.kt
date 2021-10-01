package com.todoapp

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.todoapp.data.Task
import com.todoapp.data.source.TasksRepository
import com.todoapp.tasks.TasksActivity
import com.todoapp.util.DataBindingIdlingResource
import com.todoapp.util.EspressoIdlingResource
import com.todoapp.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
// Flaky Test
class TasksActivityTest {

    private lateinit var repository: TasksRepository

    // An idling resource that waits for Data Binding to have no pending bindings.
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
        repository = ServiceLocator.provideTasksRepository(getApplicationContext())
        runBlocking {
            repository.deleteAllTasks()
        }
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    //As mentioned, any setup of the data state, such as adding tasks to the repository,
    // must happen before ActivityScenario.launch() is called. Calling such additional setup code,
    // such as saving tasks to the repository, is not currently supported by ActivityScenarioRule.
    // Therefore, we choose not to use ActivityScenarioRule and instead manually call launch and close.

    @Test
    fun editTask() = runBlocking {

        // Set initial state.
        repository.saveTask(Task("TITLE1", "DESCRIPTION")) // must call before ActivityScenarioRule


        // Start up Tasks screen.
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)

        dataBindingIdlingResource.monitorActivity(activityScenario) // LOOK HERE


        // Espresso code will go here.

        // Click on the task on the list and verify that all the data is correct.
        onView(withText("TITLE1")).perform(click())
        onView(withId(R.id.task_detail_title_text)).check(matches(withText("TITLE1")))
        onView(withId(R.id.task_detail_description_text)).check(matches(withText("DESCRIPTION")))
        onView(withId(R.id.task_detail_complete_checkbox)).check(matches(not(isChecked())))

        // Click on the edit button, edit, and save.
        onView(withId(R.id.edit_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("NEW TITLE"))
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("NEW DESCRIPTION"))
        onView(withId(R.id.save_task_fab)).perform(click())

        // Verify task is displayed on screen in the task list.
        onView(withText("NEW TITLE")).check(matches(isDisplayed()))
        // Verify previous task is not displayed.
        onView(withText("TITLE1")).check(doesNotExist())



        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }

    @Test
    fun createOneTask_deleteTask() {

        // 1. Start TasksActivity.
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)

        dataBindingIdlingResource.monitorActivity(activityScenario) // LOOK HERE

        // 2. Add an active task by clicking on the FAB and saving a new task.
        onView(withId(R.id.add_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("Awais TITLE"))
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("Awais DESCRIPTION"))
        onView(withId(R.id.save_task_fab)).perform(click())

        // 3. Open the new task in a details view.
        onView(withText("Awais TITLE")).perform(click())

        // 4. Click delete task in menu.
        onView(withId(R.id.menu_delete)).perform(click())

        // 5. Verify it was deleted.
        //onView(not(withText("NEW TITLE")))
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_all)).perform(click())
        onView(withText("Awais TITLE")).check(doesNotExist())

        // 6. Make sure the activity is closed.
        activityScenario.close()
    }
}