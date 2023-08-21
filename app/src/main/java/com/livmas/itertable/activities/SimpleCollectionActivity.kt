package com.livmas.itertable.activities

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.livmas.itertable.R
import com.livmas.itertable.databinding.ActivityCollectionBinding

abstract class SimpleCollectionActivity: CollectionActivity() {
    protected lateinit var binding: ActivityCollectionBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycler(binding.rvContent)

        binding.apply {
            tvTitle.text = resources.getString(R.string.colon, collInfo.name)
            tvType.text = collInfo.type.toString()

            fabNewItem.setOnClickListener {
                startNewListDialog()
            }
            bBack.setOnClickListener {
                finish()
            }
        }
    }
}