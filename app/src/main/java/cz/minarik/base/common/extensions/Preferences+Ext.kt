package cz.minarik.base.common.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.format.DateUtils
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.minarik.base.R
import cz.minarik.base.common.RECYCLER_MAX_VERTICAL_OFFEST_FOR_SMOOTH_SCROLLING
import cz.minarik.base.common.prefs.PrefManager
import cz.minarik.base.common.prefs.PreferenceProperty
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import java.net.URL
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

fun Fragment.initToolbar(toolbar: Toolbar) {
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

fun Drawable.tint(context: Context, color: Int): Drawable {
    val wrapDrawable: Drawable? = DrawableCompat.wrap(this)
    return wrapDrawable?.let {
        DrawableCompat.setTint(
            it,
            ContextCompat.getColor(context, color)
        )
        it
    } ?: this
}

fun URL.getFavIcon(): String {
    return "https://www.google.com/s2/favicons?sz=64&domain_url=$host"
}


val Context.isInternetAvailable: Boolean
    get() {
        var result = false
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        return result
    }

fun moshi(): Moshi {
    return Moshi.Builder()
        .add(Date::class.java, MoshiDateAdapter().nullSafe())
        .add(KotlinJsonAdapterFactory())
        .build()
}


class MoshiDateAdapter : JsonAdapter<Date>() {
    @FromJson
    override fun fromJson(reader: JsonReader): Date? {
        return try {
            val dateAsString = reader.nextString()
            Date(dateAsString.toLong())
        } catch (e: Exception) {
            null
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Date?) {
        if (value != null) {
            writer.value(value.time)
        }
    }

}

fun Context.copyToClipBoard(label: String, text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}

@SuppressLint("RestrictedApi")
fun PopupMenu.iconizeMenu(resources: Resources, iconPadding: Int = 4.dpToPx) {
    if (menu is MenuBuilder) {
        val menuBuilder = menu as MenuBuilder
        menuBuilder.setOptionalIconsVisible(true)
        for (item in menuBuilder.visibleItems) {
            val iconMarginPx =
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    iconPadding.toFloat(),
                    resources.displayMetrics
                ).toInt()
            if (item.icon != null) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    item.icon =
                        InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
                } else {
                    item.icon =
                        object :
                            InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                            override fun getIntrinsicWidth(): Int {
                                return intrinsicHeight + iconMarginPx + iconMarginPx
                            }
                        }
                }
            }
        }
    }
}

fun RecyclerView.dividerFullWidth() {
    val listDivider = LastDividerItemDecorator(
        AppCompatResources.getDrawable(
            context,
            R.drawable.recyclerview_divider_full_horizontal
        )!!
    )
    addItemDecoration(listDivider)
}


/**
 * Custom Tabs warmUp routine with optional Uri preloading.
 */
fun Context.warmUpBrowser(uriToPreload: Uri? = null) {
    val customTabsConnection = object : CustomTabsServiceConnection() {
        override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
            client.run {
                warmup(0)
                uriToPreload?.let {
                    val customTabsSession = newSession(object : CustomTabsCallback() {})
                    val success = customTabsSession?.mayLaunchUrl(it, null, null)
                    Log.i(
                        "warmUpBrowser",
                        "Preloading url $it ${if (success == true) "SUCCESSFUL" else "FAILED"}"
                    )
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }
    //init Custom tabs services
    val success = CustomTabsClient.bindCustomTabsService(
        this,
        CHROME_PACKAGE,
        customTabsConnection
    )
    Log.i("warmUpBrowser", "Binding Custom Tabs service ${if (success) "SUCCESSFUL" else "FAILED"}")
}


fun <T> compareLists(first: List<T>, second: List<T>): Boolean {

    if (first.size != second.size) {
        return false
    }

    return first.zip(second).all { (x, y) ->
        x == y
    }
}

fun Intent.addAppReferrer(context: Context) {
    val scheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        Intent.URI_ANDROID_APP_SCHEME
    } else {
        1 shl 1
    }
    putExtra(
        Intent.EXTRA_REFERRER,
        Uri.parse("${scheme}//${context.packageName}")
    )
}

const val CHROME_PACKAGE = "com.android.chrome"

fun Context.openCustomTabs(
    uri: Uri,
    customTabsBuilder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
) {
    try {
        customTabsBuilder.setToolbarColor(
            ContextCompat.getColor(
                this,
                cz.minarik.base.R.color.colorToolbar
            )
        )
        customTabsBuilder.addDefaultShareMenuItem()

        customTabsBuilder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
        customTabsBuilder.setExitAnimations(
            this,
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )

        val intent = customTabsBuilder.build()

        intent.intent.addAppReferrer(this)

        //to prevent "choose app" dialog -> open in CustomTabs if possible
        getCustomTabsPackages(this, uri)?.let {
            for (resolveInfo in it) {
                if (resolveInfo.activityInfo.packageName == CHROME_PACKAGE) {
                    intent.intent.setPackage(CHROME_PACKAGE)
                    break
                }
            }
        }
        intent.launchUrl(this, uri)
    } catch (e: ActivityNotFoundException) {
        Log.e("openCustomTabs", e.message ?: "")
        toast(R.string.common_base_error)
    }
}

fun getCustomTabsPackages(context: Context, uri: Uri): ArrayList<ResolveInfo>? {
    val pm: PackageManager = context.packageManager
    // Get default VIEW intent handler.
    val activityIntent =
        Intent(Intent.ACTION_VIEW, uri)

    // Get all apps that can handle VIEW intents.
    val resolvedActivityList: List<ResolveInfo> = pm.queryIntentActivities(activityIntent, 0)
    val packagesSupportingCustomTabs: ArrayList<ResolveInfo> = ArrayList()
    for (info in resolvedActivityList) {
        val serviceIntent = Intent()
        serviceIntent.action = ACTION_CUSTOM_TABS_CONNECTION
        serviceIntent.setPackage(info.activityInfo.packageName)
        if (pm.resolveService(serviceIntent, 0) != null) {
            packagesSupportingCustomTabs.add(info)
        }
    }
    return packagesSupportingCustomTabs
}


fun TextView.handleHTML(context: Context) {
    movementMethod = BetterLinkMovementMethod.newInstance().apply {
        setOnLinkClickListener { textView, url ->
            context.openCustomTabs(url.toUri())
            true
        }
        setOnLinkLongClickListener { textView, url ->
            // Handle long-click or return false to let the framework handle this link.
            false
        }
        setOnClickListener {

        }
    }
}

fun String.getHostFromUrl(): String? {
    return try {
        val url = URL(this)
        url.host.replace("www.", "")
    } catch (e: Exception) {
        Log.e("getHostFromUrl", e.message ?: "")
        null
    }
}

fun RecyclerView.scrollToTop(smooth: Boolean = false) {
    val smoothScroll =
        smooth && computeVerticalScrollOffset() > RECYCLER_MAX_VERTICAL_OFFEST_FOR_SMOOTH_SCROLLING
    if (smoothScroll) {
        smoothScrollToPosition(0);
    } else {
        scrollToPosition(0)
    }
}

fun Date.toTimeElapsed(pastOnly: Boolean = true): CharSequence {
    var actualTime = time
    if (pastOnly && System.currentTimeMillis() - time < 0) actualTime =
        System.currentTimeMillis() //API sometimes returns pubDates in future :(
    return DateUtils.getRelativeTimeSpanString(
        actualTime,
        System.currentTimeMillis(),
        DateUtils.SECOND_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_ALL
    )
}

val screenWidth: Int get() = Resources.getSystem().displayMetrics.widthPixels
val screenWidthDp: Int get() = screenWidth.pxToDp
val screenHeight: Int get() = Resources.getSystem().displayMetrics.heightPixels
val screenHeightDp: Int get() = screenHeight.pxToDp


fun RecyclerView.isScrolledToTop(): Boolean {
    return !canScrollVertically(-1)
}

@Suppress("unused")
fun Fragment.setTransparentStatusBar(transparent: Boolean = true) {
    (activity as AppCompatActivity).setTransparentStatusBar(transparent)
}


fun Activity.setTransparentStatusBar(transparent: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            if (transparent) {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                statusBarColor = Color.TRANSPARENT
            } else {
                clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                statusBarColor = Color.BLACK
            }
        }
    }
}

fun Activity.hideKeyboard() {
    findViewById<View>(android.R.id.content).hideKeyboard()
}

fun Fragment.hideKeyboard() {
    (activity as AppCompatActivity).findViewById<View>(android.R.id.content)?.hideKeyboard()
}

fun Activity.openKeyboard() {
    findViewById<View>(android.R.id.content).openKeyboard()
}

fun Fragment.openKeyboard() {
    (activity as AppCompatActivity).findViewById<View>(android.R.id.content)?.openKeyboard()
}


fun hideKeyboard(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.openKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

