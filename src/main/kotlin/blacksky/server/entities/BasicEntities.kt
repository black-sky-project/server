package blacksky.server.entities

import jakarta.persistence.*
import java.util.*

@Table(name = "university")
@Entity
open class University(
    @Id open val id: UUID,
    @Lob open val name: String,
    @OneToMany(
        mappedBy = "university",
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        cascade = [CascadeType.ALL]
    ) open val departments: MutableList<Department> = mutableListOf()
)

@Table(name = "department")
@Entity
open class Department(
    @Id open val id: UUID,
    @Lob open val name: String,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "university_id") open val university: University
)

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