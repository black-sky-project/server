package blacksky.server.controllers

import blacksky.server.services.CourseService
import blacksky.server.services.PostCourseDto
import blacksky.server.services.SecurityService
import blacksky.server.services.toDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/courses")
class CourseController(private val courseService: CourseService, private val securityService: SecurityService) {
    @GetMapping("/get/list")
    fun getList(@RequestHeader token: String, @RequestParam departmentId: UUID?, @RequestParam universityId: UUID?) =
        securityService.getUserByToken(token).run {
            when {
                departmentId != null -> courseService.getByDepartment(departmentId).map { it.toDto() }
                universityId != null -> courseService.getByUniversity(universityId).map { it.toDto() }
                else -> courseService.getAll().map { it.toDto() }
            }
        }

    @PostMapping("/new")
    fun post(@RequestHeader token: String, @RequestBody dto: PostCourseDto) =
        securityService.checkAdminRights(token).run { courseService.create(dto).toDto() }

    @DeleteMapping("/delete")
    fun delete(@RequestHeader token: String, @RequestParam id: UUID) =
        securityService.checkAdminRights(token).run { courseService.delete(id) }
}