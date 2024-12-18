package br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.ui.main

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.R
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.data.model.FilterEnum
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.databinding.ActivityMainBinding
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.databinding.DialogNewTaskBinding
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.ui.adapter.TaskAdapter
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.ui.listener.TaskClickListener

class MainActivity : AppCompatActivity(), TaskClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        configSpinner() // Configuração do Spinner
        configListView()
        configOnClickListener()
        configObservers()
    }

    override fun clickDone(position: Int) {
        viewModel.updateTask(position)
        binding.spinnerFilter.setSelection(0)
        viewModel.filterTasks(FilterEnum.FILTER_ALL)
    }

    private fun configListView() {
        adapter = TaskAdapter(this, mutableListOf(), this)
        binding.listTasks.adapter = adapter
    }

    private fun configObservers() {
        viewModel.filteredTasks.observe(this) { tasks ->
            adapter.updateTasks(tasks)
        }

        viewModel.insertTask.observe(this) { success ->
            val message = if (success) {
                getString(R.string.task_inserted_success)
            } else {
                getString(R.string.task_inserted_error)
            }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        viewModel.updateTask.observe(this) { success ->
            if (success) {
                Toast.makeText(this, getString(R.string.task_updated_success), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun configOnClickListener() {
        binding.buttonAddTask.setOnClickListener {
            openDialogNewTask()
            binding.spinnerFilter.setSelection(0)
            val filter = FilterEnum.FILTER_ALL  // Internacionalizado
            viewModel.filterTasks(filter)
        }
    }

    private fun configSpinner() {
        val filters = FilterEnum.values()
        val filterNames = filters.map { it.getDisplayName(applicationContext) }
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filterNames)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilter.adapter = spinnerAdapter

        // opção do Spinner
        binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val filter = filters[position]
                viewModel.filterTasks(filter)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun openDialogNewTask() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_new_task, null)
        val bindingDialog = DialogNewTaskBinding.bind(dialogView)

        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(getString(R.string.add_new_task))
            .setPositiveButton(getString(R.string.save)) { dialog, _ ->
                val description = bindingDialog.editDescription.text.toString()
                viewModel.insertTask(description)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }
}
