package com.livmas.itertable.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.livmas.itertable.MainDB
import com.livmas.itertable.NotificationReceiver
import com.livmas.itertable.activities.ComplexCollectionActivity
import com.livmas.itertable.databinding.FragmentAlarmBinding
import com.livmas.itertable.entities.Alarm
import kotlin.concurrent.thread


class AlarmFragment : Fragment() {
    lateinit var binding: FragmentAlarmBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlarmBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            bClose.setOnClickListener {
                root.visibility = View.GONE
            }
            bReset.setOnClickListener {
                SetAlarmDialog().show(requireActivity().supportFragmentManager, "alarm")
            }
            bCancel.setOnClickListener {
                val alarmManager = context?.getSystemService<AlarmManager>()
                val myIntent = Intent(context?.applicationContext, NotificationReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context?.applicationContext, 1, myIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                thread {
                    context?.let { c ->
                        MainDB.getDB(c).getDao()
                            .deleteAlarm(
                                Alarm(
                                    ComplexCollectionActivity.activeCollectionId,
                                    0,
                                    0,
                                ))}

                    alarmManager!!.cancel(pendingIntent)
                }
            }
        }
    }
}