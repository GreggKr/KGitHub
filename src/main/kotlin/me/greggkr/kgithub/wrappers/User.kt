package me.greggkr.kgithub.wrappers

import com.google.gson.annotations.SerializedName
import java.util.*

data class User(
        @SerializedName("login") val username: String,
        val id: String,
        @SerializedName("avatar_url") val avatarUrl: String,
        @SerializedName("gravatar_id") val gravatarId: String,
        val type: String,
        @SerializedName("site_admin") val admin: Boolean,
        val name: String,
        val company: String,
        val blog: String,
        val location: String,
        val email: String,
        val hireable: Boolean,
        val bio: String,
        @SerializedName("public_repos") val publicRepos: Int,
        @SerializedName("public_gists") val publicGists: Int,
        val followers: Int,
        val following: Int,
        @SerializedName("created_at") val createdAt: Date,
        @SerializedName("updated_at") val updatedAt: Date
)