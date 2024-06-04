package blacksky.server.entities

import jakarta.persistence.*
import java.util.*

@Table(name = "university")
@Entity
open class University(
    @Id open val id: UUID, @Lob open val name: String, @OneToMany(
        mappedBy = "university", fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL]
    ) open val departments: MutableList<Department> = mutableListOf()
)

@Table(name = "department")
@Entity
open class Department(
    @Id open val id: UUID,
    @Lob open val name: String,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "university_id") open val university: University,
    @OneToMany(
        mappedBy = "department", fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL]
    ) open val courses: MutableList<Course> = mutableListOf(),
    @OneToMany(
        mappedBy = "department", fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL]
    ) open val students: MutableList<Student> = mutableListOf(),
    @OneToMany(
        mappedBy = "department", fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL]
    ) open val mentors: MutableList<Mentor> = mutableListOf()
)

enum class Degree { Bachelor, Master }

@Table(name = "course")
@Entity
open class Course(
    @Id open val id: UUID,
    open val degree: Degree,
    @Lob open val name: String,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "department_id") open val department: Department,
    @OneToMany(
        mappedBy = "course", fetch = FetchType.LAZY, orphanRemoval = true, cascade = [CascadeType.ALL]
    ) open val offers: MutableList<Offer> = mutableListOf()
)

@Table(name = "offer")
@Entity
open class Offer(
    @Id open val id: UUID,
    @Lob open val title: String,
    @Lob open val description: String,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "course_id") open val course: Course,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "mentor_id") open val mentor: Mentor,
)