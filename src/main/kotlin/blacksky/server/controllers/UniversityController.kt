package blacksky.server.controllers

import blacksky.server.services.SecurityService
import blacksky.server.services.UniversityService
import blacksky.server.services.toDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/universities")
class UniversityController(
    private val universityService: UniversityService, private val securityService: SecurityService
) {
    @GetMapping("/get/list")
    fun getList(@RequestHeader token: String) =
        securityService.getUserByToken(token).run { universityService.getAll().map { it.toDto() } }

    @PostMapping("/new")
    fun post(@RequestHeader token: String, @RequestBody name: String) =
        securityService.checkAdminRights(token).run { universityService.create(name).toDto() }

    @DeleteMapping("/delete")
    fun delete(@RequestHeader token: String, @RequestParam id: UUID) =
        securityService.checkAdminRights(token).run { universityService.delete(id) }
}