package blacksky.server.services

import blacksky.server.entities.University
import blacksky.server.exceptions.ConflictException
import blacksky.server.exceptions.InternalErrorException
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

    fun createUniversity(name: String) = University(UUID.randomUUID(), name).let {
        with(universityRepository) {
            if (findAll().any { it.name.lowercase() == name.lowercase() }) throw ConflictException("University with such name already exists")
            saveAndFlush(it)
            findByIdOrNull(it.id) ?: throw InternalErrorException("Couldn't create university")
        }
    }

    fun deleteUniversity(id: UUID) = with(universityRepository) {
        deleteById(id)
        flush()
    }
}