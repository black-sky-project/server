package blacksky.server.controllers

import blacksky.server.services.*
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService, private val securityService: SecurityService) {
    @GetMapping("/get/list/students")
    fun getStudents(
        @RequestHeader token: String, @RequestParam departmentId: UUID?, @RequestParam universityId: UUID?
    ) = securityService.getUserByToken(token).run {
        when {
            departmentId != null -> userService.getStudentsByDepartment(departmentId).map { it.toDto() }
            universityId != null -> userService.getStudentsByUniversity(universityId).map { it.toDto() }
            else -> userService.getAllStudents().map { it.toDto() }
        }
    }

    @GetMapping("/get/list/mentors")
    fun getAllMentors(
        @RequestHeader token: String, @RequestParam departmentId: UUID?, @RequestParam universityId: UUID?
    ) = securityService.getUserByToken(token).run {
        when {
            departmentId != null -> userService.getMentorsByDepartment(departmentId).map { it.toDto() }
            universityId != null -> userService.getMentorsByUniversity(universityId).map { it.toDto() }
            else -> userService.getAllMentors().map { it.toDto() }
        }
    }

    @GetMapping("/get/list/admins")
    fun getAllAdmins(@RequestHeader token: String) =
        securityService.checkAdminRights(token).run { userService.getAllAdmins().map { it.toDto() } }

    @GetMapping("/get/list/all")
    fun getAll(@RequestHeader token: String) =
        securityService.checkAdminRights(token).run { userService.getAllUsers().map { it.toDto() } }

    @GetMapping("/isLoginAvailable")
    fun isLoginAvailable(@RequestHeader token: String, @RequestBody login: String) = userService.isLoginAvailable(login)

    @PostMapping("/new/student")
    fun postStudent(@RequestHeader token: String, @RequestBody dto: PostStudentDto) =
        securityService.checkAdminRights(token).run { userService.createStudent(dto).toDto() }

    @PostMapping("/new/mentor")
    fun postMentor(@RequestHeader token: String, @RequestBody dto: PostMentorDto) =
        securityService.checkAdminRights(token).run { userService.createMentor(dto).toDto() }

    @PostMapping("/new/admin")
    fun postAdmin(@RequestHeader token: String, @RequestBody dto: PostAdminDto) =
        securityService.checkAdminRights(token).run { userService.createAdmin(dto).toDto() }

    @DeleteMapping("/delete")
    fun deleteUser(@RequestHeader token: String, @RequestParam id: UUID) =
        securityService.checkAdminRights(token).run { userService.deleteUser(id) }
}