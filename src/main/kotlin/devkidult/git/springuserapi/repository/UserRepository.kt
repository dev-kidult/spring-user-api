package devkidult.git.springuserapi.repository

import devkidult.git.springuserapi.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>, QuerydslPredicateExecutor<User>
