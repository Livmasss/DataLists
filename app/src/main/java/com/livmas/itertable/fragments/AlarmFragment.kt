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
import com.livmas.itertable.receivers.NotificationReceiver
import com.livmas.itertable.R
import com.livmas.itertable.activities.ComplexCollectionActivity
import com.livmas.itertable.databinding.FragmentAlarmBinding
import com.livmas.itertable.entities.Alarm
import java.util.Calendar
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
            bClose.setOnClickListener(closeListener())
            bReset.setOnClickListener(resetListener())
            bCancel.setOnClickListener(cancelListener())
        }

        initFragment()
    }

    private fun closeListener(): View.OnClickListener {
        return View.OnClickListener {
            binding.root.visibility = View.GONE
        }
    }

    private fun resetListener(): View.OnClickListener {
        return View.OnClickListener {
            SetAlarmDialog().show(requireActivity().supportFragmentManager, "alarm")
        }
    }

    private fun cancelListener(): View.OnClickListener {
        return View.OnClickListener {
            clearDisplay()

            val alarmManager = context?.getSystemService<AlarmManager>()

            val intent = Intent(context?.applicationContext, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context?.applicationContext,
                1,
                intent,
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

    fun makeVisible() {
        binding.root.visibility = View.VISIBLE
    }
    fun clearDisplay() {
        binding.apply {
            tvRepeatTime.text = getString(R.string.nul)
            tvStartTime.text = getString(R.string.nul)
            tvStartDate.text = getString(R.string.nul)
            tvIsActive.text = getString(R.string.inactive)
        }
    }
    private fun initFragment() {
        thread {
            val activity = activity as ComplexCollectionActivity
            val alarm = activity.collInfo.id?.let { context?.let { c -> MainDB.getDB(c).getDao().getAlarm(it)} } ?: return@thread

            binding.apply {
                alarm.apply {
                    if (repeat > (0).toLong()) {
                        val rCalendar = Calendar.getInstance()
                        rCalendar.timeInMillis = repeat

                        activity.runOnUiThread {
                            tvRepeatTime.text = getString(R.string.time_template,
                                rCalendar.get(Calendar.HOUR_OF_DAY), rCalendar.get(Calendar.MINUTE))
                        }
                    }
                    val sCalendar = Calendar.getInstance()
                    sCalendar.timeInMillis = lastCall

                    activity.runOnUiThread {
                        tvStartTime.text = getString(
                            R.string.time_template,
                            sCalendar.get(Calendar.HOUR_OF_DAY),
                            sCalendar.get(Calendar.MINUTE)
                        )

                        tvStartDate.text = getString(
                            R.string.date_template,
                            sCalendar.get(Calendar.DAY_OF_MONTH),
                            sCalendar.get(Calendar.MONTH),
                            sCalendar.get(Calendar.YEAR)
                        )

                        tvIsActive.text = getString(R.string.active)
                    }
                }
            }
        }
    }
}