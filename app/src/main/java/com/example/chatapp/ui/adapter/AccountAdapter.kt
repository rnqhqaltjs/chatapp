package com.example.chatapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Memo
import com.example.chatapp.databinding.AccountItemBinding
import com.example.chatapp.ui.viewmodel.MemoViewModel

class AccountAdapter(val context: Context, private var memoList:List<Memo>, private val memoViewModel: MemoViewModel) : RecyclerView.Adapter<AccountAdapter.MyViewHolder>() {

    // 어떤 xml 으로 뷰 홀더를 생성할지 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = AccountItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    // 뷰 홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(memoList[position],memoViewModel)

    }

    // 뷰 홀더의 개수 리턴
    override fun getItemCount(): Int {
        return memoList.size
    }

    inner class MyViewHolder(private val binding: AccountItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(memo:Memo, memoViewModel: MemoViewModel) {

            binding.title.text = memo.title

            itemView.setOnClickListener {

//                Intent(context, ToDoInsideActivity::class.java).apply{
//                    putExtra("id", memo.id)
//                    putExtra("title", memo.title)
//                    putExtra("content", memo.content)
//                    putExtra("year", memo.year)
//                    putExtra("month", memo.month)
//                    putExtra("day", memo.day)
//                    putExtra("notifyId", memo.notifyId)
//                    context.startActivity(this)
//
//                }

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