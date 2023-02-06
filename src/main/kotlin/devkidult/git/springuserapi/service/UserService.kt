package devkidult.git.springuserapi.service

import devkidult.git.springuserapi.domain.QUser.user
import devkidult.git.springuserapi.dto.UserDetail
import devkidult.git.springuserapi.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findOne(user.email.eq(username).or(user.phone.eq(username)))
            .orElseThrow { IllegalArgumentException("NOT_FOUND_USER") }
            .let { UserDetail.from(it) }

}
