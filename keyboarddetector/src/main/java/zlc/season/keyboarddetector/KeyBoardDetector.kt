package zlc.season.keyboarddetector

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent

class KeyBoardDetector : PopupWindow {

    /**
     * Use this to get keyboard height.
     */
    var keyboardLiveData = MutableLiveData<Int>()
        private set

    private var lifecycle: Lifecycle? = null
    private var parentView: View? = null

    private val onGlobalLayoutListener = OnGlobalLayoutListener {
        if (contentView != null) {
            contentView.postInvalidate()
            calculateKeyboardHeight()
        }
    }

    private val showRunnable = Runnable {
        realShow()
    }

    constructor(activity: FragmentActivity) : super(activity) {
        lifecycle = activity.lifecycle
        init(activity)
    }

    constructor(fragment: Fragment) : super(fragment.context) {
        val activity = fragment.activity
        if (activity != null) {
            lifecycle = fragment.lifecycle
            init(activity)
        }
    }

    private var parentWindowBottom = 0

    @SuppressLint("InflateParams")
    private fun init(activity: FragmentActivity) {
        softInputMode = SOFT_INPUT_ADJUST_RESIZE or SOFT_INPUT_STATE_ALWAYS_VISIBLE
        inputMethodMode = INPUT_METHOD_NEEDED

        width = 0
        height = MATCH_PARENT

        val view = FrameLayout(activity)
        view.setBackgroundColor(Color.TRANSPARENT)
        view.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        contentView = view

        parentView = activity.findViewById(android.R.id.content)
        parentView?.post {
            parentWindowBottom = parentView.windowBottom()-parentView.windowTop()
            println("windowTop:${parentView.windowTop()},windowBottom:${parentWindowBottom}")
        }


        lifecycle?.addObserver(KeyboardDetectorObserver())
    }

    private fun realShow() {
        showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0)
    }

    private fun calculateKeyboardHeight() {
        val currentWindowBottom = contentView.windowBottom()-contentView.windowTop()
        println("windowTop1:${contentView.windowTop()},windowBottom1:${currentWindowBottom}")
        val height = parentWindowBottom - currentWindowBottom // + contentView.windowTop()

        if (height <= 0) {
            updateHeight(0)
        } else {
            updateHeight(height)
        }
    }

    private fun updateHeight(height: Int) {
        keyboardLiveData.value = height
    }

    private fun View?.windowBottom(): Int {
        if (this == null) return 0
        val windowRect = Rect()
        getWindowVisibleDisplayFrame(windowRect)
        return windowRect.bottom
    }

    private fun View?.windowTop(): Int {
        if (this == null) return 0
        val windowRect = Rect()
        getWindowVisibleDisplayFrame(windowRect)
        return windowRect.top
    }

    private inner class KeyboardDetectorObserver : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onStart() {
            contentView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            if (!isShowing) {
                if (parentView != null && parentView?.windowToken == null) {
                    parentView?.post(showRunnable)
                } else {
                    realShow()
                }
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {
            dismiss()
            parentView?.removeCallbacks(showRunnable)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onStop() {
            contentView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            parentView = null
            lifecycle?.removeObserver(this)
            lifecycle = null
        }
    }
}