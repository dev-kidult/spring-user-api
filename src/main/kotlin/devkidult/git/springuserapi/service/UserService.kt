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

    fun changePassword(): UserDto.PasswordResponse {
        val newPassword = RandomString.make(8)
        val user = getUserByPrincipal()
        user.password = passwordEncoder.encode(newPassword)
        // NOTE 실제론 새로운 비밀번호를 response로 않고 메일이라던지 SMS라던지 다른체널로 새 비밀번호 전달
        return UserDto.PasswordResponse(password = newPassword)
    }
}
