package com.example.ahmadreza.flickerapp

import android.net.Uri
import android.os.AsyncTask
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by ahmadreza on 8/18/18.
 */
class GetFlickJsonData(val mcallback: OnDataAvailable?, var mBaseUrl: String, var mMatchAll: Boolean, var mLanguage: String) : AsyncTask<String, Unit, ArrayList<Photo>>(), GetRawData.OnDownloadCom {

    var mphoto: ArrayList<Photo>? = null
    var isruninsamethread = false

    interface OnDataAvailable {
        fun onDataAvailable(data: ArrayList<Photo>, status: DownloadStatus)
    }

    override fun onDownloadCom(data: String, status: DownloadStatus) {

        println("GetFlickJsonData.onDownloadCom Start")

        var st: DownloadStatus? = null

        if (status == DownloadStatus.OK) {
            st = DownloadStatus.OK
            mphoto = ArrayList()

            try {
                val jasondata = JSONObject(data)
                val itemsArray = jasondata.getJSONArray("items")
                for (i in (0 until itemsArray.length())) {
                    val jsonPhoto = itemsArray.getJSONObject(i)
                    val title = jsonPhoto.getString("title")
                    val author = jsonPhoto.getString("author")
                    val authorId = jsonPhoto.getString("author_id")
                    val tags = jsonPhoto.getString("tags")
                    val jsonmedia = jsonPhoto.getJSONObject("media")
                    val photourl = jsonmedia.getString("m")
                    var link = photourl.replace("_m.", "_b.")

                    val photoObj = Photo(title, author, authorId, link, tags, photourl)
                    mphoto!!.add(photoObj)

                    println("link = ${link}")

                    println("GetFlickJsonData.onDownloadCom::::: done")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                st = DownloadStatus.FAILED_OR_EMPTY
            }
        }

        if (isruninsamethread && mcallback != null) {
            // now inform the caller that processing is done
            mcallback.onDataAvailable(mphoto!!, st!!)
        }

        println("GetFlickJsonData.onDownloadCom End")
    }

    fun executeOnSameThread(searchCriteria: String) {
        println("GetFlickJsonData.executeOnSameThread start")
        isruninsamethread = true
        val destinationUri = creatUri(searchCriteria, mLanguage, mMatchAll)

        GetRawData(this).execute(destinationUri)

        println("GetFlickJsonData.executeOnSameThread end")
    }

    override fun doInBackground(vararg params: String?): ArrayList<Photo>? {

        println("GetFlickJsonData.doInBackground Start")
        val destinationUri = creatUri(params[0]!!, mLanguage, mMatchAll)

        GetRawData(this).runInSameThread(destinationUri!!)
        println("GetFlickJsonData.doInBackground End")
        return mphoto
    }

    override fun onPostExecute(result: ArrayList<Photo>?) {
        println("GetFlickJsonData.onPostExecute start")

        if (mcallback != null) {
            mcallback.onDataAvailable(mphoto!!, DownloadStatus.OK)
        }

        println("GetFlickJsonData.onPostExecute End")
    }

    private fun creatUri(searchCriteria: String, Language: String, MatchAll: Boolean): String? {

        return Uri.parse(mBaseUrl).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", if (MatchAll) "ALL" else "ANY")
                .appendQueryParameter("lang", Language)
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("format", "json")
                .build().toString()

    }

}