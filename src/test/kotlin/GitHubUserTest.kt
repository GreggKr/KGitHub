import me.greggkr.kgithub.KGitHub

fun main(args: Array<String>) {
    val user = KGitHub.getUser("glkfdmgodmfgipdmfgimdfipgdmfgimdfgimdfigmf")

    if (user.data == null) {
        println("invalid return. reason: ${user.type.name}")
        return
    }

    println(user.data)
}