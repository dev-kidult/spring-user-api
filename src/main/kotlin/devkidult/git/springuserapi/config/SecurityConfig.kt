package devkidult.git.springuserapi.config

import com.fasterxml.jackson.databind.ObjectMapper
import devkidult.git.springuserapi.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.CorsUtils
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository,
    private val loginSuccessHandler: LoginSuccessHandler,
    private val loginFailureHandler: LoginFailureHandler,
    private val authenticationConfiguration: AuthenticationConfiguration,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
            .headers()
            .frameOptions().sameOrigin().and()
            .cors().and()
            .authorizeRequests()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .antMatchers("/api/auth/**", "/h2-console/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().disable()
            .addFilterAt(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager

    @Bean
    fun customUsernamePasswordAuthenticationFilter(): UsernamePasswordAuthenticationFilter =
        AuthenticationFilter(objectMapper, userRepository, passwordEncoder()).apply {
            setFilterProcessesUrl("/api/auth/sign-in")
            usernameParameter = "loginId"
            passwordParameter = "password"
            setAuthenticationManager(authenticationManager(authenticationConfiguration))
            setAuthenticationSuccessHandler(loginSuccessHandler)
            setAuthenticationFailureHandler(loginFailureHandler)
        }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource = CorsConfiguration().apply {
        addAllowedOriginPattern("*")
        addAllowedHeader("*")
        addAllowedMethod("*")
        allowCredentials = true
    }.let {
        UrlBasedCorsConfigurationSource().apply { registerCorsConfiguration("/**", it) }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
