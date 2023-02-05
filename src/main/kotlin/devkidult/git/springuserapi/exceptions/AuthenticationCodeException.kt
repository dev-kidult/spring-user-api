package devkidult.git.springuserapi.exceptions

import devkidult.git.springuserapi.exceptions.message.ErrorMessage
import org.springframework.security.core.AuthenticationException
import java.io.Serial

data class AuthenticationCodeException(override val message: String = "유효하지 않은 인증정보입니다.") : AuthenticationException(message) {
    companion object {

        @Serial
        private const val serialVersionUID: Long = 8935679830306248122L
    }

    var errorCode: String? = null

    constructor(message: String, errorCode: String) : this(message) {
        this.errorCode = errorCode
    }

    constructor(message: String, errorMessage: ErrorMessage) : this(message) {
        this.errorCode = errorMessage.value
    }

    constructor(errorMessage: ErrorMessage) : this(errorMessage.description) {
        this.errorCode = errorMessage.value
    }
}
