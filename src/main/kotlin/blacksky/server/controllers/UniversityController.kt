package blacksky.server.controllers

import blacksky.server.exceptions.BadRequestException
import blacksky.server.services.UniversityService
import blacksky.server.services.toDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/universities")
class UniversityController(private val universityService: UniversityService) {
    @GetMapping("/get")
    fun getAllUniversities() = universityService.getAllUniversities().map { it.toDto() }

    @PostMapping("/new")
    fun postUniversity(@RequestBody name: String) = universityService.createUniversity(name).toDto()

    @DeleteMapping("/delete")
    fun deleteUniversity(@RequestParam id: UUID) = universityService.deleteUniversity(id)
}