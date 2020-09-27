package cz.minarik.base.common.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import cz.minarik.base.R
import cz.minarik.base.common.prefs.PreferenceProperty
import cz.minarik.base.common.prefs.PrefManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
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



fun RecyclerView.dividerMedium() {
    addItemDecoration(SpacesItemDecoration(8.dpToPx))
}

fun RecyclerView.horizontalDividerMedium() {
    addItemDecoration(HorizontalSpacesItemDecoration(4.dpToPx))
}

fun RecyclerView.horizontalDividerLarge() {
    addItemDecoration(HorizontalSpacesItemDecoration(16.dpToPx))
}

fun RecyclerView.dividerLarge() {
    addItemDecoration(SpacesItemDecoration(16.dpToPx))
}


class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect.bottom = space
    }
}

class HorizontalSpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect.right = space
    }
}

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

@SuppressWarnings("deprecated")
fun String.toHtml(withCdata: Boolean? = false): Spanned =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(
            when (withCdata) {
                true -> "<![CDATA[$this]]>"
                false -> this
                else -> this
            }, Html.FROM_HTML_MODE_LEGACY
        )
    } else {
        Html.fromHtml(
            when (withCdata) {
                true -> "<![CDATA[$this]]>"
                false -> this
                else -> this
            }
        )
    }

fun String.toDateFromRSS(): Date? {
    return parseDate("EEE, dd MMM yyyy HH:mm:ss zzz")
}

fun String.parseDate(pattern: String = "yyyy-MM-dd'T'HH:mm"): Date? {
    return try {
        SimpleDateFormat(pattern, Locale.ENGLISH).parse(this)
    } catch (e: Exception) {
        //todo Timber
        null
    }
}

fun Date.toShortFormat(): String {
    return DateFormat.getDateInstance(DateFormat.SHORT).format(this)
}

fun Date.toFullFormat(): String {
    return DateFormat.getDateInstance(DateFormat.FULL).format(this)
}

fun Date.toLongFormat(): String {
    return DateFormat.getDateInstance(DateFormat.LONG).format(this)
}

fun Date.toMediumFormat(): String {
    return DateFormat.getDateInstance(DateFormat.MEDIUM).format(this)
}

fun Activity.toast(message: String) {
    showToast(this, message)
}

fun Activity.toast(@StringRes resId: Int) {
    showToast(this, getString(resId))
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(message: String) {
    showToast(this, message)
}

fun Context.toast(@StringRes resId: Int) {
    showToast(this, getString(resId))
}

