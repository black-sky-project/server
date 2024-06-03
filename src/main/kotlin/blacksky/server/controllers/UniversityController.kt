package blacksky.server.controllers

import blacksky.server.services.UniversityService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/universities")
class UniversityController(private val universityService: UniversityService) {
    @GetMapping("/get/all")
    fun getAllUniversities() = universityService.getAllUniversities()

    @GetMapping("/get")
    fun getUniversity(@RequestParam id: UUID) = universityService.getUniversity(id)

    @PostMapping("/new")
    fun postUniversity(@RequestBody name: String) = universityService.createUniversity(name)

    @DeleteMapping("/delete")
    fun deleteUniversity(@RequestParam id: UUID) = universityService.deleteUniversity(id)
}