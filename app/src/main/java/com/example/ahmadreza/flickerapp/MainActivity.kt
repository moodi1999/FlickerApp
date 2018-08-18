package com.example.ahmadreza.flickerapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , GetFlickJsonData.OnDataAvailable{

    override fun onDataAvailable(data: ArrayList<Photo>, status: DownloadStatus) {
        if (status == DownloadStatus.OK){
            println(data)
        }
        else{
            println(status)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        /*GetRawData().execute("https://api.flickr.com/services/feeds/photos_public.gne?tags=android&format=json&nojsoncallback=1")*/
    }

    override fun onResume() {
        super.onResume()
        GetFlickJsonData(this, "https://api.flickr.com/services/feeds/photos_public.gne", true, "en-us").executeOnSameThread("android, nought")
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
