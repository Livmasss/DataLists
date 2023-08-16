package com.livmas.itertable.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.livmas.itertable.R
import com.livmas.itertable.databinding.EditDialogBinding

class EditItemDialog(private val liveData: MutableLiveData<String>, private val oldName: String): DialogFragment() {
    private lateinit var binding: EditDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = EditDialogBinding.inflate(layoutInflater)
        binding.etName.setText(oldName)
        return AlertDialog.Builder(context)
            .setView(binding.root)
            .setMessage(R.string.edit_coll_dialog)
            .setNegativeButton(R.string.cancel) {_, _ ->}
            .setPositiveButton(R.string.confirm) {_, _ ->
                val name = binding.etName.text.toString()
                liveData.value = name
            }
            .create()
    }
}