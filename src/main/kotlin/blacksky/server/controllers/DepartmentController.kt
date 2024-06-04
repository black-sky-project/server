package blacksky.server.controllers

import blacksky.server.services.DepartmentService
import blacksky.server.services.PostDepartmentDto
import blacksky.server.services.toDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/departments")
class DepartmentController(private val departmentService: DepartmentService) {
    @GetMapping("/get/list")
    fun getList(@RequestParam universityId: UUID?) = when {
        universityId != null -> departmentService.getByUniversity(universityId).map { it.toDto() }
        else -> departmentService.getAll().map { it.toDto() }
    }

    @PostMapping("/new")
    fun post(@RequestBody dto: PostDepartmentDto) = departmentService.create(dto).toDto()

    @DeleteMapping("/delete")
    fun delete(@RequestParam id: UUID) = departmentService.delete(id)
}