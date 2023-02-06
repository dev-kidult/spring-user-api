package devkidult.git.springuserapi.service

import com.fasterxml.jackson.databind.ObjectMapper
import devkidult.git.springuserapi.domain.QUser.user
import devkidult.git.springuserapi.domain.User
import devkidult.git.springuserapi.dto.UserDetail
import devkidult.git.springuserapi.dto.UserDto
import devkidult.git.springuserapi.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(private val userRepository: UserRepository, private val objectMapper: ObjectMapper) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        getOneByUsername(username).let { UserDetail.from(it) }

    fun getOneByUsername(username: String): User =
        userRepository.findOne(user.email.eq(username).or(user.phone.eq(username)))
            .orElseThrow { IllegalArgumentException("NOT_FOUND_USER") }

    fun getMe(): UserDto.Me {
        val user = SecurityContextHolder.getContext().authentication.principal as UserDetail
        val dbUser = getOneByUsername(user.username)
        return objectMapper.convertValue(dbUser, UserDto.Me::class.java)
    }
}
