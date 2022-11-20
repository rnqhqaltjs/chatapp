package com.example.chatapp.ui.view.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.chatapp.data.model.Memo
import com.example.chatapp.databinding.ActivityAccountAddBinding
import com.example.chatapp.ui.viewmodel.MemoViewModel
import java.util.*


class AccountAddActivity : AppCompatActivity() {

    // 액티비티에서 인터페이스를 받아옴
    private val binding: ActivityAccountAddBinding by lazy {
        ActivityAccountAddBinding.inflate(layoutInflater)
    }
    private val MemoViewModel: MemoViewModel by viewModels()

    private var classify = 1
    private val cal = Calendar.getInstance()
    private var hour = cal.get(Calendar.HOUR_OF_DAY)
    private var minute = cal.get(Calendar.MINUTE)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var years = intent.getIntExtra("year", -1)
        var months = intent.getIntExtra("month", -1)
        var day = intent.getIntExtra("day", -1)

        binding.date.text = "${years}년 " + "${months+1}월 " + "${day}일"
        binding.time.text = String.format(Locale.KOREA, "%02d:%02d",hour,minute)

        binding.depositicon.setOnClickListener{
            binding.main.text = "수입"
            binding.main.setTextColor(Color.BLUE)
            binding.depositicon.setColorFilter(Color.BLUE)
            binding.withdrawicon.setColorFilter(Color.BLACK)
            classify = 0
        }

        binding.withdrawicon.setOnClickListener {
            binding.main.text = "지출"
            binding.main.setTextColor(Color.RED)
            binding.withdrawicon.setColorFilter(Color.RED)
            binding.depositicon.setColorFilter(Color.BLACK)
            classify = 1
        }

        binding.date.setOnClickListener {
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                years = year
                months = month
                day =dayOfMonth

                binding.date.text = "${years}년 " + "${months+1}월 " + "${day}일"

            }
            DatePickerDialog(this, dateSetListener, years,months,day).show()
        }

        binding.time.setOnClickListener {
            getTime()
        }

        binding.fab.setOnClickListener {

            val category = binding.category.text.toString()
            val description = binding.description.text.toString()

            if (category.isEmpty()) {

                Toast.makeText(this, "분류를 입력해주세요", Toast.LENGTH_SHORT).show()

            } else if(binding.amount.text.toString().isEmpty() || binding.amount.text.toString().toInt()==0){

                Toast.makeText(this, "금액을 제대로 입력해주세요", Toast.LENGTH_SHORT).show()

            } else {

                val amount = binding.amount.text.toString().toInt()

                if(classify==0){

                    val memo = Memo(0, category, description, amount,0,hour,minute, years, months, day)
                    MemoViewModel.addMemo(memo)
                    Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show()
                    finish()

                } else if(classify==1){

                    val memo = Memo(0,category, description, 0, amount,hour,minute, years, months, day)
                    MemoViewModel.addMemo(memo)
                    Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show()
                    finish()

                }

            }

        }

    }

    private fun getTime(){

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
            hour = selectedHour
            minute = selectedMinute

            binding.time.text = String.format(Locale.KOREA, "%02d:%02d",hour,minute)
        }

        TimePickerDialog(this, timeSetListener, hour, minute, true).show()

    }
}