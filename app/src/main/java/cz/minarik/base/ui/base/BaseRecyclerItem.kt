package cz.minarik.base.ui.base

interface BaseRecyclerItem {
    fun onDetachedFromWindow(holder: BaseViewHolder<Any>)
    fun onAttachedToWindow(holder: BaseViewHolder<Any>)
}