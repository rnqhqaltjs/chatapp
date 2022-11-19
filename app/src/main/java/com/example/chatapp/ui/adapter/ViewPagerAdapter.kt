package com.example.chatapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.chatapp.ui.view.fragment.AccountBookFragment
import com.example.chatapp.ui.view.fragment.AccountCalendarFragment
import com.example.chatapp.ui.view.fragment.AccountChartFragment

class ViewPagerAdapter (fragment : FragmentActivity) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AccountBookFragment()
            1 -> AccountCalendarFragment()
            else -> AccountChartFragment()
        }
    }
}