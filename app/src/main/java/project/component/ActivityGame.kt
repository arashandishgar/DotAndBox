package project.component

import android.app.Activity
import android.os.Bundle
import com.example.login.R
import kotlinx.android.synthetic.main.activity_main.*

class ActivityGame : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_HumanVsHuman.setOnClickListener {
            gameView.reset(false,false)
        }
        btn_CpuVsCpu.setOnClickListener {
            gameView.reset(true,true)

        }
        btn_HumanVsCpu.setOnClickListener {
            gameView.reset(false,true)


        }

    }

}
