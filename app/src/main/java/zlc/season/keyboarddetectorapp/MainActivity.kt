package zlc.season.keyboarddetectorapp

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import zlc.season.keyboarddetector.KeyBoardDetector
import zlc.season.tango.click

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val keyBoardDetector = KeyBoardDetector(this)
        val layoutParams = edit.layoutParams as ViewGroup.MarginLayoutParams

        keyBoardDetector.keyboardLiveData.observe(this, Observer {
            if (it == null) return@Observer
            layoutParams.bottomMargin = it
            edit.layoutParams = layoutParams
        })

        next.click {
            startActivity(Intent(this, TestActivity::class.java))
        }
    }
}
