package blacksky.server.services

import blacksky.server.entities.Department
import blacksky.server.exceptions.ConflictException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
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
    fun getAllDepartments(): List<Department> = departmentRepository.findAll()

    fun getUniversityDepartments(universityId: UUID) = universityService.getUniversityById(universityId).departments

    fun createDepartment(dto: PostDepartmentDto) =
        universityService.getUniversityById(dto.universityId).let { university ->
            Department(UUID.randomUUID(), dto.name, university).also { department ->
                with(university.departments) {
                    if (any { it.id == department.id }) throw ConflictException(
                        "Department with such name already exists in the university"
                    )
                    add(department)
                }
                departmentRepository.flush()
            }
        }

    fun deleteDepartment(id: UUID) = with(departmentRepository) {
        deleteById(id)
        flush()
    }
}