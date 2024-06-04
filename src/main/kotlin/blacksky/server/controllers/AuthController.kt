package blacksky.server.controllers

import blacksky.server.services.LoginDto
import blacksky.server.services.SecurityService
import blacksky.server.services.toDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val securityService: SecurityService) {
    @PostMapping("/login")
    fun login(@RequestBody dto: LoginDto) = securityService.login(dto)

    @PostMapping("/getMe")
    fun getMe(@RequestHeader token: String) = securityService.getUserByToken(token).toDto()
}