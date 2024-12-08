package br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.data.dao.TaskDao
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.data.model.Task

class MainViewModel : ViewModel() {
    private val dao = TaskDao

    // Lista completa de tarefas
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>>
        get() = _tasks

    // Lista filtrada de tarefas
    private val _filteredTasks = MutableLiveData<List<Task>>()
    val filteredTasks: LiveData<List<Task>>
        get() = _filteredTasks

    private val _insertTask = MutableLiveData<Boolean>()
    val insertTask: LiveData<Boolean> = _insertTask

    private val _updateTask = MutableLiveData<Boolean>()
    val updateTask: LiveData<Boolean>
        get() = _updateTask

    init {
        mock() // Adiciona tarefas mockadas
        load() // Carrega as tarefas inicialmente
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
        dao.add(Task("Arrumar a Cama", false))
        dao.add(Task("Retirar o lixo", false))
        dao.add(Task("Fazer trabalho de DMO1", true))
    }

    private fun load() {
        _tasks.value = dao.getAll()
        filterTasks("All") // Atualiza a lista filtrada inicialmente para "All"
    }

    // Função para filtrar as tarefas com base no filtro selecionado
    fun filterTasks(filter: String) {
        val originalTasks = _tasks.value ?: listOf()

        val filtered = when (filter) {

            "All" -> originalTasks
            "Completed" -> originalTasks.filter { it.isCompleted }
            "Not Completed" -> originalTasks.filter { !it.isCompleted }
            else -> originalTasks
        }

        _filteredTasks.value = filtered // Atualiza o LiveData das tarefas filtradas
    }
}
