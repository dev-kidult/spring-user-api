package devkidult.git.springuserapi.service

import com.fasterxml.jackson.databind.ObjectMapper
import devkidult.git.springuserapi.domain.User
import devkidult.git.springuserapi.domain.redis.SmsVerify
import devkidult.git.springuserapi.dto.UserDto
import devkidult.git.springuserapi.exceptions.AuthenticationCodeException
import devkidult.git.springuserapi.repository.SmsVerifyRepository
import devkidult.git.springuserapi.repository.UserRepository
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.given
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class AuthServiceTest {

    @InjectMocks
    lateinit var authService: AuthService

    @Mock
    lateinit var smsVerifyService: SmsVerifyService

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var objectMapper: ObjectMapper

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    val request = UserDto.Request(email = "doole3488@gmail.com", nickname = "devkidult", name = "정용희", phone = "01012345678", password = "1234")
    val user = User(email = "doole3488@gmail.com", nickname = "devkidult", name = "정용희", phone = "01012345678", password = "1234")

    @Test
    @DisplayName("회원가입")
    fun signUpTest() {
        given { objectMapper.convertValue(request, User::class.java) }.willReturn(user)
        given { passwordEncoder.encode(request.password) }.willReturn(user.password)
        given { userRepository.exists(any()) }.willReturn(false)

        authService.signUp(request)
        verify(userRepository, times(1)).save(any())
    }

    @Test
    @DisplayName("회원가입실패 - 이미 가입된 사용자")
    fun signUpTestExistsFail() {
        given { userRepository.exists(any()) }.willReturn(true)

        val exception = assertThrows<AuthenticationCodeException> { authService.signUp(request) }
        assertThat(exception.message, containsString("이미 회원가입한 사용자입니다."))
    }
}
