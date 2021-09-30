package com.todoapp.taskdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.todoapp.data.source.TasksRepository

@Suppress("UNCHECKED_CAST")
class TaskDetailViewModelFactory (private val tasksRepository: TasksRepository)
    : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (TaskDetailViewModel(tasksRepository) as T)
}