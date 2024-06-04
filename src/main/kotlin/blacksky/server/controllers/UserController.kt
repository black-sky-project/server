package blacksky.server.controllers

import blacksky.server.services.*
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService) {
    @GetMapping("/get/list/students")
    fun getStudents(@RequestParam departmentId: UUID?, @RequestParam universityId: UUID?) = when {
        departmentId != null -> userService.getStudentsByDepartment(departmentId).map { it.toDto() }
        universityId != null -> userService.getStudentsByUniversity(universityId).map { it.toDto() }
        else -> userService.getAllStudents().map { it.toDto() }
    }

    @GetMapping("/get/list/mentors")
    fun getAllMentors(@RequestParam departmentId: UUID?, @RequestParam universityId: UUID?) = when {
        departmentId != null -> userService.getMentorsByDepartment(departmentId).map { it.toDto() }
        universityId != null -> userService.getMentorsByUniversity(universityId).map { it.toDto() }
        else -> userService.getAllMentors().map { it.toDto() }
    }

    @GetMapping("/get/list/admins")
    fun getAllAdmins() = userService.getAllAdmins().map { it.toDto() }

    @GetMapping("/get/list/all")
    fun getAll() = userService.getAllUsers().map { it.toDto() }

    @GetMapping("/isLoginAvailable")
    fun isLoginAvailable(@RequestBody login: String) = userService.isLoginAvailable(login)

    @PostMapping("/new/student")
    fun postStudent(@RequestBody dto: PostStudentDto) = userService.createStudent(dto).toDto()

    @PostMapping("/new/mentor")
    fun postMentor(@RequestBody dto: PostMentorDto) = userService.createMentor(dto).toDto()

    @PostMapping("/new/admin")
    fun postAdmin(@RequestBody dto: PostAdminDto) = userService.createAdmin(dto).toDto()

    @DeleteMapping("/delete")
    fun deleteUser(@RequestParam id: UUID) = userService.deleteUser(id)
}