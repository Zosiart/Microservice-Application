package nl.tudelft.sem.template.authentication.controllers;

import nl.tudelft.sem.template.authentication.authentication.JwtTokenGenerator;
import nl.tudelft.sem.template.authentication.authentication.JwtUserDetailsService;
import nl.tudelft.sem.template.authentication.domain.user.service.RegistrationService;
import nl.tudelft.sem.template.authentication.models.AuthenticationRequestModel;
import nl.tudelft.sem.template.authentication.models.AuthenticationResponseModel;
import nl.tudelft.sem.template.authentication.models.RegistrationRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthenticationController {

    public static final String USER_DISABLED_STATUS = "USER_DISABLED";
    private static final String INVALID_CREDENTIALS_STATUS = "INVALID_CREDENTIALS";

    private final transient AuthenticationManager authenticationManager;
    private final transient JwtTokenGenerator jwtTokenGenerator;
    private final transient JwtUserDetailsService jwtUserDetailsService;
    private final transient RegistrationService registrationService;

    /**
     * Instantiates a new UsersController.
     *
     * @param authenticationManager the authentication nl.tudelft.sem.template.example.manager
     * @param jwtTokenGenerator     the token generator
     * @param jwtUserDetailsService the user service
     * @param registrationService   the registration service
     */
    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenGenerator jwtTokenGenerator,
                                    JwtUserDetailsService jwtUserDetailsService,
                                    RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.registrationService = registrationService;
    }

    /**
     * Endpoint for authentication.
     *
     * @param request The login model
     * @return JWT token if the login is successful
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseModel> authenticate(@RequestBody AuthenticationRequestModel request)
            throws ResponseStatusException {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));
        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, USER_DISABLED_STATUS, e);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS_STATUS, e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        }

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(request.getUsername());
        final String jwtToken = jwtTokenGenerator.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponseModel(jwtToken));
    }

    /**
     * Endpoint for registration.
     *
     * @param request The registration model
     * @return 200 OK if the registration is successful
     * @throws Exception if a user with this netid already exists
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegistrationRequestModel request) throws ResponseStatusException {
        try {
            registrationService.registerUser(request);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
