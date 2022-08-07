package com.example.recipeapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.data.models.Step
import com.example.recipeapp.databinding.StepItemBinding

class StepsAdapter : RecyclerView.Adapter<StepsAdapter.StepsViewHolder>() {

    private var steps: List<Step> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(steps: List<Step>) {
        this.steps = steps
        notifyDataSetChanged()
    }

    inner class StepsViewHolder(private val itemBinding: StepItemBinding) :
        BaseViewHolder<Step>(itemBinding.root) {
        @SuppressLint("SetTextI18n")
        override fun onBind(item: Step) {
            itemBinding.stepNumber.text = "Step ${item.number}"
            itemBinding.stepImg.text = item.number.toString()
            itemBinding.stepDisc.text = item.step
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsViewHolder {
        val view = LayoutInflater.from(parent.context)
        return StepsViewHolder(StepItemBinding.inflate(view, parent, false))
    }

    override fun onBindViewHolder(holder: StepsViewHolder, position: Int) {
        holder.onBind(steps[position])
    }

    override fun getItemCount(): Int = steps.size

}