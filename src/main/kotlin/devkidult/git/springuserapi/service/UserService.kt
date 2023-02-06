package devkidult.git.springuserapi.service

import com.fasterxml.jackson.databind.ObjectMapper
import devkidult.git.springuserapi.domain.QUser.user
import devkidult.git.springuserapi.domain.User
import devkidult.git.springuserapi.dto.UserDetail
import devkidult.git.springuserapi.dto.UserDto
import devkidult.git.springuserapi.repository.UserRepository
import net.bytebuddy.utility.RandomString
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val objectMapper: ObjectMapper,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        getOneByUsername(username).let { UserDetail.from(it) }

    fun getOneByUsername(username: String): User =
        userRepository.findOne(user.email.eq(username).or(user.phone.eq(username)))
            .orElseThrow { IllegalArgumentException("NOT_FOUND_USER") }

    fun getUserByPrincipal(): User {
        val user = SecurityContextHolder.getContext().authentication.principal as UserDetail
        return getOneByUsername(user.username)
    }

    fun getMe(): UserDto.Me = getUserByPrincipal().let { objectMapper.convertValue(user, UserDto.Me::class.java) }

    fun changePassword(request: UserDto.PasswordRequest) {
        if (request.newPassword != request.repeatPassword) throw IllegalArgumentException("WRONG PASSWORD")
        val user = getUserByPrincipal()
        user.password = passwordEncoder.encode(request.newPassword)
    }
}
