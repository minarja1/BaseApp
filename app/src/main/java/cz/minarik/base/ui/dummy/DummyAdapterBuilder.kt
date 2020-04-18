package cz.minarik.base.ui.dummy

import android.content.Context
import android.view.View
import cz.minarik.base.R
import cz.minarik.base.data.rest.dto.response.DummyResponse
import cz.minarik.base.ui.base.BaseListAdapterBuilder
import kotlinx.android.synthetic.main.row_dummy.view.*

class DummyAdapterBuilder(context: Context, private val onClick: (DummyResponse) -> Unit) :
    BaseListAdapterBuilder<DummyResponse>(context) {

    override fun getRowLayout() = R.layout.row_dummy

    override fun View.onBind(item: DummyResponse) {
        // ToDo bind
        name.text = item.title
        descripiton.text = item.body

        setOnClickListener {
            onClick(item)
        }
    }
}