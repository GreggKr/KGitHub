package me.greggkr.kgithub

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.greggkr.kgithub.auth.GitHubAuthenticator
import me.greggkr.kgithub.wrappers.Repository
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import java.util.stream.Collectors

private const val BASE_URL = "https://api.github.com"
private const val BASE_USER_URL = "$BASE_URL/users"

class KGitHub {
    companion object {
        private val client = OkHttpClient.Builder()
                .authenticator(GitHubAuthenticator())
                .build()

        private val gson = GsonBuilder()
                .setPrettyPrinting()
                .create()

        /**
         * @param user the user to check repositories for
         * @return null if user does not exist, an Array of Repositories by the user
         */
        fun getRepositories(user: String): Array<Repository>? {
            val req = Request.Builder()
                    .url("$BASE_USER_URL/$user/repos")
                    .get()
                    .build()

            val res = client.newCall(req).execute()

            val body = res.body() ?: return null

            val str = body.string()

            if (!validate(str)) return null

            return gson.fromJson(str, Array<Repository>::class.java)
        }

        fun getRepository(user: String, repo: String): Repository? {
            val repos = getRepositories(user)

            return Arrays.stream(repos)
                    .filter { it.name.equals(repo, true) }
                    .limit(1)
                    .collect(Collectors.toList())[0]
        }

        /**
         * Checks the GitHub response to make sure it's valid
         *
         * @param json the json to check
         * @return true if valid response, false otherwise
         */
        private fun validate(json: String): Boolean {
            val elem = gson.fromJson(json, JsonElement::class.java)

            if (elem.isJsonArray) return true // If it's an array, it's always valid

            val obj = elem.asJsonObject
            return obj.has("message") && obj.get("message").asString == "Not found"
        }
    }
}
