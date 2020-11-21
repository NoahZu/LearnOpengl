package github.noahzu.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder

object TextResourceReader {

    fun readTextFileFromResource(context : Context,resourceId : Int) : String{
        val body = StringBuilder()

        val inputStream = context.resources.openRawResource(resourceId)
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var nextLine : String? = ""
        do {
            nextLine = bufferedReader.readLine()
            nextLine?.run {
                body.append(this)
                body.append("\n")
            }
        } while(nextLine != null)

        return body.toString()
    }
}