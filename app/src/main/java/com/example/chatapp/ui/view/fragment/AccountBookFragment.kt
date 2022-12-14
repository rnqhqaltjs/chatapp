package com.example.chatapp.ui.view.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.data.db.MemoDatabase
import com.example.chatapp.data.model.Memo
import com.example.chatapp.databinding.FragmentAccountBookBinding
import com.example.chatapp.ui.adapter.AccountAdapter
import com.example.chatapp.ui.view.activity.AccountAddActivity
import com.example.chatapp.ui.viewmodel.MemoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AccountBookFragment : Fragment() {

    private var _binding: FragmentAccountBookBinding? = null
    private val binding get() = _binding!!
    private val memoList : List<Memo> = listOf()
    private val adapter : AccountAdapter by lazy { AccountAdapter(requireContext(), memoList) } // 어댑터 선언
    private val memoViewModel: MemoViewModel by viewModels() // 뷰모델 연결
    private lateinit var memodatabase: MemoDatabase

    private val calendar = Calendar.getInstance()
    private var currentYear = calendar.get(Calendar.YEAR)
    private var currentMonth = calendar.get(Calendar.MONTH)
    private var currentDate = calendar.get(Calendar.DATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBookBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        memodatabase = MemoDatabase.getDatabase(requireContext())!!

        // 아이템에 아이디를 설정해줌 (깜빡이는 현상방지)
        if (!adapter.hasObservers()) {
            adapter.setHasStableIds(true)
        }

        binding.dateFormatted.text = "${currentYear}년 " + "${currentMonth+1}월"

        // 아이템을 가로로 하나씩 보여주고 어댑터 연결
        binding.recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(),LinearLayoutManager.VERTICAL))

            binding.plusmonth.setOnClickListener {
                currentMonth += 1
                binding.dateFormatted.text = "${currentYear}년 " + String.format("%02d", currentMonth+1)+"월"
                memoViewModel.readMonthData(currentYear,currentMonth)
                totalAll()

        }

        binding.minusmonth.setOnClickListener {
            currentMonth -= 1
            binding.dateFormatted.text = "${currentYear}년 " + String.format("%02d", currentMonth+1)+"월"
            memoViewModel.readMonthData(currentYear,currentMonth)
            totalAll()

        }

        // 메모 데이터가 수정되었을 경우 날짜 데이터를 불러옴 (currentData 변경)
        memoViewModel.readAllData.observe(viewLifecycleOwner) {
            memoViewModel.readMonthData(currentYear,currentMonth)
            totalAll()
        }

        // 현재 날짜 데이터 리스트(currentData) 관찰하여 변경시 어댑터에 전달해줌
        memoViewModel.currentData.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        binding.fab.setOnClickListener {

            onFabClicked()

        }


    }

    private fun totalAll() {
        val deposits = ArrayList<Int>()
        val withdraws = ArrayList<Int>()
        val prices = ArrayList<Int>()

        lifecycleScope.launch(Dispatchers.IO) {
            val value = memodatabase.memoDao().readMonthData(currentYear, currentMonth)
            if(value.isNotEmpty()){
                for (i in value.indices) {
                    val price = value[i].withdraw
                    val price2 = value[i].deposit
                    prices.add(-price)
                    prices.add(price2)
                    deposits.add(price2)
                    withdraws.add(price)
                }
            }

            withContext(Dispatchers.Main) {

                binding.deposit.text = deposits.sum().toString() + "원"
                binding.withdraw.text = withdraws.sum().toString() + "원"
                binding.total.text = prices.sum().toString() + "원"
                if(prices.sum()>0){
                    binding.total.setTextColor(Color.BLUE)
                } else if(prices.sum()<0){
                    binding.total.setTextColor(Color.RED)
                } else{
                    binding.total.setTextColor(Color.BLACK)
                }

            }

        }
    }

    // Fab 클릭시 사용되는 함수
    private fun onFabClicked(){
        Intent(requireContext(), AccountAddActivity::class.java).apply{
            putExtra("year",calendar.get(Calendar.YEAR))
            putExtra("month",calendar.get(Calendar.MONTH))
            putExtra("day",currentDate)
            startActivity(this)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}