package com.example.androidmobilebootcampfourthweek.fragments.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmobilebootcampfourthweek.R
import com.example.androidmobilebootcampfourthweek.responseModels.Todo
import kotlinx.android.synthetic.main.item_todo.view.*


class TodosAdapter(
    private val todoList: List<Todo>,
    private val listener: OnClickListener
) : RecyclerView.Adapter<TodosAdapter.ViewHolder>() {


    inner class ViewHolder(itemView1: View) : RecyclerView.ViewHolder(itemView1),
        View.OnClickListener {

        val checkState: ImageView = itemView1.checkState
        val todoText: TextView = itemView1.todoText
        val dividerLine: View = itemView1.dividerLine
        val deleteButton: ImageView = itemView1.deleteButton
        val completeButton: ImageView = itemView1.completeButton


        init {
            itemView1.completeButton.setOnClickListener(this)
        }

        init {
            itemView1.deleteButton.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onDeleteButtonClick(position)
            }
        }


    }

    interface OnClickListener {
        fun onDeleteButtonClick(position: Int)
        fun onCompleteButtonClick(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView1 = LayoutInflater.from(parent.context).inflate(
            R.layout.item_todo,
            parent, false
        )


        return ViewHolder(itemView1)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = todoList[position]

        fun setImageResourceOfCheckState(): Int {
            return if (!currentItem.completed) {
                R.drawable.ic_circle_checkbox_unchecked
            } else {
                R.drawable.ic_circle_checkbox_checked
            }
        }


        holder.checkState.setImageResource(setImageResourceOfCheckState())

        holder.completeButton.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                listener.onCompleteButtonClick(position)
                changeCheckBoxState(holder.checkState)
            }
        }

        holder.todoText.text = currentItem.description

    }

    override fun getItemViewType(position: Int) = position

    override fun getItemCount(): Int {
        return todoList.size
    }

    private fun changeCheckBoxState(checkState: ImageView) {
        if (checkState.resources.equals(R.drawable.ic_circle_checkbox_unchecked)) {
            checkState.setImageResource(R.drawable.ic_circle_checkbox_checked)
        } else {
            checkState.setImageResource(R.drawable.ic_circle_checkbox_unchecked)
        }
    }



}
