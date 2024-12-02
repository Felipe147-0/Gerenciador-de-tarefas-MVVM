package br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.data.dao

import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.data.model.Task


object TaskDao {
    private var tasks: MutableList<Task> = mutableListOf()

    fun add(task: Task) {
        tasks.add(task)
    }

    fun getAll() :  List<Task> {
        tasks.sortBy { it.isCompleted }
        return tasks
    }


    fun get(id: Long): Task? {
        return tasks.stream()
            .filter{t -> t.id == id}
            .findFirst()
            .orElse(null)
    }
}

