package com.example.myapplication.tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.db.DbUtils
import com.example.myapplication.model.TaskInfo
import com.example.myapplication.utils.AppHelper
import com.example.myapplication.utils.CustomDialog


class TaskItemAdapter(private val context: Context, private val mList: ArrayList<TaskInfo>) :
    RecyclerView.Adapter<TaskItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tasks, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewModel = mList[position]
        holder.tvTitle.text = itemViewModel.task
        holder.tvTime.text = AppHelper.getDateFromMilliseconds(itemViewModel.time)
        if (itemViewModel.isSelected) {
            holder.checkUncheck.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_check
                )
            )
        } else {
            holder.checkUncheck.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_uncheck
                )
            )
        }
        holder.deleteTask.setOnClickListener {
            val customDialog = CustomDialog(
                title = context.getString(R.string.confirmation_title),
                message = context.getString(
                    R.string.confirmation_message,
                    itemViewModel.task.trim()
                ),
                positiveButton = context.getString(R.string.confirmation_ok),
                negativeButton = context.getString(R.string.confirmation_cancel)
            ) { _, _ ->
                DbUtils.deleteTask(itemViewModel)
            }
            customDialog.show((context as AppCompatActivity).supportFragmentManager, "confirm")
        }
        holder.checkUncheck.setOnClickListener {
            val updatedValue = !itemViewModel.isSelected
            mList[position].isSelected = updatedValue
            notifyItemChanged(position, mList[position])
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val deleteTask: ImageView = itemView.findViewById(R.id.ivClose)
        val checkUncheck: ImageView = itemView.findViewById(R.id.ivCheckUncheck)
    }
}