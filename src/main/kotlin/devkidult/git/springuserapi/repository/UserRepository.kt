package devkidult.git.springuserapi.repository

import devkidult.git.springuserapi.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.OffsetDateTime

@Repository
interface UserRepository : JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    @Modifying
    @Transactional
    @Query("UPDATE User SET lastLoggedAt = :lastLoggedAt where id = :id")
    fun updateLastLoginAt(@Param("id") id: Long, @Param("lastLoggedAt") lastLoggedAt: LocalDateTime): Int
}
