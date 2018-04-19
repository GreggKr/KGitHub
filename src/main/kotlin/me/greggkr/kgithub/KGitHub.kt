package me.greggkr.kgithub

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import me.greggkr.kgithub.auth.GitHubAuthenticator
import me.greggkr.kgithub.response.Response
import me.greggkr.kgithub.response.ResponseType
import me.greggkr.kgithub.wrappers.Repository
import me.greggkr.kgithub.wrappers.User
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
        fun getRepositories(user: String): Response<Array<Repository>?> {
            val req = Request.Builder()
                    .url("$BASE_USER_URL/$user/repos")
                    .get()
                    .build()

            val res = client.newCall(req).execute()

            val body = res.body() ?: return Response(ResponseType.NOT_FOUND, null)

            val str = body.string()

            val valRes = validate(str)

            if (valRes != ResponseType.OK) return Response(valRes, null)

            val repos = gson.fromJson(str, Array<Repository>::class.java)

            return Response(ResponseType.OK, repos)
        }

        fun getRepository(user: String, repo: String): Response<Repository?> {
            val repos = getRepositories(user)

            val r = Arrays.stream(repos.data)
                    .filter { it.name.equals(repo, true) }
                    .limit(1)
                    .collect(Collectors.toList())[0] ?: return Response(ResponseType.NOT_FOUND, null)

            return Response(ResponseType.OK, r)
        }

        fun getUser(user: String): Response<User?> {
            val req = Request.Builder()
                    .url("$BASE_USER_URL/$user")
                    .get()
                    .build()

            val res = client.newCall(req).execute()

            val body = res.body() ?: return Response(ResponseType.OK, null)

            val str = body.string()

            val valRes = validate(str)

            if (valRes != ResponseType.OK) return Response(valRes, null)

            val u = gson.fromJson(str, User::class.java)

            return Response(ResponseType.OK, u)
        }

        /**
         * Checks the GitHub response to make sure it's valid
         *
         * @param json the json to check
         * @return true if valid response, false otherwise
         */
        private fun validate(json: String): ResponseType {
            val elem = gson.fromJson(json, JsonElement::class.java)

            if (elem.isJsonArray) return ResponseType.OK // If it's an array, it's always valid

            val obj = elem.asJsonObject

            if (obj.has("message")) {
                if (obj.get("message").asString != "Not found") return ResponseType.NOT_FOUND
            }

            return ResponseType.OK
        }
    }
}
