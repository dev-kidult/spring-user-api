package devkidult.git.springuserapi.service

import com.fasterxml.jackson.databind.ObjectMapper
import devkidult.git.springuserapi.domain.QUser.user
import devkidult.git.springuserapi.domain.User
import devkidult.git.springuserapi.dto.UserDto
import devkidult.git.springuserapi.exceptions.AuthenticationCodeException
import devkidult.git.springuserapi.exceptions.message.AuthErrorMessage.ALREADY_SIGN_UP_USER
import devkidult.git.springuserapi.repository.UserRepository
import net.bytebuddy.utility.RandomString
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException

@Service
@Transactional
class AuthService(
    private val smsVerifyService: SmsVerifyService,
    private val userRepository: UserRepository,
    private val objectMapper: ObjectMapper,
    private val passwordEncoder: PasswordEncoder,
) {

    fun signUp(request: UserDto.Request) {
        if (exists(request)) throw AuthenticationCodeException(ALREADY_SIGN_UP_USER)

        smsVerifyService.verifiedAndDelete(request.phone)

        val user = objectMapper.convertValue(request, User::class.java).apply { password = passwordEncoder.encode(password) }
        userRepository.save(user)
    }

    fun exists(request: UserDto.Request) =
        userRepository.exists(
            user.email.eq(request.email)
                .or(user.phone.eq(request.phone))
        )

    fun newPassword(request: UserDto.PhoneRequest): UserDto.PasswordResponse {
        smsVerifyService.verifiedAndDelete(request.phone)
        val newPassword = RandomString.make(8)
        val user = userRepository.findOne(user.phone.eq(request.phone)).orElseThrow { IllegalArgumentException("NOT_FOUND_USER") }
        user.password = passwordEncoder.encode(newPassword)
        // NOTE 실제론 새로운 비밀번호를 response로 않고 메일이라던지 SMS라던지 다른체널로 새 비밀번호 전달
        return UserDto.PasswordResponse(password = newPassword)
    }
}
