package devkidult.git.springuserapi.exceptions.message

enum class AuthErrorMessage(
    override val value: String,
    override val description: String,
) : ErrorMessage {

    TOO_MANY_VERIFY_REQUEST("TOO_MANY_VERIFY_REQUEST", "짧은 시간 너무 많은 요청을 하였습니다. 30분후에 다시 시도하세요."),
    NOT_FOUND_VERIFY("NOT_FOUND_VERIFY", "유효한 핸드폰 인증내역이 없습니다."),
    WRONG_VERIFY_CODE("WRONG_VERIFY_CODE", "인증코드가 일치하지 않습니다."),
    ALREADY_SIGN_UP_USER("ALREADY_SIGN_UP_USER", "이미 회원가입한 사용자입니다."),
    CHECK_ID_PASSWORD("CHECK_ID_PASSWORD", "아이디 혹은 비밀번호가 틀렸습니다.")
}
