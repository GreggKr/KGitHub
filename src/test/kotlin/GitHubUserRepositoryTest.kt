import me.greggkr.kgithub.KGitHub

fun main(args: Array<String>) {
    val repos = KGitHub.getRepositories("GreggKr")

    if (repos == null) {
        println("repos == null")
        return
    }

    for (r in repos) {
        print(r.name)
        print(", ")
    }
}
