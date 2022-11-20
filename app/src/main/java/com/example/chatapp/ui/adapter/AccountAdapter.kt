package com.example.chatapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Memo
import com.example.chatapp.databinding.AccountItemBinding
import com.example.chatapp.ui.view.activity.AccountInsideActivity
import com.example.chatapp.ui.viewmodel.MemoViewModel
import java.util.*

class AccountAdapter(val context: Context, private var memoList: List<Memo>) : RecyclerView.Adapter<AccountAdapter.MyViewHolder>() {

    // 어떤 xml 으로 뷰 홀더를 생성할지 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = AccountItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    // 뷰 홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(memoList[position])

    }

    // 뷰 홀더의 개수 리턴
    override fun getItemCount(): Int {
        return memoList.size
    }

    inner class MyViewHolder(private val binding: AccountItemBinding) : RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun bind(memo: Memo) {

            binding.category.text= memo.category
            if(memo.withdraw==0){
                binding.price.text = "+"+memo.deposit.toString()+"원"
                binding.price.setTextColor(Color.BLUE)
            } else {
                binding.price.text = "-"+memo.withdraw.toString()+"원"
                binding.price.setTextColor(Color.RED)
            }

            binding.date.text = memo.year.toString().replace("20","")+"년 "+(memo.month+1).toString()+"월 "+memo.day.toString()+"일"
            binding.time.text= String.format(Locale.KOREA, "%02d시 %02d분",memo.hour,memo.minute)


            itemView.setOnClickListener {

                Intent(context, AccountInsideActivity::class.java).apply{
                    putExtra("id", memo.id)
                    putExtra("category", memo.category)
                    putExtra("description", memo.description)
                    putExtra("year", memo.year)
                    putExtra("month", memo.month)
                    putExtra("day", memo.day)
                    putExtra("hour", memo.hour)
                    putExtra("minute", memo.minute)
                    putExtra("deposit", memo.deposit)
                    putExtra("withdraw", memo.withdraw)
                    context.startActivity(this)

                }

            }

        }

    }

    // 메모 리스트 갱신
    fun setData(memo : List<Memo>){
        memoList = memo
        notifyDataSetChanged()
    }

    // 아이템에 아이디를 설정해줌 (깜빡이는 현상방지)
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}