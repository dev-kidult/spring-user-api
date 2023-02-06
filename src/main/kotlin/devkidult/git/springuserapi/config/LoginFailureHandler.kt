package devkidult.git.springuserapi.config

import com.fasterxml.jackson.databind.ObjectMapper
import devkidult.git.springuserapi.dto.LoginDto
import devkidult.git.springuserapi.exceptions.AuthenticationCodeException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoginFailureHandler(private val objectMapper: ObjectMapper) : AuthenticationFailureHandler {
    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException,
    ) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        response.characterEncoding = "UTF-8"

        if (exception is AuthenticationCodeException) {
            objectMapper.writeValue(response.writer, FailedResponse(exception.message, exception.errorCode))
        } else {
            objectMapper.writeValue(response.writer, LoginDto.Response(false, exception.message))
        }
    }
}

data class FailedResponse(val message: String, val errorCode: String?)
