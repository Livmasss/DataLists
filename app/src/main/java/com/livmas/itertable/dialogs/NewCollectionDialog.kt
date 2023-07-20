package com.livmas.itertable.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.livmas.itertable.DataModel
import com.livmas.itertable.R
import com.livmas.itertable.databinding.CollectionInputDialogBinding
import com.livmas.itertable.entities.CollectionType

class NewCollectionDialog: DialogFragment() {
    private val dataModel: DataModel by activityViewModels()
    lateinit var binding: CollectionInputDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = CollectionInputDialogBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(activity)

        builder.setView(binding.root)
            .setPositiveButton(R.string.create) { _, _ ->
                val name = binding.etCollectionName.text.toString()
                val collType = readRadioGroup(binding.rbGroup)

                if (isInputEmpty(name, collType)) {
                    Toast.makeText(
                        this.context,
                        resources.getText(R.string.enter_data_toast),
                        Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                //Writes collection data in dataModel
                dataModel.newCollType.value = collType
                dataModel.newCollName.value = name
            }
            .setNegativeButton(R.string.cancel) { _, _ ->

            }

        return builder.create()
    }

    private fun readRadioGroup(rbGroup: RadioGroup): CollectionType? {
        rbGroup.run {
            if (checkedRadioButtonId == -1)
                return null

            val checkedButton = findViewById<RadioButton>(checkedRadioButtonId)

            return when(checkedButton.text.toString()) {
                resources.getString(R.string.list) -> CollectionType.List
                resources.getString(R.string.checkbox_list) -> CollectionType.CheckList
                resources.getString(R.string.queue) -> CollectionType.Queue
                resources.getString(R.string.stack) -> CollectionType.Stack
                resources.getString(R.string.cycle) -> CollectionType.Cycle
                else -> {
                    null
                }
            }
        }
    }

    private fun isInputEmpty(name: String, type: CollectionType?) = (name == "" || type == null)
}