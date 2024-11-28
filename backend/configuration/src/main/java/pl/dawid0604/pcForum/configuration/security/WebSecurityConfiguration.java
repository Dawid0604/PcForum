package pl.dawid0604.pcForum.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer.SessionFixationConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {
    private final UserDetailsServiceCustomImpl userDetailsServiceCustomImpl;
    private final AuthenticationEntryPointCustomImpl authenticationEntryPointCustomImpl;
    private final ObjectMapper objectMapper;

    private static final String COOKIE_NAME = "JSESSIONID";
    private static final String LOGIN_URL_ENDPOINT = "/api/v1/access/login";
    private static final String[] WHITELIST_ENDPOINTS = {
            LOGIN_URL_ENDPOINT , "/api/v1/access/register", "/api/v1/threads/categories",
            "/api/v1/threads/categories/{encryptedId}/sub", "/api/v1/threads/{encryptedId}",
            "/api/v1/thread/{encryptedId}", "/api/v1/post/{encryptedId}", "/api/v1/thread/handle/view/{encryptedId}",
            "/api/v1/post/newest", "/api/v1/threads/popular", "/api/v1/threads/creator/categories",
            "/api/v1/threads/creator/categories/{encryptedId}/sub"
    };

    @Bean
    @DependsOn("authenticationManager")
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.userDetailsService(userDetailsServiceCustomImpl)
                           .exceptionHandling(this::customize)
                           .csrf(AbstractHttpConfigurer::disable)
                           .cors(withDefaults())
                           .authorizeHttpRequests(this::customize)
                           .logout(this::customize)
                           .sessionManagement(this::customize)
                           .securityContext(this::customize)
                           .addFilterAt(new AuthenticationFilterCustomImpl(authenticationManager(httpSecurity), objectMapper, LOGIN_URL_ENDPOINT),
                                        UsernamePasswordAuthenticationFilter.class)
                           .build();
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
                                serializer.setCookieName(COOKIE_NAME);
                                serializer.setUseHttpOnlyCookie(false);
                                serializer.setCookiePath("/");
        return serializer;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
                          corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
                          corsConfiguration.addAllowedHeader(CONTENT_TYPE);
                          corsConfiguration.addAllowedHeader(AUTHORIZATION);
                          corsConfiguration.addAllowedMethod(GET);
                          corsConfiguration.addAllowedMethod(POST);
                          corsConfiguration.addAllowedMethod(PATCH);
                          corsConfiguration.addAllowedMethod(PUT);
                          corsConfiguration.addAllowedMethod(DELETE);
                          corsConfiguration.addAllowedMethod(OPTIONS);
                          corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                                        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                           .build();
    }

    private void customize(final SecurityContextConfigurer<HttpSecurity> config) {
        config.securityContextRepository(new HttpSessionSecurityContextRepository());
    }

    private void customize(final LogoutConfigurer<HttpSecurity> config) {
        config.logoutUrl("/api/v1/access/logout");
        config.addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(COOKIES)));
        config.deleteCookies(COOKIE_NAME);
        config.logoutSuccessHandler((_request, _response, _authentication) -> _response.setStatus(SC_OK));
    }

    private void customize(final SessionManagementConfigurer<HttpSecurity> config) {
        config.maximumSessions(1).maxSessionsPreventsLogin(true);
        config.sessionFixation(SessionFixationConfigurer::newSession);
        config.sessionCreationPolicy(STATELESS);
    }

    private void customize(final ExceptionHandlingConfigurer<HttpSecurity> config) {
        config.authenticationEntryPoint(authenticationEntryPointCustomImpl);
    }

    private void customize(final AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry config) {
        config.requestMatchers(WHITELIST_ENDPOINTS).permitAll()
              .anyRequest().authenticated();
    }
}
