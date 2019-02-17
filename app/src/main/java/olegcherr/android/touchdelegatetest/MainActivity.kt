package olegcherr.android.touchdelegatetest

import android.graphics.Rect
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val rootView get() = findViewById<ViewGroup>(R.id.rootView)
    private val targetView get() = findViewById<View>(R.id.targetView)
    private val logTextView get() = findViewById<TextView>(R.id.logTextView)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        targetView.setOnTouchListener { v, event ->
            log("TARGET event=${event.name}")
            true
        }

        rootView.setOnTouchListener { v, event ->
            log("ROOT event=${event.name}")
            v.touchDelegate!!.onTouchEvent(event)
        }

        rootView.post {
            val rect = Rect()
            val inset = (-20).dp

            targetView.getHitRect(rect)
            rect.inset(inset, inset)
            rootView.touchDelegate = TouchDelegate(rect, targetView)
        }
    }


    private val Int.dp get() = Math.round(this * resources.displayMetrics.density)

    private val MotionEvent.name: String?
        get() {
            return when (action) {
                MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
                MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
                MotionEvent.ACTION_UP -> "ACTION_UP"
                MotionEvent.ACTION_CANCEL -> "ACTION_CANCEL"
                else -> null
            }
        }


    private fun log(msg: String) {
        logTextView.text = StringBuilder(logTextView.text).insert(0, "${SystemClock.uptimeMillis()} $msg\n")
    }
}
