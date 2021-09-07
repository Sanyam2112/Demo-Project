package com.example.vCovid.data

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import com.example.vCovid.models.countries.CountriesData
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class NetworkHandler {
    @SuppressLint("StaticFieldLeak")
    class FetchFlag(iv: ImageView, url:String) : AsyncTask<Any, Void, Bitmap?>()
    {
        var sany = iv
        var urll = url
        override fun doInBackground(vararg params: Any): Bitmap? {
            var result: String

            var connection: HttpURLConnection? = null
            try {
                val url = URL(urll)
                connection = url.openConnection() as HttpURLConnection
                val httpResult: Int =
                    connection.responseCode // Gets the status code from an HTTP response message.

                if (httpResult == HttpURLConnection.HTTP_OK)
                {
                    val inputStream = connection.inputStream
                    return BitmapFactory.decodeStream(inputStream)
                } else {
                    result = connection.responseMessage
                    Log.i("Error",result)
                }

            } catch (e: SocketTimeoutException) {
                result = "Connection Timeout"
                Log.i("Error",result)
            } catch (e: Exception) {
                result = "Error : " + e.message
                Log.i("Error",result)
            } finally {
                connection?.disconnect()
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            Log.i("JSON Response Result 1", result.toString())
            sany.setImageBitmap(result)
        }

    }


    @SuppressLint("StaticFieldLeak")
    class NetworkHandler2() : AsyncTask<Any, Void, String>()
    {

        override fun doInBackground(vararg params: Any): String {
            var result: String

            var connection: HttpURLConnection? = null
            try {
//
                val url = URL("https://api.covid19api.com/countries")
                connection = url.openConnection() as HttpURLConnection

                val httpResult: Int =
                    connection.responseCode // Gets the status code from an HTTP response message.

                if (httpResult == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val sb = StringBuilder()
                    var line: String?
                    try {
                        while (reader.readLine().also { line = it } != null) {
                            sb.append(line + "\n")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        try {
                            inputStream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    result = sb.toString()
                } else {
                    result = connection.responseMessage
                }

            } catch (e: SocketTimeoutException) {
                result = "Connection Timeout"
            } catch (e: Exception) {
                result = "Error : " + e.message
            } finally {
                connection?.disconnect()
            }

            return result
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)


//            Log.i("JSON Response Result 1", result)

            val responseData = Gson().fromJson(result, CountriesData::class.java)
            Log.i("JSON Response Result", responseData[0].country)


        }

    }
}