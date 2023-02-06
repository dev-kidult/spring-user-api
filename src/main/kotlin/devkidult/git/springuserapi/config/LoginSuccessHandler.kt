package devkidult.git.springuserapi.config

import com.fasterxml.jackson.databind.ObjectMapper
import devkidult.git.springuserapi.dto.LoginDto
import devkidult.git.springuserapi.dto.UserDetail
import devkidult.git.springuserapi.repository.UserRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.time.LocalDateTime

@Component
class LoginSuccessHandler(private val userRepository: UserRepository, private val objectMapper: ObjectMapper) :
    AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        val userDetail = SecurityContextHolder.getContext().authentication.principal as UserDetail

        userRepository.updateLastLoginAt(userDetail.id, LocalDateTime.now())

        response.status = HttpStatus.OK.value()
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        objectMapper.writeValue(response.writer, LoginDto.Response(true, "Succeeded"))
    }
}
