package devkidult.git.springuserapi.repository

import devkidult.git.springuserapi.domain.redis.SmsVerify
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SmsVerifyRepository : CrudRepository<SmsVerify, String> {
}
