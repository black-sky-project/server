package blacksky.server.services

import blacksky.server.entities.University
import blacksky.server.exceptions.ConflictException
import blacksky.server.exceptions.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

interface UniversityRepository : JpaRepository<University, UUID>

data class UniversityDto(val id: UUID, val name: String)

fun University.toDto() = UniversityDto(id, name)

@Service
class UniversityService @Autowired constructor(private val universityRepository: UniversityRepository) {
    fun getAllUniversities(): List<University> = universityRepository.findAll()

    fun getUniversityById(id: UUID) =
        universityRepository.findByIdOrNull(id) ?: throw NotFoundException("University with such id not found")

    fun createUniversity(name: String) = University(UUID.randomUUID(), name).also {
        with(universityRepository) {
            if (findAll().any { it.name.lowercase() == name.lowercase() }) throw ConflictException(
                "University with such name already exists"
            )
            saveAndFlush(it)
        }
    }

    fun deleteUniversity(id: UUID) = with(universityRepository) {
        deleteById(id)
        flush()
    }
}