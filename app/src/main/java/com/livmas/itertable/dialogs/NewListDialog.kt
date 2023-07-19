package com.livmas.itertable.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.livmas.itertable.DataModel
import com.livmas.itertable.R
import com.livmas.itertable.databinding.ListInputDialogBinding

class NewListDialog: DialogFragment() {
    private val dataModel: DataModel by activityViewModels()
    private lateinit var binding: ListInputDialogBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = ListInputDialogBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(activity)

        builder.setView(binding.root)
            .setNegativeButton(R.string.cancel) { _, _ ->

            }
            .setPositiveButton(R.string.create) { _, _ ->
                val input = binding.etName.text.toString()
                dataModel.listName.value = input
            }
            .setMessage(R.string.new_list_dialog)

        return builder.create()
    }
}