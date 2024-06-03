package blacksky.server.services

import blacksky.server.entities.University
import blacksky.server.exceptions.ConflictException
import blacksky.server.exceptions.InternalErrorException
import blacksky.server.exceptions.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

interface UniversityRepository : JpaRepository<University, UUID>

@Service
class UniversityService @Autowired constructor(private val universityRepository: UniversityRepository) {
    fun getAllUniversities(): List<University> = universityRepository.findAll()

    fun getUniversity(id: UUID) =
        universityRepository.findByIdOrNull(id) ?: throw NotFoundException("No university with such id")

    fun createUniversity(name: String) = with(University(UUID.randomUUID(), name)) {
        if (universityRepository.findAll()
                .any { it.name.lowercase() == name.lowercase() }
        ) throw ConflictException("University with such name already exists")
        universityRepository.saveAndFlush(this)
        universityRepository.findByIdOrNull(id) ?: throw InternalErrorException("Couldn't create university")
    }

    fun deleteUniversity(id: UUID) {
        universityRepository.deleteById(id)
        universityRepository.flush()
    }
}