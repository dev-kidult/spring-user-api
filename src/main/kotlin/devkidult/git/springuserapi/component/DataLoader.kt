package devkidult.git.springuserapi.component

import devkidult.git.springuserapi.domain.User
import devkidult.git.springuserapi.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataLoader(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        val users = mutableListOf<User>()
        (1..9).forEach {
            users.add(
                User(
                    email = "email$it@email.com",
                    password = passwordEncoder.encode("1234"),
                    name = "user$it",
                    nickname = "nickname$it",
                    phone = "0101234567$it",
                )
            )
        }
        userRepository.saveAll(users)
    }
}
