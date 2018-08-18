package com.example.ahmadreza.flickerapp

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
/**
 * Created by ahmadreza on 8/18/18.
 */
class GetRawData(val mcallback: OnDownloadCom, var mDownloadStatus: DownloadStatus = DownloadStatus.IDLE) : AsyncTask<String, Unit, String>() {

    interface OnDownloadCom{
        fun onDownloadCom(data: String, status: DownloadStatus)
    }

    fun runInSameThread(s: String) {
        println("GetRawData.runInSameThread Start")
        onPostExecute(doInBackground(s))
        println("GetRawData.runInSameThread End")
    }

    override fun doInBackground(vararg params: String?): String {

        println("GetRawData.doInBackground Start")

        var connection: HttpURLConnection? = null
        var result: StringBuilder? = null
        var reader:BufferedReader? = null

        if (params[0] == null){
            mDownloadStatus = DownloadStatus.NOTINITIALISED
            return "null"
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING

            val url = URL(params[0])
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            val response = connection.responseCode
            println("response code = ${response}")
            result = StringBuilder()
            reader = BufferedReader(InputStreamReader(connection.inputStream))

            var line = reader.readLine()

            while (null != line){
                result.append(line).append("\n")
                line = reader.readLine()
            }

            mDownloadStatus = DownloadStatus.OK
            return result.toString()

        }catch (e: MalformedURLException){
            println("GetRawData.doInBackground: MalfEx ${e.message}")
        }catch (e: IOException){
            println("GetRawData.doInBackground: IOEx ${e.message}")
        }catch (e: SecurityException){
            println("GetRawData.doInBackground: SecuEx ${e.message}")
        }
        finally {
            println("GetRawData.doInBackground End")
            if (connection != null){
                connection.disconnect()
            }
            if (reader != null){
                try {
                    reader.close()
                }catch (e: IOException){
                    println("IO EX in if")
                }
            }
        }

        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY
        return "null"
    }

    override fun onPostExecute(result: String?) {
        println("GetRawData.onPostExecute Start")
        if (mcallback != null){
            mcallback.onDownloadCom(result!!, mDownloadStatus)
        }

        println("GetRawData.onPostExecute End")
    }
}