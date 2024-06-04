package blacksky.server.controllers

import blacksky.server.services.DepartmentService
import blacksky.server.services.PostDepartmentDto
import blacksky.server.services.SecurityService
import blacksky.server.services.toDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/departments")
class DepartmentController(
    private val departmentService: DepartmentService, private val securityService: SecurityService
) {
    @GetMapping("/get/list")
    fun getList(@RequestHeader token: String, @RequestParam universityId: UUID?) =
        securityService.getUserByToken(token).run {
            when {
                universityId != null -> departmentService.getByUniversity(universityId).map { it.toDto() }
                else -> departmentService.getAll().map { it.toDto() }
            }
        }

    @PostMapping("/new")
    fun post(@RequestHeader token: String, @RequestBody dto: PostDepartmentDto) =
        securityService.checkAdminRights(token).run { departmentService.create(dto).toDto() }

    @DeleteMapping("/delete")
    fun delete(@RequestHeader token: String, @RequestParam id: UUID) =
        securityService.checkAdminRights(token).run { departmentService.delete(id) }
}