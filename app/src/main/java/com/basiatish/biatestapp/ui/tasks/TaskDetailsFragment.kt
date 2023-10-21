package com.basiatish.biatestapp.ui.tasks

import android.animation.LayoutTransition
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.navigation.fragment.findNavController
import com.basiatish.biatestapp.App
import com.basiatish.biatestapp.databinding.TaskDetailsFooterVariant1Binding
import com.basiatish.biatestapp.databinding.TaskDetailsFooterVariant2Binding
import com.basiatish.biatestapp.databinding.TaskDetailsFragmentBinding
import com.basiatish.domain.entities.Task
import androidx.navigation.fragment.navArgs
import android.Manifest
import androidx.core.app.ActivityCompat
import javax.inject.Inject

class TaskDetailsFragment : Fragment() {

    private var _binding: TaskDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val navArgs: TaskDetailsFragmentArgs by navArgs()

    private lateinit var _footerBinding: Any
    private val footerBinding get() = _footerBinding
    private var footerFlag = false

    private var taskID = 0
    private var incidentText: String? = null

    @Inject lateinit var viewModel: TaskDetailsViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.tasksComponent().create().inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TaskDetailsFragmentBinding.inflate(layoutInflater, container, false)
        taskID = navArgs.taskId
        viewModel.getTask(taskID)
        viewModel.getIncident(taskID)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.task.observe(this.viewLifecycleOwner) {
            if (it != null) {
                bind(it)
                binding.downloadButton.isEnabled = true
                setupTextViewActions()
                setupListeners()
                setupFooterObservers()
            }
        }
    }

    private fun setupListeners() {
        binding.downloadButton.setOnClickListener {
            (requireActivity().application as App).downLoad(
                binding.taskDetailsView.contactName.text.toString(),
                taskID)
            binding.downloadButton.isEnabled = false
        }
        setupFooterListeners()
    }


    private fun setupFooterObservers() {
        viewModel.incident.observe(this.viewLifecycleOwner) {
            if (it != null && footerFlag) {
                addBadge()
                incidentText = it.text
            }
        }
    }

    private fun setupFooterListeners() {
        if (!footerFlag) {
            (footerBinding as TaskDetailsFooterVariant1Binding).declineButton.setOnClickListener {
                removeFooter()
                viewModel.updateTaskStatus(taskID, "Decline")
                binding.downloadButton.isEnabled = false
            }
            (footerBinding as TaskDetailsFooterVariant1Binding).submitButton.setOnClickListener {
                removeFooter()
                viewModel.updateTaskStatus(taskID, "Current")
                addView("Current")
            }
        } else {
            (footerBinding as TaskDetailsFooterVariant2Binding).incidentContainer.setOnClickListener {
                val action = TaskDetailsFragmentDirections.actionTaskDetailsFragmentToIncidentFragment(taskID, incidentText)
                findNavController().navigate(action)
            }
            (footerBinding as TaskDetailsFooterVariant2Binding).declineTaskButton.setOnClickListener {
                removeFooter()
                viewModel.updateTaskStatus(taskID, "Decline")
                binding.downloadButton.isEnabled = false
            }
            (footerBinding as TaskDetailsFooterVariant2Binding).documentsContainer.setOnClickListener {
                val action = TaskDetailsFragmentDirections
                    .actionTaskDetailsFragmentToTaskDocumentsFragment(taskID,
                            binding.taskDetailsView.contactName.text.toString())
                findNavController().navigate(action)
            }
        }
    }

    private fun setupTextViewActions() {
        binding.taskDetailsView.startPoint.setOnClickListener {
            val address = "${binding.taskDetailsView.city.text} ${binding.taskDetailsView.startPoint.text}"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("geo:0,0?q=${address}")
            startActivity(intent)
        }
        binding.taskDetailsView.endPoint.setOnClickListener {
            val address = "${binding.taskDetailsView.city.text} ${binding.taskDetailsView.endPoint.text}"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("geo:0,0?q=${address}")
            startActivity(intent)
        }
        binding.taskDetailsView.contact.setOnClickListener {
            val phone = binding.taskDetailsView.contact.text
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.fromParts("tel", phone.toString(), null)
            startActivity(intent)
        }
    }

    private fun bind(task: Task) {
        binding.apply {
            taskDetailsView.apply {
                cargoTypeDetails.text = task.cargoType
                city.text = task.city
                date.text = task.date
                time.text = task.time
                startPoint.text = task.startPoint
                endPoint.text = task.endPoint
                bodyType.text = task.bodyType
                order.text = task.orderDetails
                payDetails.text = task.payDetails
                contact.text = task.phone
                contactName.text = task.name
            }
        }
        addView(task.status)
    }

    private fun addView(status: String) {
        val container = binding.taskViewContainer
        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT)
        if (status == "Available") {
            _footerBinding = TaskDetailsFooterVariant1Binding.inflate(LayoutInflater.from(requireContext()))
            val view = (footerBinding as TaskDetailsFooterVariant1Binding).root
            view.layoutParams = params
            (view.layoutParams as ConstraintLayout.LayoutParams).topToBottom =
                binding.clientRulesContainer.id
            (container as ViewGroup).addView(view, params)
            footerFlag = false
        } else if (status == "Current") {
            _footerBinding = TaskDetailsFooterVariant2Binding.inflate(LayoutInflater.from(requireContext()))
            val view = (footerBinding as TaskDetailsFooterVariant2Binding).root
            view.layoutParams = params
            (view.layoutParams as ConstraintLayout.LayoutParams).topToBottom =
                binding.clientRulesContainer.id
            (container as ViewGroup).addView(view, params)
            footerFlag = true
        } else if (status == "Done") {
            binding.downloadButton.isEnabled = false
        }
    }

    private fun removeFooter() {
        val transition = LayoutTransition()
        transition.setDuration(500)
        transition.setInterpolator(LayoutTransition.DISAPPEARING, LinearOutSlowInInterpolator())
        transition.enableTransitionType(LayoutTransition.DISAPPEARING)
        val container = binding.taskViewContainer
        container.layoutTransition = transition
        if (!footerFlag) {
            val view = (footerBinding as TaskDetailsFooterVariant1Binding).root
            (container as ViewGroup). removeView(view)
        } else {
            val view = (footerBinding as TaskDetailsFooterVariant2Binding).root
            (container as ViewGroup). removeView(view)
        }
    }
    private fun  addBadge() {
        (footerBinding as TaskDetailsFooterVariant2Binding).badgeContainer.visibility = View.VISIBLE
        (footerBinding as TaskDetailsFooterVariant2Binding).badgeText.text = "1"
    }

}