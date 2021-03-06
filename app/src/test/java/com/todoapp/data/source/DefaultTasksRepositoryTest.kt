package com.todoapp.data.source

import com.todoapp.MainCoroutineRule
import com.todoapp.data.Result
import com.todoapp.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultTasksRepositoryTest{

    private val task1 = Task("Title1", "Description1")
    private val task2 = Task("Title2", "Description2")
    private val task3 = Task("Title3", "Description3")

    private val remoteTasks = listOf(task1, task2).sortedBy { it.id }
    private val localTasks = listOf(task3).sortedBy { it.id }
    private val newTasks = listOf(task3).sortedBy { it.id }

    private lateinit var tasksRemoteDataSource: FakeDataSource
    private lateinit var tasksLocalDataSource: FakeDataSource

    // Class under test
    private lateinit var tasksRepository: DefaultTasksRepository

    // for swapping the Dispatcher.MAIN with testCoroutineDispatcher
    // to avoid error like Dispatcher.MAINLOOPER is not initialized
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        tasksLocalDataSource = FakeDataSource(localTasks.toMutableList())
        // Get a reference to the class under test
        tasksRepository = DefaultTasksRepository(
            // TODO Dispatchers.Unconfined should be replaced with Dispatchers.Main
            //  this requires understanding more about coroutines + testing
            //  so we will keep this as Unconfined for now.
            tasksRemoteDataSource, tasksLocalDataSource, Dispatchers.Main // Main is used because we swap Dispatcher.Main with TestCoroutineScope
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getTasks_requestsAllTasksFromRemoteDataSource() = mainCoroutineRule.runBlockingTest{
        // When tasks are requested from the tasks repository
        // the repository will delete all tasks of localDataSource in getTasks() function
        val tasks = tasksRepository.getTasks(true) as Result.Success

        // Then tasks are loaded from the remote data source
        assertThat(tasks.data, IsEqual(remoteTasks))
        assertThat(tasks.data.size,`is`(2)) // because the localDataSource
        // will be Empty by getTasks method and return      tasksLocalDataSource
    }
}