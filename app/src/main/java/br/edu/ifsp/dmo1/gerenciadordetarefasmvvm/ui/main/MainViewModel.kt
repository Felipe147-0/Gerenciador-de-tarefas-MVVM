package br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.data.dao.TaskDao
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.data.model.Task
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.data.model.FilterEnum

class MainViewModel : ViewModel() {
    private val dao = TaskDao

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>>
        get() = _tasks

    private val _filteredTasks = MutableLiveData<List<Task>>()
    val filteredTasks: LiveData<List<Task>>
        get() = _filteredTasks

    private val _insertTask = MutableLiveData<Boolean>()
    val insertTask: LiveData<Boolean> = _insertTask

    private val _updateTask = MutableLiveData<Boolean>()
    val updateTask: LiveData<Boolean>
        get() = _updateTask

    init {
        mock()
        load()
    }

    fun insertTask(description: String) {
        val task = Task(description, false)
        dao.add(task)
        _insertTask.value = true
        load()
    }

    fun updateTask(position: Int) {
        val task = dao.getAll()[position]
        task.isCompleted = !task.isCompleted
        _updateTask.value = true
        load()
    }

    private fun mock() {
       // dao.add(Task("Arrumar a Cama", false))
       // dao.add(Task("Retirar o lixo", false))
       // dao.add(Task("Fazer trabalho de DMO1", true))
    }

    private fun load() {
        _tasks.value = dao.getAll()
        filterTasks(FilterEnum.FILTER_ALL)
    }

    fun filterTasks(filter: FilterEnum) {
        val originalTasks = _tasks.value ?: listOf()


        val filtered = when (filter) {

            FilterEnum.FILTER_ALL -> originalTasks
            FilterEnum.FILTER_COMPLETED -> originalTasks.filter { it.isCompleted }
            FilterEnum.FILTER_NOT_COMPLETED -> originalTasks.filter { !it.isCompleted }
            else -> originalTasks
        }

        _filteredTasks.value = filtered
    }
}
