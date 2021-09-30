package com.todoapp.statistics

import android.app.Application
import androidx.lifecycle.*
import com.todoapp.TodoApplication
import com.todoapp.data.Result
import com.todoapp.data.Task
import com.todoapp.data.source.DefaultTasksRepository
import kotlinx.coroutines.launch
import com.todoapp.data.Result.Success

/**
 * ViewModel for the statistics screen.
 */
class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    // Note, for testing and architecture purposes, it's bad practice to construct the repository
    // here. We'll show you how to fix this during the codelab
    private val tasksRepository = (application as TodoApplication).taskRepository


    private val tasks: LiveData<Result<List<Task>>> = tasksRepository.observeTasks()
    val error: LiveData<Boolean> = tasks.map { it is Error }
    val empty: LiveData<Boolean> = tasks.map { (it as? Success)?.data.isNullOrEmpty() }
    private val stats: LiveData<StatsResult?> = tasks.map {
        if (it is Success) {
            getActiveAndCompletedStats(it.data)
        } else {
            null
        }
    }
    val activeTasksPercent = stats.map { it?.activeTasksPercent ?: 0f }
    val completedTasksPercent: LiveData<Float> = stats.map { it?.completedTasksPercent ?: 0f }


    private val _dataLoading = MutableLiveData<Boolean>(false)
    val dataLoading: LiveData<Boolean> = _dataLoading
    fun refresh() {
        _dataLoading.value = true
            viewModelScope.launch {
                tasksRepository.refreshTasks()
                _dataLoading.value = false
            }
    }
}