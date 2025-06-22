package com.teedee.firetv

import androidx.lifecycle.ViewModel

class PartyViewModel : ViewModel() {
    fun generatePartyKey(): String {
        val charset = ('A'..'Z') + ('0'..'9')
        return (1..8).map { charset.random() }.joinToString("")
    }
}
