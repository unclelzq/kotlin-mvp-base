package com.vc.base.rcy

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.vc.base.util.VIEW_THROTTLE_TIME
import java.util.concurrent.TimeUnit

//常用单type 适配器

abstract class CommonApt<T> constructor(private val dataList: MutableList<T>, val layout: Int) : androidx.recyclerview.widget.RecyclerView.Adapter<CommonViewHolder>() {

    private var onItemClickListener: OnItemClickListener<T>? = null

    private var onItemLongClickListener: OnItemLongClickListener<T>? = null

    private var selectPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder = CommonViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false), parent.context)

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.isSelect = selectPosition == position
        dispay(holder, dataList[position], position)
        RxView.clicks(holder.itemView)
                .throttleFirst(VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe {
                    onItemClickListener?.onItemClickListener(position, dataList[position], this)
                }

        RxView.longClicks(holder.itemView)
                .throttleFirst(VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe {
                    onItemLongClickListener?.onItemLongClickListener(position, dataList[position], this)
                }
    }

    override fun getItemCount(): Int = dataList.size

    private fun removeItem(position: Int) {
        dataList.remove(dataList[position])
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun removeItem(item: T) {
        val index = dataList.indexOf(item)
        removeItem(index)
    }

    fun setDataList(dataList: MutableList<T>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    fun addMoreDataList(dataList: MutableList<T>) {
        this.dataList.addAll(this.dataList.size, dataList)
        notifyItemRangeInserted(this.dataList.size, dataList.size)
    }

    fun addDataItem(newItem: T) {
        this.dataList.add(newItem)
        notifyItemRangeInserted(this.dataList.size, 1)
    }

    fun upDataItem(position: Int, item: T) {
        dataList[position] = item
        notifyItemChanged(position)
    }

    fun upDataItem(newItem: T, oldItem: T) {
        upDataItem(dataList.indexOf(oldItem), newItem)
    }

    fun setSelect(position: Int): CommonApt<T> {
        selectPosition = position
        notifyDataSetChanged()
        return this
    }

    fun getItemData(position: Int): T = dataList[position]

    abstract fun dispay(holder: CommonViewHolder, item: T, position: Int)

    fun setClick(onItemClickListener: OnItemClickListener<T>): CommonApt<T> {
        this.onItemClickListener = onItemClickListener
        return this
    }

    fun setLongClick(onItemLongClickListener: OnItemLongClickListener<T>): CommonApt<T> {
        this.onItemLongClickListener = onItemLongClickListener
        return this
    }

}

class CommonViewHolder(containerView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView) {
    private val context: Context by lazy { context }
    var isSelect: Boolean = false
}

interface OnItemClickListener<T> {
    fun onItemClickListener(position: Int, item: T, apt: CommonApt<T>)
}

interface OnItemLongClickListener<T> {
    fun onItemLongClickListener(position: Int, item: T, apt: CommonApt<T>)
}