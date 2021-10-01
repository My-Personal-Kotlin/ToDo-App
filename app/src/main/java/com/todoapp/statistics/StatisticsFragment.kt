package com.todoapp.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.todoapp.R
import com.todoapp.TodoApplication
import com.todoapp.databinding.StatisticsFragBinding
import com.todoapp.util.setupRefreshLayout

/**
 * Main UI for the statistics screen.
 */
class StatisticsFragment : Fragment() {

    private lateinit var viewDataBinding: StatisticsFragBinding

    private val viewModel by viewModels<StatisticsViewModel>{
        StatisticsViewModelFactory(
            (requireContext().applicationContext as TodoApplication).taskRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, R.layout.statistics_frag, container,
            false
        )

        viewDataBinding.viewmodel = viewModel

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        this.setupRefreshLayout(viewDataBinding.refreshLayout)

        return viewDataBinding.root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//
//    }
}
