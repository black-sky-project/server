package blacksky.server.services

import blacksky.server.entities.Department
import blacksky.server.exceptions.ConflictException
import blacksky.server.exceptions.InternalErrorException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

interface DepartmentRepository : JpaRepository<Department, UUID>

data class PostDepartmentDto(val universityId: UUID, val name: String)

data class DepartmentDto(val id: UUID, val name: String, val universityId: UUID)

fun Department.toDto() = DepartmentDto(id, name, universityId)

@Service
class DepartmentService @Autowired constructor(private val departmentRepository: DepartmentRepository) {
    fun getAllDepartments(): List<Department> = departmentRepository.findAll()

    fun getUniversityDepartments(universityId: UUID) = getAllDepartments().filter { it.universityId == universityId }

    fun createDepartment(dto: PostDepartmentDto) = with(Department(UUID.randomUUID(), dto.name, dto.universityId)) {
        if (departmentRepository.findAll()
                .any { it.name.lowercase() == name.lowercase() }
        ) throw ConflictException("Department with such name already exists")
        departmentRepository.saveAndFlush(this)
        departmentRepository.findByIdOrNull(id) ?: throw InternalErrorException("Couldn't create department")
    }

    fun deleteDepartment(id: UUID) = with(departmentRepository) {
        deleteById(id)
        flush()
    }
}