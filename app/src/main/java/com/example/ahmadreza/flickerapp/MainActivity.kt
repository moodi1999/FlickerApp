package com.example.ahmadreza.flickerapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), GetFlickJsonData.OnDataAvailable , RecyclerItemClickListener.OnRecyclerClicklistenr {

    override fun onItemLongClick(view: View, position: Int) {
        println("MainActivity.onItemLongClick")
    }

    override fun onItemClick(view: View, position: Int) {
        println("MainActivity.onItemClick")
    }

    var adaptor: FlickerRecyclerViewAdaptor? = null

    override fun onDataAvailable(data: ArrayList<Photo>, status: DownloadStatus) {
        println("MainActivity.onDataAvailable Start")
        if (status == DownloadStatus.OK) {
            adaptor!!.loadNewData(data)
        } else {
            println(status)
        }
        println("MainActivity.onDataAvailable End")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this, recyclerView, this))
        adaptor = FlickerRecyclerViewAdaptor( ArrayList<Photo>(), this)
        recyclerView.adapter = adaptor

        println("MainActivity.onCreate")

        /*GetRawData().execute("https://api.flickr.com/services/feeds/photos_public.gne?tags=android&format=json&nojsoncallback=1")*/
    }

    override fun onResume() {
        super.onResume()
        println("MainActivity.onResume start")
        GetFlickJsonData(this, "https://api.flickr.com/services/feeds/photos_public.gne", true, "en-us").execute("android")
        println("MainActivity.onResume End")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
