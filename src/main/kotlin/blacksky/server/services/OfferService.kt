package blacksky.server.services

import blacksky.server.entities.Offer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import java.util.*

interface OfferRepository : JpaRepository<Offer, UUID>

data class PostOfferDto(val mentorId: UUID, val courseId: UUID, val title: String, val description: String)

data class OfferDto(val id: UUID, val mentorId: UUID, val courseId: UUID, val title: String, val description: String)

fun Offer.toDto() = OfferDto(id, mentor.id, course.id, title, description)

@Service
class OfferService @Autowired constructor(
    private val offerRepository: OfferRepository,
    private val courseService: CourseService,
    private val userService: UserService,
) {
    fun getAll(): List<Offer> = offerRepository.findAll()

    fun getByCourse(courseId: UUID) = courseService.getById(courseId).offers

    fun getByMentor(mentorId: UUID) = userService.getMentorById(mentorId).offers

    fun getByDepartment(departmentId: UUID) = courseService.getByDepartment(departmentId).map { it.offers }.flatten()

    fun getByUniversity(universityId: UUID) = courseService.getByUniversity(universityId).map { it.offers }.flatten()

    fun create(dto: PostOfferDto) = courseService.getById(dto.courseId).let { course ->
        userService.getMentorById(dto.mentorId).let { mentor ->
            Offer(UUID.randomUUID(), dto.title, dto.description, course, mentor).also {
                offerRepository.save(it)
                offerRepository.flush()
            }
        }
    }

    fun delete(id: UUID) = with(offerRepository) {
        deleteById(id)
        flush()
    }
}