package br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.data.dao.TaskDao
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.data.model.Task

class MainViewModel : ViewModel(){
    private val dao = TaskDao

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    init {
        mock()
        load()
    }

    private fun load(){
        _tasks.value = dao.getAll()
    }

    private fun mock(){
        dao.add(Task("Implementar exerc DMO", false))
        dao.add(Task("Tomar cafe na cantina", true))
    }

}