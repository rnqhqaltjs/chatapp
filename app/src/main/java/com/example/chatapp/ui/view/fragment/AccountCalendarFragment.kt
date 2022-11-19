package com.example.chatapp.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.data.db.MemoDatabase
import com.example.chatapp.data.model.Memo
import com.example.chatapp.databinding.FragmentAccountBookBinding
import com.example.chatapp.databinding.FragmentAccountCalendarBinding
import com.example.chatapp.ui.adapter.AccountAdapter
import com.example.chatapp.ui.viewmodel.MemoViewModel

class AccountCalendarFragment : Fragment() {

    private var _binding: FragmentAccountCalendarBinding? = null
    private val binding get() = _binding!!
    private val memoViewModel: MemoViewModel by viewModels() // 뷰모델 연결
    private val memoList : List<Memo> = listOf()
    private val adapter : AccountAdapter by lazy { AccountAdapter(requireContext(),memoList,memoViewModel) } // 어댑터 선언
    private lateinit var memodatabase: MemoDatabase

    private var year : Int = 0
    private var month : Int = 0
    private var day : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountCalendarBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        memodatabase = MemoDatabase.getDatabase(requireContext())!!

        // 아이템에 아이디를 설정해줌 (깜빡이는 현상방지)
        if (!adapter.hasObservers()) {
            adapter.setHasStableIds(true)
        }

        // 아이템을 가로로 하나씩 보여주고 어댑터 연결
        binding.recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        binding.recyclerView.adapter = adapter

        binding.calendarView.setOnDateChangedListener { widget, date, selected ->

            year = date.year
            month = date.month
            day = date.day

            // 해당 날짜 데이터를 불러옴 (currentData 변경)
            memoViewModel.readDateData(year,month,day)
        }

        // 메모 데이터가 수정되었을 경우 날짜 데이터를 불러옴 (currentData 변경)
        memoViewModel.readAllData.observe(viewLifecycleOwner) {
            memoViewModel.readDateData(year, month, day)
            memoViewModel.dotDecorator(requireContext(),binding.calendarView,memodatabase)
        }

        // 현재 날짜 데이터 리스트(currentData) 관찰하여 변경시 어댑터에 전달해줌
        memoViewModel.currentData.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        binding.fab.setOnClickListener {

            if(year == 0) {
                Toast.makeText(activity, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                onFabClicked()
            }

        }


    }

    // Fab 클릭시 사용되는 함수
    private fun onFabClicked(){
//        Intent(requireContext(), ToDoAddActivity::class.java).apply{
//            putExtra("year",year)
//            putExtra("month",month)
//            putExtra("day",day)
//            startActivity(this)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}