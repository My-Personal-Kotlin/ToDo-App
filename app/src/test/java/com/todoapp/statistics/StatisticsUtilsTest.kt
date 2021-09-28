package com.todoapp.statistics

import com.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test

class StatisticsUtilsTest {

    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero() {

        // GIVEN  Create an active tasks (the false makes this active)
        val tasks = listOf<Task>(
            Task("title", "desc", isCompleted = false)
        )

        // WHEN Call our function
        val result = getActiveAndCompletedStats(tasks)

        // THEN Check the result
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(100f))
    }

    @Test
    fun getActiveAndCompletedStats_both_returnFortySixty() {

        // Create an active tasks (the false makes this active)
        val tasks = listOf<Task>(
            Task("title", "desc", isCompleted = false),
            Task("title", "desc", isCompleted = false),
            Task("title", "desc", isCompleted = false),
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = true)
        )
        // Call our function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertEquals( 40f,result.completedTasksPercent)
        assertEquals( 60f,result.activeTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_empty_returnsZero() {

        // Create an active tasks (the false makes this active)
        val tasks = emptyList<Task>()

        // Call our function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertEquals( 0f,result.completedTasksPercent)
        assertEquals( 0f,result.activeTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_error_returnsZero() {

        // Create an active tasks (the false makes this active)
        val tasks = null

        // Call our function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertEquals( 0f,result.completedTasksPercent)
        assertEquals( 0f,result.activeTasksPercent)
    }
}
