package blacksky.server.services

import blacksky.server.entities.Course
import blacksky.server.entities.Degree
import blacksky.server.exceptions.ConflictException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import java.util.*

interface CourseRepository : JpaRepository<Course, UUID>

data class PostCourseDto(val degree: Degree, val name: String, val departmentId: UUID)

data class CourseDto(val id: UUID, val degree: Degree, val name: String, val departmentId: UUID)

fun Course.toDto() = CourseDto(id, degree, name, department.id)

@Service
class CourseService @Autowired constructor(
    private val courseRepository: CourseRepository,
    private val departmentService: DepartmentService,
    private val universityService: UniversityService
) {
    fun getAll(): List<Course> = courseRepository.findAll()

    fun getByDepartment(departmentId: UUID) = departmentService.getById(departmentId).courses

    fun getByUniversity(universityId: UUID) =
        universityService.getById(universityId).departments.map { it.courses }.flatten()

    fun create(dto: PostCourseDto) = departmentService.getById(dto.departmentId).let { department ->
        Course(UUID.randomUUID(), dto.degree, dto.name, department).also { course ->
            with(department.courses) {
                if (any { it.name.lowercase() == course.name.lowercase() }) throw ConflictException(
                    "Course with such name already exists in the department"
                )
                add(course)
            }
            courseRepository.flush()
        }
    }

    fun delete(id: UUID) = with(courseRepository) {
        deleteById(id)
        flush()
    }
}