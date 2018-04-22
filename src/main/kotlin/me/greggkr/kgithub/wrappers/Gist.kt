package me.greggkr.kgithub.wrappers

import com.google.gson.annotations.SerializedName
import java.util.*

data class Gist(
        val id: String,
        val description: String?,
        val public: Boolean,
        val owner: Owner,
        val user: Owner?,
        val files: Map<String, File>,
        val truncated: Boolean,
        val comments: Int,
        @SerializedName("html_url") val url: String,
        @SerializedName("git_pull_url") val pullUrl: String,
        @SerializedName("git_push_url") val pushUrl: String,
        @SerializedName("created_at") val createdAt: Date,
        @SerializedName("updated_at") val updatedAt: Date
)