package devkidult.git.springuserapi.controller

import devkidult.git.springuserapi.dto.UserDto
import devkidult.git.springuserapi.service.UserService
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/me")
    fun getVerifyCode() = userService.getMe()

    @PutMapping("/password")
    @ResponseStatus(OK)
    fun changePassword(request: UserDto.PasswordRequest) = userService.changePassword(request)
}
