package devkidult.git.springuserapi.controller

import devkidult.git.springuserapi.exceptions.AuthenticationCodeException
import javax.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import java.util.*

data class ErrorResponse(
    val path: String,
    val title: String,
    val message: String,
    val errorCode: String? = null,
)

@RestControllerAdvice
class ExceptionAdvice {

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleDefaultException(request: HttpServletRequest, e: Exception) =
        ErrorResponse(path = request.requestURI, title = "서버 오류가 발생했습니다.", message = e.message ?: "empty message")

    @ExceptionHandler(AuthenticationCodeException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAuthenticationCodeException(request: HttpServletRequest, e: AuthenticationCodeException) =
        ErrorResponse(path = request.requestURI, title = "인증 오류가 발생했습니다.", message = e.message, errorCode = e.errorCode)

    @ExceptionHandler(NoHandlerFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNoHandlerFoundException(request: HttpServletRequest, e: NoHandlerFoundException) =
        ErrorResponse(path = request.requestURI, title = "존재하지 않는 API입니다.", message = e.message ?: "empty message")
}
