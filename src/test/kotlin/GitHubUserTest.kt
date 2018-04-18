import me.greggkr.kgithub.KGitHub

fun main(args: Array<String>) {
    val user = KGitHub.getUser("oof")

    if (user == null) {
        println("user == null")
        return
    }

    println(user)
}