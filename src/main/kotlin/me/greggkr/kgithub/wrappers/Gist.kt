package me.greggkr.kgithub.wrappers

import com.google.gson.annotations.SerializedName

data class Gist(
        @SerializedName("html_url") val url: String
)