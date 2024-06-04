package blacksky.server.services

fun hashPassword(password: String): String = password.hashCode().toString()