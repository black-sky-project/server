package blacksky.server.services

import blacksky.server.entities.*
import blacksky.server.exceptions.ConflictException
import blacksky.server.exceptions.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

interface StudentRepository : JpaRepository<Student, UUID>
interface MentorRepository : JpaRepository<Mentor, UUID>
interface AdminRepository : JpaRepository<Admin, UUID>

//region DTOs
data class PostStudentDto(
    val login: String, val password: String, val name: String, val acquiringDegree: Degree, val departmentId: UUID
)

data class PostMentorDto(
    val login: String, val password: String, val name: String, val bio: String, val departmentId: UUID
)

data class PostAdminDto(val login: String, val password: String, val name: String, val bio: String)

data class UserDto(val id: UUID, val login: String, val name: String)

data class StudentDto(
    val id: UUID, val login: String, val name: String, val acquiringDegree: Degree, val departmentId: UUID
)

data class MentorDto(val id: UUID, val login: String, val name: String, val bio: String, val departmentId: UUID)

data class AdminDto(val id: UUID, val login: String, val name: String)

fun IUser.toDto() = UserDto(id, login, name)
fun Student.toDto() = StudentDto(id, login, name, acquiringDegree, department.id)
fun Mentor.toDto() = MentorDto(id, login, name, bio, department.id)
fun Admin.toDto() = AdminDto(id, login, name)
//endregion

@Service
class UserService @Autowired constructor(
    private val studentRepository: StudentRepository,
    private val mentorRepository: MentorRepository,
    private val adminRepository: AdminRepository,
    private val departmentService: DepartmentService
) {
    fun getAllStudents(): List<Student> = studentRepository.findAll()
    fun getAllMentors(): List<Mentor> = mentorRepository.findAll()
    fun getAllAdmins(): List<Admin> = adminRepository.findAll()
    fun getAllUsers(): List<IUser> = getAllStudents() + getAllMentors() + getAllAdmins()

    fun getMentorById(id: UUID) =
        mentorRepository.findByIdOrNull(id) ?: throw NotFoundException("No mentor with such id")

    fun getStudentsByDepartment(departmentId: UUID) = departmentService.getById(departmentId).students
    fun getMentorsByDepartment(departmentId: UUID) = departmentService.getById(departmentId).mentors

    fun getStudentsByUniversity(universityId: UUID) =
        departmentService.getByUniversity(universityId).map { it.students }.flatten()

    fun getMentorsByUniversity(universityId: UUID) =
        departmentService.getByUniversity(universityId).map { it.mentors }.flatten()

    fun getUserById(userId: UUID) = when {
        studentRepository.existsById(userId) -> studentRepository.findById(userId).get()
        mentorRepository.existsById(userId) -> mentorRepository.findById(userId).get()
        adminRepository.existsById(userId) -> adminRepository.findById(userId).get()
        else -> throw NotFoundException("No user with such id")
    }

    fun isLoginAvailable(login: String) = getAllUsers().none { it.login.lowercase() == login.lowercase() }

    fun createStudent(dto: PostStudentDto) = departmentService.getById(dto.departmentId).let { department ->
        Student(
            UUID.randomUUID(), dto.login, dto.name, hashPassword(dto.password), dto.acquiringDegree, department
        ).also { student ->
            if (isLoginAvailable(student.login).not()) throw ConflictException("User with such login already exists")
            department.students.add(student)
            studentRepository.flush()
        }
    }

    fun createMentor(dto: PostMentorDto) = departmentService.getById(dto.departmentId).let { department ->
        Mentor(
            UUID.randomUUID(), dto.login, dto.name, hashPassword(dto.password), dto.bio, department
        ).also { mentor ->
            if (isLoginAvailable(mentor.login).not()) throw ConflictException("User with such login already exists")
            department.mentors.add(mentor)
            studentRepository.flush()
        }
    }

    fun createAdmin(dto: PostAdminDto) = Admin(
        UUID.randomUUID(), dto.login, dto.name, hashPassword(dto.password)
    ).also { admin ->
        if (isLoginAvailable(admin.login).not()) throw ConflictException("User with such login already exists")
        adminRepository.save(admin)
        studentRepository.flush()
    }

    fun deleteUser(id: UUID) = when {
        studentRepository.existsById(id) -> studentRepository.deleteById(id)
        mentorRepository.existsById(id) -> mentorRepository.deleteById(id)
        adminRepository.existsById(id) -> adminRepository.deleteById(id)
        else -> throw NotFoundException("No user with such id")
    }
}