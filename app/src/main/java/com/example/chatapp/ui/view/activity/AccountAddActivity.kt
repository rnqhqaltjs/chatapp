package com.example.chatapp.ui.view.activity

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.chatapp.R
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

    private val cal = Calendar.getInstance()
    private val deposit = 0
    private val withdraw = 0
    private var classify = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val year = intent.getIntExtra("year", -1)
        val month = intent.getIntExtra("month", -1)
        val day = intent.getIntExtra("day", -1)

        binding.year.text = year.toString()
        binding.month.text = month.toString()
        binding.day.text = day.toString()

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

        binding.fab.setOnClickListener {

            val category = binding.category.text.toString()
            val description = binding.description.text.toString()
            val amount = binding.amount.text.toString().toInt()

            if (title.isEmpty()) {

                Toast.makeText(this, "분류를 입력해주세요", Toast.LENGTH_SHORT).show()

            } else if(amount==0){

                Toast.makeText(this, "금액을 입력해주세요", Toast.LENGTH_SHORT).show()

            } else {


                if(classify==0){

                    val memo = Memo(0, category, description, amount,0, year, month, day)
                    MemoViewModel.addMemo(memo)
                    Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show()
                    finish()

                } else if(classify==1){

                    val memo = Memo(0,category, description, 0, amount, year, month, day)
                    MemoViewModel.addMemo(memo)
                    Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show()
                    finish()

                }

            }

        }

    }
}