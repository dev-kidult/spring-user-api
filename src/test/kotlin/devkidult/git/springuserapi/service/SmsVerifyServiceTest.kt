package devkidult.git.springuserapi.service

import devkidult.git.springuserapi.domain.redis.SmsVerify
import devkidult.git.springuserapi.dto.UserDto
import devkidult.git.springuserapi.exceptions.AuthenticationCodeException
import devkidult.git.springuserapi.repository.SmsVerifyRepository
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.given
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class SmsVerifyServiceTest {

    @InjectMocks
    lateinit var smsVerifyService: SmsVerifyService

    @Mock
    lateinit var smsVerifyRepository: SmsVerifyRepository

    @Test
    @DisplayName("새 전화번호 인증번호 생성 테스트")
    fun createTestByNewPhone() {
        given { smsVerifyRepository.findById(anyString()) }.willReturn(Optional.empty())

        smsVerifyService.create("01012345678")
        verify(smsVerifyRepository, times(1)).findById(anyString())
        verify(smsVerifyRepository, times(1)).save(any())
    }

    @Test
    @DisplayName("기존 전화번호 인증번호 생성 테스트")
    fun createTestByOlderPhone() {
        val smsVerify = SmsVerify(phone = "01012345678")
        given { smsVerifyRepository.findById(anyString()) }.willReturn(Optional.of(smsVerify))

        smsVerifyService.create("01012345678")
        verify(smsVerifyRepository, times(1)).findById(anyString())
        verify(smsVerifyRepository, times(1)).save(any())
    }

    @Test
    @DisplayName("기존 전화번호 인증번호 생성 테스트 - 실패")
    fun createTestByOlderPhoneFail() {
        val smsVerify = SmsVerify(phone = "01012345678", count = 3)
        given { smsVerifyRepository.findById(anyString()) }.willReturn(Optional.of(smsVerify))

        val exception = assertThrows<AuthenticationCodeException> { smsVerifyService.create("01012345678") }
        assertThat(exception.message, containsString("짧은 시간 너무 많은 요청을 하였습니다. 30분후에 다시 시도하세요."))
    }

    @Test
    @DisplayName("인증코드 인증성공")
    fun verifyTest() {
        val smsVerify = SmsVerify(phone = "01012345678", count = 1)
        given { smsVerifyRepository.findById(anyString()) }.willReturn(Optional.of(smsVerify))

        val request = UserDto.VerifyByPhone(phone = "01012345678", code = smsVerify.code)
        smsVerifyService.verify(request)
        verify(smsVerifyRepository, times(1)).findById(anyString())
        verify(smsVerifyRepository, times(1)).save(any())
    }

    @Test
    @DisplayName("인증코드 인증실패 - 잘못된 번호")
    fun verifyTestByWrongPhone() {
        given { smsVerifyRepository.findById(anyString()) }.willReturn(Optional.empty())

        val request = UserDto.VerifyByPhone(phone = "01012345678", code = "aaaaaa")
        val exception = assertThrows<AuthenticationCodeException> { smsVerifyService.verify(request) }
        assertThat(exception.message, containsString("유효한 핸드폰 인증내역이 없습니다."))
    }

    @Test
    @DisplayName("인증코드 인증실패 - 잘못된 코드")
    fun verifyTestByWrongCode() {
        val smsVerify = SmsVerify(phone = "01012345678", count = 1)
        given { smsVerifyRepository.findById(anyString()) }.willReturn(Optional.of(smsVerify))

        val request = UserDto.VerifyByPhone(phone = "01012345678", code = "aaaaaa")
        val exception = assertThrows<AuthenticationCodeException> { smsVerifyService.verify(request) }
        assertThat(exception.message, containsString("인증코드가 일치하지 않습니다."))
    }
}
