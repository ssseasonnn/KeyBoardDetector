package zlc.season.keyboarddetectorapp

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import zlc.season.keyboarddetector.KeyBoardDetector

class TestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val keyBoardDetector = KeyBoardDetector(this)
        val layoutParams = edit.layoutParams as ViewGroup.MarginLayoutParams

        keyBoardDetector.keyboardLiveData.observe(this, Observer {
            if (it == null) return@Observer
            layoutParams.bottomMargin = it
            edit.layoutParams = layoutParams
        })
    }
}