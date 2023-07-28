package com.livmas.itertable.activities

import android.os.Bundle
import com.livmas.itertable.R
import com.livmas.itertable.databinding.ActivityComplexCollectionBinding
import com.livmas.itertable.entities.CollectionType

abstract class ComplexCollectionActivity: CollectionActivity() {
    protected lateinit var binding: ActivityComplexCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplexCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            tvTitle.text = resources.getString(R.string.colon, collInfo.name)
            tvType.text = CollectionType.valueOf(collInfo.typeId).toString()

            fabNewItem.setOnClickListener {
                startNewListDialog()
            }
            bBack.setOnClickListener {
                finish()
            }
        }

        initRecycler(binding.rvContent)
    }
}