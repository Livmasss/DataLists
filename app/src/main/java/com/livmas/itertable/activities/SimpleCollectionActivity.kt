package com.livmas.itertable.activities

import android.os.Bundle
import com.livmas.itertable.R
import com.livmas.itertable.databinding.ActivityCollectionBinding
import com.livmas.itertable.entities.CollectionType

abstract class SimpleCollectionActivity: CollectionActivity() {
    protected lateinit var binding: ActivityCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycler(binding.rvContent)

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
    }
}