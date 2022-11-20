package com.example.chatapp.ui.view.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.chatapp.R
import com.example.chatapp.data.db.MemoDatabase
import com.example.chatapp.data.model.Memo
import com.example.chatapp.databinding.FragmentAccountBookBinding
import com.example.chatapp.databinding.FragmentAccountChartBinding
import com.example.chatapp.ui.adapter.AccountAdapter
import com.example.chatapp.ui.viewmodel.MemoViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountChartFragment : Fragment() {

    private var _binding: FragmentAccountChartBinding? = null
    private val binding get() = _binding!!
    private val memoViewModel: MemoViewModel by viewModels() // 뷰모델 연결
    private lateinit var memodatabase: MemoDatabase
    private lateinit var pieChart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountChartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        memodatabase = MemoDatabase.getDatabase(requireContext())!!
        pieChart = binding.piechart

        memoViewModel.readAllData.observe(viewLifecycleOwner) {
            totalAll()
            memoViewModel.TotalPieChart(memodatabase,pieChart,resources)
        }

    }

    private fun totalAll() {
        val deposits = ArrayList<Int>()
        val withdraws = ArrayList<Int>()
        val prices = ArrayList<Int>()

        lifecycleScope.launch(Dispatchers.IO) {
            val value = memodatabase.memoDao().getCalendarAll()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}