package com.livmas.itertable.entities.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.livmas.itertable.DataModel
import com.livmas.itertable.R
import com.livmas.itertable.databinding.NewCollectionDialogBinding
import com.livmas.itertable.entities.CollectionType

class NewCollectionDialogFragment: DialogFragment() {
    private val dataModel: DataModel by activityViewModels()
    lateinit var binding: NewCollectionDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = NewCollectionDialogBinding.inflate(layoutInflater)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = it.layoutInflater

            val view = inflater.inflate(R.layout.new_collection_dialog, null)
            builder.setView(view)
                .setPositiveButton(R.string.confirm
                ) { _, _ ->
                    val editText = view.findViewById<EditText>(R.id.etCollectionName)
                    val radioGroup = view.findViewById<RadioGroup>(R.id.rbGroup)

                    val name = editText.text.toString()
                    val collType = readRadioGroup(radioGroup)

                    binding.tvTest.text = ("$name ${collType.toString()}")
                    dataModel.collectionType.value = collType
                    dataModel.collectionName.value = name
                }
                .setNegativeButton(R.string.cancel
                ) { _, _ ->

                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun readRadioGroup(rbGroup: RadioGroup): CollectionType? {
        return rbGroup.run {
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
}