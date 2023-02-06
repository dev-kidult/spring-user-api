package devkidult.git.springuserapi.dto

class LoginDto {

    data class Response(
        val result: Boolean,
        val message: String?,
    )
}
