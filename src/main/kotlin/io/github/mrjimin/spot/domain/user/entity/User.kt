package io.github.mrjimin.spot.domain.user.entity

import io.github.mrjimin.spot.global.common.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(unique = true, nullable = false)
    var email: String,

    @Column
    var password: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var provider: LoginProvider = LoginProvider.LOCAL,

    @Column
    var providerId: String? = null,

    @Column(nullable = false)
    var nickname: String,

    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER
) : BaseEntity()