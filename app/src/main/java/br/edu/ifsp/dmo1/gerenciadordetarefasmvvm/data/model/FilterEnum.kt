package br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.data.model

import android.content.Context
import br.edu.ifsp.dmo1.gerenciadordetarefasmvvm.R

enum class FilterEnum(val resId: Int){
    FILTER_ALL(R.string.filter_all),
    FILTER_COMPLETED(R.string.filter_completed),
    FILTER_NOT_COMPLETED(R.string.filter_not_completed);

    fun getDisplayName(context: Context): String {
        return context.getString(resId)
    }
}