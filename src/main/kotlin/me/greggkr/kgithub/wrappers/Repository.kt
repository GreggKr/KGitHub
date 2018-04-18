package me.greggkr.kgithub.wrappers

import com.google.gson.annotations.SerializedName
import java.util.*

data class Repository(
        val id: Long,
        val name: String,
        @SerializedName("full_name") val fullName: String,
        val owner: User,
        val private: Boolean,
        @SerializedName("html_url") val url: String,
        val description: String,
        val fork: Boolean,
        @SerializedName("created_at") val createdAt: Date,
        @SerializedName("updated_at") val updatedAt: Date,
        @SerializedName("pushed_at") val pushedAt: Date,
        val homepage: String?,
        val size: Int,
        @SerializedName("stargazers_count") val stars: Int,
        @SerializedName("watchers_count") val watchers: Int,
        val language: String,
        @SerializedName("has_issues") val hasIssues: Boolean,
        @SerializedName("has_projects") val hasProjects: Boolean,
        @SerializedName("has_downloads") val hasDownloads: Boolean,
        @SerializedName("has_wiki") val hasWiki: Boolean,
        @SerializedName("has_pages") val hasPages: Boolean,
        @SerializedName("forks_count") val forkCount: Int,
        val archived: Boolean,
        @SerializedName("open_issues_count") val issuesCount: Int,
        @SerializedName("license") val license: String?,
        @SerializedName("watchers") val watcherCount: Int,
        @SerializedName("default_branch") val defaultBranch: String
)