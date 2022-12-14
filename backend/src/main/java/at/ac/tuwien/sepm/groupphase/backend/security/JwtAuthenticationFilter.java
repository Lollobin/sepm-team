package at.ac.tuwien.sepm.groupphase.backend.security;

import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.GENERIC_LOGIN_FAILURE_MSG;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AuthRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;
    private final AuthenticationFailureHandler failureHandler;
    private final AuthenticationSuccessHandler successHandler;

    public JwtAuthenticationFilter(
        AuthenticationManager authenticationManager,
        SecurityProperties securityProperties,
        JwtTokenizer jwtTokenizer,
        AuthenticationFailureHandler failureHandler,
        AuthenticationSuccessHandler successHandler) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
        this.failureHandler = failureHandler;
        this.successHandler = successHandler;
        setFilterProcessesUrl(securityProperties.getLoginUri());
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthRequestDto user = null;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), AuthRequestDto.class);
            // Compares the user with CustomUserDetailService#loadUserByUsername and check if the
            // credentials are correct
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        } catch (IOException e) {
            throw new BadCredentialsException("Wrong API request or JSON schema", e);
        } catch (BadCredentialsException e) {
            if (user != null && user.getEmail() != null) {
                LOGGER.error("Unsuccessful authentication attempt for user {}", user.getEmail());
                throw new BadCredentialsException("Bad credentials for user: " + user.getEmail(),
                    e);
            }
            throw new BadCredentialsException(GENERIC_LOGIN_FAILURE_MSG, e);
        } catch (LockedException e) {
            throw new LockedException(GENERIC_LOGIN_FAILURE_MSG, e);

        }
    }

    @Override
    protected void unsuccessfulAuthentication(
        HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {

        this.failureHandler.onAuthenticationFailure(request, response, failed);

    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult)
        throws IOException, ServletException {
        this.successHandler.onAuthenticationSuccess(request, response, chain, authResult);

        User user = ((User) authResult.getPrincipal());

        List<String> roles =
            user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        response.getWriter().write(jwtTokenizer.getAuthToken(user.getUsername(), roles));
        LOGGER.info("Successfully authenticated user {}", user.getUsername());
    }
}
