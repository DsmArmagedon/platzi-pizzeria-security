package com.platzi.pizzeria.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Anotación de spring framework
@Configuration
@EnableMethodSecurity(securedEnabled = true) //Habilita la seguridad por roles en servicios especificos: OrderService getCustomerOrders()
public class SecurityConfig {
    @Autowired
    private JwtUtil jwtUtil;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Permite deshabilitar el csrf de las peticiones
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/auth/login/**").permitAll()
                        .requestMatchers("api/pizzas/**").permitAll() // Permitir a todos los usuarios acceder a /pizzas/**
                        //.requestMatchers(HttpMethod.GET, "api/**").permitAll() // Permitir todas las peticiones get de la url
                        //.requestMatchers(HttpMethod.GET, "api/**").hasAnyRole("ADMIN", "CUSTOMER") // Permitir todas las peticiones get de la url api hacia arriba a los roles ADMIN y CUSTOMER
                        .requestMatchers(HttpMethod.GET, "api/customers/**").hasAnyRole("ADMIN", "CUSTOMER") // Permitir todas las peticiones get de la url api hacia arriba a los roles ADMIN y CUSTOMER
                        .requestMatchers(HttpMethod.POST, "api/pizzas/**").hasRole("ADMIN") // Permitir todas las peticiones post de la url api hacia arriba solo al rol ADMIN
                        .requestMatchers("/api/orders/random").hasAuthority("RANDOM_ORDER") // El usuario que tenga ese permiso puntual podra ejecutar ese pattern tomar en cuenta que esto se hace porque abajo lo bloquea para todolo que tenga /api/orders/**
                        .requestMatchers(HttpMethod.PUT).denyAll() // Denegar todas la peticiones put
                        .anyRequest().authenticated()) // Requerir autenticación para cualquier otra solicitud
//                .httpBasic(Customizer.withDefaults()); // Habilitar autenticación básica HTTP
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Incluir filtro propio en la seguridad
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

//    /**
//     * Crear usuario en memoria y reemplaza el usuario que crea automaticamente el framework
//     */
//    @Bean
//    public UserDetailsService memoryUsers() {
//        UserDetails admin = User.builder().username("admin").password(passwordEncoder().encode("admin")).roles("ADMIN").build();
//        UserDetails customer = User.builder().username("customer").password(passwordEncoder().encode("customer")).roles("CUSTOMER").build();
//
//        return new InMemoryUserDetailsManager(admin, customer);
//    }

    /**
     * Crea un password encoder para ocultar la contraseña en este caso utiliza bcrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
