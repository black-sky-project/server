package blacksky.server.services

import blacksky.server.entities.Department
import blacksky.server.exceptions.ConflictException
import blacksky.server.exceptions.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

interface DepartmentRepository : JpaRepository<Department, UUID>

data class PostDepartmentDto(val universityId: UUID, val name: String)

data class DepartmentDto(val id: UUID, val name: String, val universityId: UUID)

fun Department.toDto() = DepartmentDto(id, name, university.id)

@Service
class DepartmentService @Autowired constructor(
    private val departmentRepository: DepartmentRepository, private val universityService: UniversityService
) {
    fun getAll(): List<Department> = departmentRepository.findAll()

    fun getById(id: UUID) =
        departmentRepository.findByIdOrNull(id) ?: throw NotFoundException("No department with such id")

    fun getByUniversity(universityId: UUID) = universityService.getById(universityId).departments

    fun create(dto: PostDepartmentDto) = universityService.getById(dto.universityId).let { university ->
        Department(UUID.randomUUID(), dto.name, university).also { department ->
            with(university.departments) {
                if (any { it.name.lowercase() == department.name.lowercase() }) throw ConflictException(
                    "Department with such name already exists in the university"
                )
                add(department)
            }
            departmentRepository.flush()
        }
    }

    fun delete(id: UUID) = with(departmentRepository) {
        deleteById(id)
        flush()
    }
}