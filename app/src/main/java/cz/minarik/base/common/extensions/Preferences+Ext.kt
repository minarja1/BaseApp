package cz.minarik.base.common.extensions

import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import cz.minarik.base.R
import cz.minarik.base.common.prefs.PreferenceProperty
import cz.minarik.base.common.prefs.PrefManager
import kotlin.properties.ReadWriteProperty

fun PrefManager.longPreference(
    key: String,
    defaultValue: Long = 0L
): ReadWriteProperty<Any, Long> =
    PreferenceProperty(
        sharedPreferences = this.shared,
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getLong,
        setter = SharedPreferences.Editor::putLong
    )

fun PrefManager.intPreference(
    key: String,
    defaultValue: Int = 0
): ReadWriteProperty<Any, Int> =
    PreferenceProperty(
        sharedPreferences = this.shared,
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getInt,
        setter = SharedPreferences.Editor::putInt
    )

fun PrefManager.booleanPreference(
    key: String,
    defaultValue: Boolean = false
): ReadWriteProperty<Any, Boolean> =
    PreferenceProperty(
        sharedPreferences = this.shared,
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getBoolean,
        setter = SharedPreferences.Editor::putBoolean
    )

fun PrefManager.stringPreference(
    key: String,
    defaultValue: String? = null
): ReadWriteProperty<Any, String?> =
    PreferenceProperty(
        sharedPreferences = this.shared,
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getString,
        setter = SharedPreferences.Editor::putString
    )


fun RecyclerView.divider() {
    val listDivider = LastDividerItemDecorator(
        AppCompatResources.getDrawable(
            context,
            R.drawable.recyclerview_divider_16dp_vertical
        )!!
    )
    addItemDecoration(listDivider)
}

fun Fragment.initToolbar(toolbar: Toolbar){
    val navController = NavHostFragment.findNavController(this)
    val appBarConfiguration = AppBarConfiguration(navController.graph)
    toolbar.setupWithNavController(navController, appBarConfiguration)
}

class LastDividerItemDecorator(private val mDivider: Drawable) : RecyclerView.ItemDecoration() {
    private val mBounds = Rect()

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()

        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0..childCount - 2) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + mDivider.intrinsicHeight

            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mDivider.draw(canvas)
        }

        canvas.restore()
    }
}