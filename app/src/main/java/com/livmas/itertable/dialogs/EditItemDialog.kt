package com.livmas.itertable.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.livmas.itertable.DataModel
import com.livmas.itertable.R
import com.livmas.itertable.databinding.EditDialogBinding

class EditItemDialog: DialogFragment() {
    private lateinit var binding: EditDialogBinding
    private val dataModel: DataModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = EditDialogBinding.inflate(layoutInflater)
        return AlertDialog.Builder(context)
            .setView(binding.root)
            .setNegativeButton(R.string.cancel) {_, _ ->}
            .setPositiveButton(R.string.confirm) {_, _ ->
                val name = binding.etName.text.toString()
                dataModel.editItemName.value = name
            }
            .create()
    }
}