package com.west.pratice.shop

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_parking.*
import org.jetbrains.anko.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.net.URL

class ParkingActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking)

        val parking =
            "http://data.tycg.gov.tw/opendata/datalist/datasetMeta/download?id=f4cc0b12-86ac-40f9-8745-885bddc18f79&rid=0daad6e6-0632-44f5-bd25-5e1de1e9146f"
//        learnTask(parking)
//        learnUseAnko(parking)
        val retrofitUrl =
            "http://data.tycg.gov.tw/opendata/datalist/datasetMeta/"
        learnRetrofit(parking, retrofitUrl)

    }

    private fun learnRetrofit(parking: String, retrofitUrl: String) {
        doAsync {
            val json = URL(parking).readText()
            val retrofit = Retrofit.Builder()
                .baseUrl(retrofitUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val parkService = retrofit.create(ParkService::class.java)
            val park = parkService.getPark().execute().body()
            uiThread {
                toast("Got it")
                tv_park_info.text = json
                alert("Got it", "Alert") {
                    okButton {
                        park?.parkingLots?.forEach {
                            info("${it.parkId} ${it.parkName} ${it.areaId} ${it.areaName}")
                        }
                    }
                }.show()
            }
        }
    }

    private fun learnUseAnko(parking: String) {
        doAsync {
            val json = URL(parking).readText()
            info(json)
            uiThread {
                toast("Got it")
                tv_park_info.text = json
                alert("Got it", "Alert") {
                    okButton {
                        parseGson(json)
                    }
                }.show()
            }
        }
    }

    private fun parseGson(json: String) {
        val park = Gson().fromJson<Park>(json, Park::class.java)
        info("${park.parkingLots.size}")
        park.parkingLots.forEach {
            info("${it.parkId} ${it.parkName} ${it.areaId} ${it.areaName}")
        }
    }

    private fun learnTask(parking: String) {
        ParkingTask().execute(parking)
    }

    //    Learn Task
    inner class ParkingTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            val url = URL(params[0])
            val json = url.readText()
            info(json)
            return json
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            toast("Got it")
            tv_park_info.text = result
        }
    }
}

/*
{
      "parkingLots" : [ {
        "areaId" : "1",
        "areaName" : "桃園區",
        "parkName" : "府前地下停車場",
        "totalSpace" : 344,
        "surplusSpace" : "133",
        "payGuide" : "停車費率:30 元/小時。停車時數未滿一小時者，以一小時計算。逾一小時者，其超過之不滿一小時部分，如不逾三十分鐘者，以半小時計算；如逾三十分鐘者，仍以一小時計算收費。",
        "introduction" : "桃園市政府管轄之停車場",
        "address" : "桃園區縣府路一號",
        "wgsX" : 121.3011,
        "wgsY" : 24.9934,
        "parkId" : "P-TY-001"
      }
   ]
}
*/
data class Park(
    val parkingLots: List<ParkingLot>
)

data class ParkingLot(
    val address: String,
    val areaId: String,
    val areaName: String,
    val introduction: String,
    val parkId: String,
    val parkName: String,
    val payGuide: String,
    val surplusSpace: String,
    val totalSpace: Int,
    val wgsX: Double,
    val wgsY: Double
)

interface ParkService {
    @GET("download?id=f4cc0b12-86ac-40f9-8745-885bddc18f79&rid=0daad6e6-0632-44f5-bd25-5e1de1e9146f")
    fun getPark(): Call<Park>
}