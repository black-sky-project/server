package blacksky.server.controllers

import blacksky.server.services.OfferService
import blacksky.server.services.PostOfferDto
import blacksky.server.services.toDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/offers")
class OfferController(private val offerService: OfferService) {
    @GetMapping("/get/list")
    fun getList(
        @RequestParam courseId: UUID?,
        @RequestParam mentorId: UUID?,
        @RequestParam departmentId: UUID?,
        @RequestParam universityId: UUID?
    ) = when {
        courseId != null -> offerService.getByCourse(courseId).map { it.toDto() }
        mentorId != null -> offerService.getByMentor(mentorId).map { it.toDto() }
        departmentId != null -> offerService.getByDepartment(departmentId).map { it.toDto() }
        universityId != null -> offerService.getByUniversity(universityId).map { it.toDto() }
        else -> offerService.getAll().map { it.toDto() }
    }

    @PostMapping("/new")
    fun post(@RequestBody dto: PostOfferDto) = offerService.create(dto).toDto()

    @DeleteMapping("/delete")
    fun delete(@RequestParam id: UUID) = offerService.delete(id)
}