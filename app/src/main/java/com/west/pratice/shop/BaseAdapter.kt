package com.west.pratice.shop

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(private var master: List<T> = listOf()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = master.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BaseViewHolder<T>).onBind(master[position])
    }

    abstract class BaseViewHolder<E>(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(e: E)
    }

}

