package br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.R
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.databinding.ActivityMainBinding
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.ui.adapter.TaskAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        configListview()
        configObservers()
    }

    private fun configListview() {
        adapter = TaskAdapter(this, mutableListOf())
        binding.listTasks.adapter = adapter
    }

    private fun configObservers() {
        viewModel.tasks.observe(this, Observer {
            adapter.updateTasks(it)
        })
    }
}