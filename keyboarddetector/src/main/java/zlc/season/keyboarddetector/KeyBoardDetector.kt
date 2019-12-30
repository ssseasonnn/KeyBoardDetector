package zlc.season.keyboarddetector

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import android.graphics.Rect
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager.LayoutParams.*
import android.widget.PopupWindow
import zlc.season.tango.getNavigationBarHeight
import zlc.season.tango.getRealScreenHeight

class KeyBoardDetector : PopupWindow {
    /**
     * Use this to get keyboard height.
     */
    var keyboardLiveData = MutableLiveData<Int>()
        private set


    /**
     * Current lifecycle
     */
    private var lifecycle: Lifecycle? = null

    /**
     * Parent view to show this popup window.
     */
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
        init(activity)
    }

    constructor(fragment: Fragment) : super(fragment.context) {
        val activity = fragment.activity
        if (activity != null) {
            init(activity)
        }
    }

    @SuppressLint("InflateParams")
    private fun init(activity: FragmentActivity) {
        softInputMode = SOFT_INPUT_ADJUST_RESIZE or SOFT_INPUT_STATE_ALWAYS_VISIBLE
        inputMethodMode = INPUT_METHOD_NEEDED

        width = 0
        height = MATCH_PARENT

        contentView = LayoutInflater.from(activity).inflate(
            R.layout.layout_keyboard_detector,
            null, false
        )


        parentView = activity.findViewById(android.R.id.content)

        lifecycle = activity.lifecycle
        lifecycle?.addObserver(KeyboardDetectorObserver())
    }


    private fun realShow() {
        showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0)
    }


    private fun calculateKeyboardHeight() {
        val realScreenSize = getRealScreenHeight()

        val windowRect = Rect()
        contentView.getWindowVisibleDisplayFrame(windowRect)

        val height = realScreenSize - windowRect.bottom - getNavigationBarHeight()

        if (height <= 0) {
            updateHeight(0)
        } else {
            updateHeight(height)
        }
    }

    private fun updateHeight(height: Int) {
        keyboardLiveData.value = height
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
