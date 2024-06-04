package blacksky.server.entities

import jakarta.persistence.*
import java.util.*

interface IUser {
    val id: UUID
    val login: String
    val name: String
    val passwordHash: String
}

@Table(name = "student")
@Entity
open class Student(
    @Id override val id: UUID,
    @Lob override val login: String,
    @Lob override val name: String,
    @Lob override val passwordHash: String,
    open val acquiringDegree: Degree,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "department_id") open val department: Department,
) : IUser


@Table(name = "mentor")
@Entity
open class Mentor(
    @Id override val id: UUID,
    @Lob override val login: String,
    @Lob override val name: String,
    @Lob override val passwordHash: String,
    @Lob open val bio: String,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "department_id") open val department: Department,
) : IUser


@Table(name = "admin")
@Entity
open class Admin(
    @Id override val id: UUID,
    @Lob override val login: String,
    @Lob override val name: String,
    @Lob override val passwordHash: String,
) : IUser