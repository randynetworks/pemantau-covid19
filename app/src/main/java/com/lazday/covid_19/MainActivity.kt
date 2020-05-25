package com.lazday.covid_19

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.lazday.covid_19.retrofit.ApiService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    private fun getData(){
        showLoading(true)
        ApiService.endpoint.getData()
            .enqueue(object : Callback<List<MainModel>>{
                override fun onFailure(call: Call<List<MainModel>>, t: Throwable) {
                    showLoading(false)
                }

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<List<MainModel>>,
                    response: Response<List<MainModel>>
                ) {
                    showLoading(false)
                    if (response.isSuccessful){
                        val mainModel: List<MainModel> = response.body()!!
                        setResponse(mainModel)
                    }
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setResponse(mainModel: List<MainModel>){
        val response = mainModel[0]
        textPositive.setText(
            "${response.positif} Orang"
        )
        textSembuh.setText(
            "${response.sembuh} Orang"
        )
        textMeninggal.setText(
            "${response.meninggal} Orang"
        )

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd MM yyyy")
        val formatted = current.format(formatter)

        date.setText("Update Pertanggal : $formatted")

    }

    private fun showLoading(loading: Boolean) {
        when(loading){
            true  -> progressBar.visibility = View.VISIBLE
            false -> progressBar.visibility = View.GONE

        }
    }

}
