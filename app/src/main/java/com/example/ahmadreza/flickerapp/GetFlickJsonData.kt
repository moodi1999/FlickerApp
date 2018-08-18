package com.example.ahmadreza.flickerapp

import android.net.Uri
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by ahmadreza on 8/18/18.
 */
class GetFlickJsonData(val mcallback: OnDataAvailable, var mBaseUrl: String, var mMatchAll: Boolean, var mLanguage: String) : GetRawData.OnDownloadCom {

    var mphoto: ArrayList<Photo>? = null

    interface OnDataAvailable{
        fun onDataAvailable(data: ArrayList<Photo>, status: DownloadStatus)
    }

    override fun onDownloadCom(data: String, status: DownloadStatus) {

        var st: DownloadStatus? = null

        if (status == DownloadStatus.OK){
            st = DownloadStatus.OK
            mphoto = ArrayList()

            try {
                val jasondata = JSONObject(data)
                val itemsArray = jasondata.getJSONArray("items")

                for (i in (0 until itemsArray.length())){
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

                    println("GetFlickJsonData.onDownloadCom::::: done")
                }
            }
            catch (e: JSONException){
                e.printStackTrace()
                st = DownloadStatus.FAILED_OR_EMPTY
            }
        }

        if (mcallback != null){
            // now inform the caller that processing is done
            mcallback.onDataAvailable(mphoto!!, st!!)
        }
    }

    fun executeOnSameThread(searchCriteria: String){
        println("GetFlickJsonData.executeOnSameThread")
        val destinationUri = creatUri(searchCriteria, mLanguage, mMatchAll)

        GetRawData(this).execute(destinationUri)
    }

    private fun creatUri(searchCriteria: String, Language: String, MatchAll: Boolean): String? {

        return Uri.parse(mBaseUrl).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", if(MatchAll) "ALL" else "ANY")
                .appendQueryParameter("lang", Language)
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString()

    }


}