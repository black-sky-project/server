package blacksky.server.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table
import java.util.*

@Table(name = "university")
@Entity
open class University(
    @Id val id: UUID,
    @Lob val name: String,
)

data class Department(val id: UUID, val name: String, val universityId: UUID)

interface IUser {
    val id: UUID
    val login: String
    val name: String
}

enum class Degree { Bachelor, Masters }

data class Course(val id: UUID, val degree: Degree, val name: String)

data class Student(
    override val id: UUID,
    override val login: String,
    override val name: String,
    val universityId: UUID,
    val departmentId: UUID,
    val acquiringDegree: Degree
) : IUser

data class Mentor(
    override val id: UUID,
    override val login: String,
    override val name: String,
    val universityId: UUID,
    val departmentId: UUID,
    val bio: String
) : IUser

data class Admin(
    override val id: UUID, override val login: String, override val name: String, val approvedById: UUID,
) : IUser