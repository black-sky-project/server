package blacksky.server.controllers

import blacksky.server.services.UniversityService
import blacksky.server.services.toDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/universities")
class UniversityController(private val universityService: UniversityService) {
    @GetMapping("/get/list")
    fun getList() = universityService.getAll().map { it.toDto() }

    @PostMapping("/new")
    fun post(@RequestBody name: String) = universityService.create(name).toDto()

    @DeleteMapping("/delete")
    fun delete(@RequestParam id: UUID) = universityService.delete(id)
}