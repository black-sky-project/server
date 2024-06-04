package blacksky.server.controllers

import blacksky.server.entities.Admin
import blacksky.server.entities.Mentor
import blacksky.server.exceptions.ForbiddenException
import blacksky.server.services.OfferService
import blacksky.server.services.PostOfferDto
import blacksky.server.services.SecurityService
import blacksky.server.services.toDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/offers")
class OfferController(private val offerService: OfferService, private val securityService: SecurityService) {
    @GetMapping("/get/list")
    fun getList(
        @RequestHeader token: String,
        @RequestParam courseId: UUID?,
        @RequestParam mentorId: UUID?,
        @RequestParam departmentId: UUID?,
        @RequestParam universityId: UUID?
    ) = securityService.getUserByToken(token).run {
        when {
            courseId != null -> offerService.getByCourse(courseId).map { it.toDto() }
            mentorId != null -> offerService.getByMentor(mentorId).map { it.toDto() }
            departmentId != null -> offerService.getByDepartment(departmentId).map { it.toDto() }
            universityId != null -> offerService.getByUniversity(universityId).map { it.toDto() }
            else -> offerService.getAll().map { it.toDto() }
        }
    }

    @PostMapping("/new")
    fun post(@RequestHeader token: String, @RequestBody dto: PostOfferDto) = securityService.getUserByToken(token)
        .takeIf { it is Admin || (it is Mentor && it.id == dto.mentorId) }
        ?.run { offerService.create(dto).toDto() } ?: ForbiddenException("You dont have rights to do it")

    @DeleteMapping("/delete")
    fun delete(@RequestHeader token: String, @RequestParam id: UUID) = securityService.getUserByToken(token)
        .takeIf { it is Admin || (it is Mentor && it.id == offerService.getById(id).mentor.id) }
        ?.run { offerService.delete(id) }
}