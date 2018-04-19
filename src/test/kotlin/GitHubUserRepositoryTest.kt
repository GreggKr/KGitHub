import me.greggkr.kgithub.KGitHub

fun main(args: Array<String>) {
    val repos = KGitHub.getRepositories("GreggKr")

    if (repos.data == null) {
        println("invalid return. reason: ${repos.type.name}")
        return
    }

    val rs = repos.data!!

    for (r in rs) {
        println(r)
    }
}
