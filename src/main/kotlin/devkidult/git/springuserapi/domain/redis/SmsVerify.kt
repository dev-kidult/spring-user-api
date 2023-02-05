package devkidult.git.springuserapi.domain.redis


import net.bytebuddy.utility.RandomString
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash(value = "smsVerify")
class SmsVerify(
    @Id
    var phone: String,
    var count: Int = 1,
    @TimeToLive
    var expiredTime: Long = 60 * 30
) {

    var code: String

    init {
        code = generateCode()
    }

    fun generateCode() = RandomString.make(6)
}
