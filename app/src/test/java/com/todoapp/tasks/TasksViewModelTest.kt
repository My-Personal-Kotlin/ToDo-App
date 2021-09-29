package com.todoapp.tasks

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.todoapp.R
import com.todoapp.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber


@RunWith(AndroidJUnit4::class)
class TasksViewModelTest{

    // TO run everything in single thread
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Subject under test
    private lateinit var tasksViewModel: TasksViewModel

    @Before
    fun setupViewModel() {
        tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun addNewTask_setsNewTaskEvent() {

        // When adding a new task
        tasksViewModel.addNewTask()

        // Then the new task event is triggered
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()
        assertThat(
            value?.getContentIfNotHandled(), (not(nullValue())))

    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {

        // When the filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // Then the "Add task" action is visible
        assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(), `is`(true))
        // Also work like Logic
        assertThat(tasksViewModel.currentFilteringLabel.getOrAwaitValue(), `is`(R.string.label_all))

    }

}