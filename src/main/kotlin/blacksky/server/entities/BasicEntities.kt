package blacksky.server.entities

import java.util.*

data class University(val id: UUID, val name: String)

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