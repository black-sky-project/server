package blacksky.server.controllers

import blacksky.server.services.CourseService
import blacksky.server.services.PostCourseDto
import blacksky.server.services.toDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/courses")
class CourseController(private val courseService: CourseService) {
    @GetMapping("/get/list")
    fun getList(@RequestParam departmentId: UUID?, @RequestParam universityId: UUID?) = when {
        departmentId != null -> courseService.getByDepartment(departmentId).map { it.toDto() }
        universityId != null -> courseService.getByUniversity(universityId).map { it.toDto() }
        else -> courseService.getAll().map { it.toDto() }
    }

    @PostMapping("/new")
    fun post(@RequestBody dto: PostCourseDto) = courseService.create(dto).toDto()

    @DeleteMapping("/delete")
    fun delete(@RequestParam id: UUID) = courseService.delete(id)
}