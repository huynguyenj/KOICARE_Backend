package group7.se1876.kcs_backend.configuration;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity

public class SecurityConfig {

    @Value("${jwt.signerKey}")
    private String SIGNAL_KEY;

    private final String[] PUBLIC_ENDPOINTS = {"/api/user","/auth/login","auth/verifyToken"};
//    private final String[] ADMIN_ENDPOINTS = {"/api/getUsers"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->
                request .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
//                        .requestMatchers(HttpMethod.GET, ADMIN_ENDPOINTS).hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()); // any method with PUBLIC_ENDPOINTS is allowed to access without security check

        //Validate jwt
        httpSecurity.oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(jwtConfigurer ->jwtConfigurer.decoder(jwtDecoder())
                            .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                            .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // 401 error handle
                );
        httpSecurity.csrf(AbstractHttpConfigurer::disable); //turn off csrf to avoid forbidden

        return httpSecurity.build();
    }

    //Decode token to check it valid or not when user login
    @Bean
    JwtDecoder jwtDecoder(){
        // SecretKeySpec with 2 param: our signerKey from application.properties and algorithm that we are using to create token header
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNAL_KEY.getBytes(),"HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    //Set hasAuthority from SCOPE_... to ROLE_... in security spring (default in authority is SCOPE_...)
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){

        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }


}
