package devkidult.git.springuserapi.config

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import devkidult.git.springuserapi.domain.QUser.user
import devkidult.git.springuserapi.exceptions.AuthenticationCodeException
import devkidult.git.springuserapi.exceptions.message.AuthErrorMessage
import devkidult.git.springuserapi.repository.UserRepository
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.stream.Collectors

class AuthenticationFilter(
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UsernamePasswordAuthenticationFilter() {

    private val holder: ThreadLocal<MutableMap<String, String?>> = ThreadLocal()

    override fun obtainUsername(request: HttpServletRequest): String? = obtainParam(request, usernameParameter)

    override fun obtainPassword(request: HttpServletRequest): String? = obtainParam(request, passwordParameter)

    private fun obtainParam(request: HttpServletRequest, paramKey: String): String? {
        val requestBody = holder.get()
        return if (requestBody != null) requestBody[paramKey] else request.getParameter(paramKey)
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        if (!HttpMethod.POST.name.equals(request.method, true)) {
            throw AuthenticationServiceException("Authentication method not supported: ${request.method}")
        }

        holder.set(null)

        val contentType = request.getHeader(HttpHeaders.CONTENT_TYPE) ?: ""
        if (contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            val map = objectMapper.readValue(
                request.reader.lines().collect(Collectors.joining()),
                object : TypeReference<MutableMap<String, String?>>() {})

            holder.set(map)
        }

        val username = obtainUsername(request)
        val password = obtainPassword(request)

        if (username.isNullOrBlank() || password.isNullOrBlank()) {
            throw AuthenticationCodeException(AuthErrorMessage.CHECK_ID_PASSWORD)
        }

        val user = userRepository.findOne(user.email.eq(username).or(user.phone.eq(username)))
            .orElseThrow { AuthenticationCodeException(AuthErrorMessage.CHECK_ID_PASSWORD) }

        if (!passwordEncoder.matches(password, user.password)) {
            throw AuthenticationCodeException(AuthErrorMessage.CHECK_ID_PASSWORD)
        }

        val token = UsernamePasswordAuthenticationToken(username, password)
        setDetails(request, token)

        return authenticationManager.authenticate(token)
    }
}
