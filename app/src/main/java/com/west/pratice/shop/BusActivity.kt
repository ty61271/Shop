package com.west.pratice.shop

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bus.*
import kotlinx.android.synthetic.main.row_bus.view.*
import org.jetbrains.anko.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.net.URL

class BusActivity : AppCompatActivity(), AnkoLogger {
    val uri =
        "https://data.tycg.gov.tw/opendata/datalist/datasetMeta/download?id=b3abedf0-aeae-4523-a804-6e807cbad589&rid=bf55b21a-2b7c-4ede-8048-f75420344aed"
    lateinit var busDatas: List<Data>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)
//        learnTask()
//        learnUseAnko()
        doAsync {
            learnRetrofit()
            uiThread {
                setRecycler(busDatas)
            }
        }


    }

    private fun learnRetrofit() {
        val baseUri = "https://data.tycg.gov.tw/opendata/datalist/datasetMeta/"
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUri)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val busService = retrofit.create(BusService::class.java)
        busDatas = busService.getDatas().execute().body()!!.datas
    }

    private fun learnUseAnko() {
        doAsync {
            parseToGson(URL(uri).readText())
            uiThread {
                setRecycler(busDatas)
            }
        }
    }

    private fun learnTask() {
        BusTask().execute(uri)
    }

    inner class BusTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String = URL(params[0]).readText()

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            info("$result")
            parseToGson(result)
            setRecycler(busDatas)
        }
    }

    private fun parseToGson(json: String?) {
        val bus = Gson().fromJson<Bus>(json, Bus::class.java)
        busDatas = bus.datas
    }

    private fun setRecycler(busDatas: List<Data>) {
        toast("Got it")
        alert("Got it", "Alert") {
            okButton {
                with(recycler_bus) {
                    layoutManager = LinearLayoutManager(this@BusActivity)
                    setHasFixedSize(true)
                    adapter = BusAdapter(busDatas)
                }
            }
        }.show()
    }

    inner class BusAdapter(datas: List<Data>) : BaseAdapter<Data>(datas) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.row_bus, parent, false)
            return BusHolder(view)
        }
    }

     class BusHolder(view: View) : BaseAdapter.BaseViewHolder<Data>(view) {
        val busIdText = view.tv_bus_id
        val routeText = view.tv_bus_route
        val speedText = view.tv_but_speed

        override fun onBind(data: Data) {
            busIdText.text = data.BusID
            routeText.text = data.RouteID
            speedText.text = data.Speed
        }
    }

}

data class Bus(
    val datas: List<Data>
)

data class Data(
    val Azimuth: String,
    val BusID: String,
    val BusStatus: String,
    val DataTime: String,
    val DutyStatus: String,
    val GoBack: String,
    val Latitude: String,
    val Longitude: String,
    val ProviderID: String,
    val RouteID: String,
    val Speed: String,
    val ledstate: String,
    val sections: String
)

interface BusService {
    @GET("download?id=b3abedf0-aeae-4523-a804-6e807cbad589&rid=bf55b21a-2b7c-4ede-8048-f75420344aed")
    fun getDatas(): Call<Bus>
}