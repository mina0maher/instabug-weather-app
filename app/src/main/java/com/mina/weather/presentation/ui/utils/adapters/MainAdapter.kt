package com.mina.weather.presentation.ui.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MainAdapter<T>(
    private val items: List<T>,
    private val layoutId: Int,
    private val bind: (T, View) -> Unit
) : RecyclerView.Adapter<MainAdapter.GenericViewHolder<T>>() {

    class GenericViewHolder<T>(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return GenericViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        bind(items[position], holder.view)
    }

    override fun getItemCount(): Int = items.size
}