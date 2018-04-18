package me.greggkr.kgithub.wrappers

import com.google.gson.annotations.SerializedName

data class Owner(
        @SerializedName("login") val username: String,
        val id: Int,
        @SerializedName("avatar_url") val avatarUrl: String,
        @SerializedName("gavatar_id") val gavatarId: String,
        @SerializedName("type") val accountType: String,
        @SerializedName("site_admin") val admin: Boolean
)