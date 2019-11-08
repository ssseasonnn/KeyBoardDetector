package zlc.season.keyboarddetectorapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.add(R.id.frame_layout, TestFragment())
        beginTransaction.commit()
    }
}