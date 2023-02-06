package devkidult.git.springuserapi.dto

import devkidult.git.springuserapi.enums.Role
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class UserDto {

    data class Request(
        @field:Email
        @field:NotBlank
        @field:Size(max = 255)
        val email: String,
        @field:NotBlank
        @field:Size(max = 30)
        val nickname: String,
        @field:NotBlank
        @field:Size(min = 4, max = 30)
        val password: String,
        @field:NotBlank
        @field:Size(max = 20)
        val name: String,
        @field:NotBlank
        @field:Pattern(regexp = "^\\d{8,11}$")
        @field:Size(min = 11, max = 11)
        val phone: String,
    )

    data class VerifyByPhone(
        @field:NotBlank
        @field:Pattern(regexp = "^\\d{8,11}$")
        @field:Size(min = 11, max = 11)
        val phone: String,
        @field:NotBlank
        @field:Size(min = 6, max = 6)
        val code: String,
    )

    data class Me(
        val email: String,
        val nickname: String,
        val name: String,
        val phone: String,
        val role: Role,
    )

    data class PhoneRequest(
        @field:NotBlank
        @field:Pattern(regexp = "^\\d{8,11}$")
        @field:Size(min = 11, max = 11)
        val phone: String,
    )

    data class PasswordRequest(
        @field:NotBlank
        @field:Size(min = 4, max = 30)
        val newPassword: String,
        @field:NotBlank
        @field:Size(min = 4, max = 30)
        val repeatPassword: String,
    )

    data class PasswordResponse(
        val password: String
    )
}
