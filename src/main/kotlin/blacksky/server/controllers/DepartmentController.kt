package blacksky.server.controllers

import blacksky.server.services.DepartmentService
import blacksky.server.services.PostDepartmentDto
import blacksky.server.services.toDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/departments")
class DepartmentController(private val departmentService: DepartmentService) {
    @GetMapping("/get")
    fun getAllDepartments(@RequestParam universityId: UUID?) = when {
        universityId != null -> departmentService.getUniversityDepartments(universityId)
        else -> departmentService.getAllDepartments().map { it.toDto() }
    }

    @PostMapping("/new")
    fun postDepartment(@RequestBody name: PostDepartmentDto) = departmentService.createDepartment(name).toDto()

    @DeleteMapping("/delete")
    fun deleteDepartment(@RequestParam id: UUID) = departmentService.deleteDepartment(id)
}