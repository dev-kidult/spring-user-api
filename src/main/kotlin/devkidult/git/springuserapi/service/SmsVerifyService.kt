package devkidult.git.springuserapi.service

import devkidult.git.springuserapi.domain.redis.SmsVerify
import devkidult.git.springuserapi.dto.UserDto
import devkidult.git.springuserapi.exceptions.AuthenticationCodeException
import devkidult.git.springuserapi.exceptions.message.AuthErrorMessage.NOT_FOUND_VERIFY
import devkidult.git.springuserapi.exceptions.message.AuthErrorMessage.TOO_MANY_VERIFY_REQUEST
import devkidult.git.springuserapi.exceptions.message.AuthErrorMessage.WRONG_VERIFY_CODE
import devkidult.git.springuserapi.repository.SmsVerifyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SmsVerifyService(private val smsVerifyRepository: SmsVerifyRepository) {

    fun create(phone: String): String {
        val verify = getById(phone)?.also {
            if (it.count == 3) {
                throw AuthenticationCodeException(TOO_MANY_VERIFY_REQUEST)
            }
            it.count += 1
            it.code = it.generateCode()
            it.expiredTime = 60 * 30
        } ?: SmsVerify(phone)
        smsVerifyRepository.save(verify)
        return verify.code
    }

    fun getById(phone: String): SmsVerify? = smsVerifyRepository.findById(phone).orElse(null)

    fun verify(request: UserDto.VerifyByPhone) {
        val verify = getById(request.phone) ?: throw AuthenticationCodeException(NOT_FOUND_VERIFY)
        if (request.code == verify.code) {
            verify.expiredTime = -1
            smsVerifyRepository.save(verify)
        } else {
            throw AuthenticationCodeException(WRONG_VERIFY_CODE)
        }
    }

    fun verifiedAndDelete(phone: String) {
        getById(phone) ?: throw IllegalAccessException()
        delete(phone)
    }

    fun delete(phone: String) {
        smsVerifyRepository.deleteById(phone)
    }
}
