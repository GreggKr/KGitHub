package me.greggkr.kgithub.wrappers

import com.google.gson.annotations.SerializedName

data class File(
        val size: Long,
        @SerializedName("raw_url") val url: String,
        val type: String,
        val truncated: Boolean,
        val language: String
)