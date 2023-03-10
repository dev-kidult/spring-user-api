package devkidult.git.springuserapi.domain

import devkidult.git.springuserapi.enums.Role
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import java.time.LocalDateTime

@Entity
@Table(
    name = "users",
    indexes = [Index(name = "idx_email", columnList = "email"), Index(name = "idx_phone", columnList = "phone")],
    uniqueConstraints = [
        UniqueConstraint(name = "uq_email", columnNames = ["email"]),
        UniqueConstraint(name = "uq_phone", columnNames = ["phone"])
    ]
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var enabled: Boolean = true,

    @Column(length = 255, nullable = false)
    var email: String,

    @Column(length = 30, nullable = false)
    var nickname: String,

    @Column(length = 255, nullable = false)
    var password: String,

    @Column(length = 20, nullable = false)
    var name: String,

    @Column(length = 11, nullable = false)
    var phone: String,

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER,

    var lastLoggedAt: LocalDateTime = LocalDateTime.now()
)
