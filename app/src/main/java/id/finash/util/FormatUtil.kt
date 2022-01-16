package id.finash.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun timestampToString(timestamp: Timestamp): String? {
    return if(timestamp != null){
        val dateFormat = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())
        dateFormat.format( timestamp.toDate() )
    } else null
}