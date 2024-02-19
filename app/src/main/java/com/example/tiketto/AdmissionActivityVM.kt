package com.example.tiketto

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class TicketInfo (
    val movie_name : String,
    val movie_date : String,
    val ticket_num : String,
    val ticket_state: String
)

interface RetrieveTicketInfo {
    @GET("tickets/{ticket_num}")
    suspend fun tickets(
        @Path("ticket_num") ticket_num: Int
    ) : TicketInfo

    @POST("tickets/admit/{ticket_num}")
    suspend fun admitTicket(
        @Path("ticket_num") ticket_num : Int
    )
}

object TicketInfoRetriever {
    fun construct() : RetrieveTicketInfo {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrieveTicketInfo::class.java)
    }
}

class AdmissionActivityVM : ViewModel() {
    private val _ticketInfo = MutableLiveData<TicketInfo>()
    val ticketInfo: LiveData<TicketInfo> get() = _ticketInfo

    fun retrieve_ticket_info(ticket_num : Int) {
        val retriever = TicketInfoRetriever.construct()

        viewModelScope.launch {
            try {
                val ticketInfo = retriever.tickets(ticket_num)
                _ticketInfo.postValue(ticketInfo)
            } catch (e: HttpException) {
                if(e.code() == 401){
                    _ticketInfo.postValue(
                        TicketInfo(
                            "null",
                            "null",
                            "null",
                            "null"
                        )
                    )
                }
            }
        }
    }
}