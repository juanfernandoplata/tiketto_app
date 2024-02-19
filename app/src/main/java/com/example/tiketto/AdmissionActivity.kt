package com.example.tiketto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AdmissionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.admission_activity)

        val vm = ViewModelProvider(this).get(AdmissionActivityVM::class.java)

        val ticket_id = intent.getIntExtra("ticket_id", -1)

        vm.retrieve_ticket_info(ticket_id)

        vm.ticketInfo.observe(this) {
            val ticket_info = it
            if(ticket_info.movie_name != "null"){
                val movie_name = findViewById<TextView>(R.id.movieName)
                val movie_date = findViewById<TextView>(R.id.movieDate)
                val ticket_num = findViewById<TextView>(R.id.ticketNum)
                val ticket_state = findViewById<TextView>(R.id.ticketState) // CHECKKKKK!!!

                movie_name.text = ticket_info.movie_name
                movie_date.text = ticket_info.movie_date
                ticket_num.text = ticket_info.ticket_num
                ticket_state.text = ticket_info.ticket_state
            }
        }

        val closeBtn = findViewById<ImageButton>(R.id.closeBtn)
        closeBtn.setOnClickListener {
            finish()
        }

        val admitBtn = findViewById<TextView>(R.id.admitBtn)
        admitBtn.setOnClickListener {
            val conn = TicketInfoRetriever.construct()

            lifecycleScope.launch {
                val res = conn.admitTicket(
                    intent.getIntExtra("ticket_id", -1)
                )
            }
        }
    }
}