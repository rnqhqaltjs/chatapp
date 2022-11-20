package com.example.chatapp.ui.view.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.data.model.Memo
import com.example.chatapp.databinding.ActivityAccountInsideBinding
import com.example.chatapp.ui.viewmodel.MemoViewModel
import java.util.*

class AccountInsideActivity : AppCompatActivity() {

    private val binding: ActivityAccountInsideBinding by lazy{
        ActivityAccountInsideBinding.inflate(layoutInflater)
    }

    private val MemoViewModel: MemoViewModel by viewModels() // 뷰모델 연결

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val id = intent.getIntExtra("id",-1)
        val category = intent.getStringExtra("category")
        val description = intent.getStringExtra("description")
        val deposit = intent.getIntExtra("deposit",-1)
        val withdraw = intent.getIntExtra("withdraw",-1)
        val hour = intent.getIntExtra("hour", -1)
        val minute = intent.getIntExtra("minute", -1)
        val year = intent.getIntExtra("year",-1)
        val month = intent.getIntExtra("month",-1)
        val day = intent.getIntExtra("day",-1)

        binding.category.text = category
        binding.description.text = description
        binding.time.text= String.format(Locale.KOREA, "%02d시 %02d분",hour,minute)
        binding.year.text = year.toString() + "년"
        binding.month.text = (month+1).toString() +"월"
        binding.day.text = day.toString()+ "일"

        if(deposit==0){
            binding.price.text = withdraw.toString()+"원"
            binding.price.setTextColor(Color.RED)
        } else {
            binding.price.text = deposit.toString()+"원"
            binding.price.setTextColor(Color.BLUE)
        }

        binding.deletefab.setOnClickListener {

            MemoViewModel.deleteMemo(Memo(id, category!!, description, deposit, withdraw, hour, minute, year, month, day))
            Toast.makeText(this, "삭제 완료", Toast.LENGTH_SHORT).show()
            finish()

        }

    }

}