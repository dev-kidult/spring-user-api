package devkidult.git.springuserapi.dto

import devkidult.git.springuserapi.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serial

class UserDetail(
    val id: Long,
    private val username: String,
    private val password: String,
    private val roles: MutableCollection<SimpleGrantedAuthority>,
) : UserDetails {
    companion object {
        @Serial
        private const val serialVersionUID: Long = 7499626256855753800L

        fun from(user: User): UserDetail = with(user) {
            UserDetail(id = id!!, username = email, password = password, roles = mutableListOf((SimpleGrantedAuthority(role.name))))
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = roles

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
