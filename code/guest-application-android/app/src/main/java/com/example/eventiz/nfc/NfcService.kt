package com.example.eventiz.nfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.eventiz.MainActivity
import com.example.eventiz.model.NfcRack
import com.google.gson.Gson

class NfcService{
    fun resume(activity: MainActivity) {
        nfcAdapter?.let {
            if (it.isEnabled) {
                it.enableForegroundDispatch(activity, pendingIntent, intentFilters, techLists)
            }
        }

        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, intentFilters, techLists)
    }
    fun pause(activity: MainActivity) {
        nfcAdapter?.disableForegroundDispatch(activity)
    }

    fun onNewIntent(intent: Intent) {
        val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

        if (rawMessages != null) {
            for (message in rawMessages) {
                if (message is NdefMessage) {
                    for (record in message.records) {
                        val payload = record.payload
                        val data = String(payload)
                        val nfcRack = Gson().fromJson(removeTagPrefix(data), NfcRack::class.java)

                        val id = nfcRack.id

                        tagContent = id.toString()
                    }
                }
            }
        }
    }

    private fun removeTagPrefix(data: String): String {
        return data.substring(3)
    }

    fun clearTag() {
        tagContent = null
    }

    var tagContent: String? by mutableStateOf("Scan bracelet to see rack number")
    var nfcAdapter: NfcAdapter? = null
    var pendingIntent: PendingIntent? = null
    private val intentFilters = arrayOf<IntentFilter>()
    private val techLists = arrayOf<Array<String>>()
    private val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)


   init {
       tagDetected.addCategory(Intent.CATEGORY_DEFAULT)
   }
}

