package blacksky.server.services

import blacksky.server.entities.IUser
import blacksky.server.exceptions.ForbiddenException
import blacksky.server.exceptions.UnauthorizedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

fun hashPassword(password: String): String = password.hashCode().toString()

data class LoginDto(val login: String, val password: String)

@Service
class SecurityService @Autowired constructor(val userService: UserService) {
    val userIdByToken = mutableMapOf<String, UUID>()

    fun getUserByToken(token: String) =
        userIdByToken[token]?.let { userService.getUserById(it) } ?: throw UnauthorizedException("Unknown token")

    fun login(dto: LoginDto) = userService.getAllUsers().firstOrNull { it.login == dto.login }
        ?.takeIf { it.passwordHash == hashPassword(dto.password) }?.let { makeToken(it) }
        ?: throw ForbiddenException("Bad credentials")

    private fun makeToken(user: IUser) = UUID.randomUUID().toString().also { userIdByToken[it] = user.id }
}