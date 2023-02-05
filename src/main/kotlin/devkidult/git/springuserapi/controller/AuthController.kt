package devkidult.git.springuserapi.controller

import devkidult.git.springuserapi.dto.UserDto
import devkidult.git.springuserapi.service.AuthService
import devkidult.git.springuserapi.service.SmsVerifyService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService, private val smsVerifyService: SmsVerifyService) {

    @GetMapping("/verify-code")
    @ResponseStatus(HttpStatus.CREATED)
    fun getVerifyCode(@RequestParam phone: String) = smsVerifyService.create(phone)

    @PostMapping("/verify-code")
    @ResponseStatus(HttpStatus.OK)
    fun verifyCode(@Validated @RequestBody request: UserDto.VerifyByPhone) = smsVerifyService.verify(request)

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@Validated @RequestBody request: UserDto.Request) = authService.signUp(request)
}
