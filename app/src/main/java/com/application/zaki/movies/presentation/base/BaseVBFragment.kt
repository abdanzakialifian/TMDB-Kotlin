package com.application.zaki.movies.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseVBFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
//        handleBottomNavigationView()
    }

//    private fun handleBottomNavigationView() {
//        if (isShowBottomNavigationView()) {
//            val bottomNavigationView =
//                activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
//            bottomNavigationView?.visible()
//        } else {
//            val bottomNavigationView =
//                activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
//            bottomNavigationView?.gone()
//        }
//    }

//    abstract fun isShowBottomNavigationView(): Boolean

    abstract fun getViewBinding(): VB

    abstract fun initView()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}