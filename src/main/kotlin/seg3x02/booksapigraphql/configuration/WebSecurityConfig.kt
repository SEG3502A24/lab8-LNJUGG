package seg3x02.booksapigraphql.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors().and() // Permet les requêtes CORS
            .csrf().disable() // Désactive la protection CSRF pour l'API
            .authorizeHttpRequests { auth -> auth.anyRequest().permitAll() } // Permet l'accès à toutes les requêtes sans authentification
        return http.build()
    }
}
