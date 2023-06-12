package com.gcoce.bc.ws.services.beneficio;

import com.gcoce.bc.ws.entities.beneficio.ERole;
import com.gcoce.bc.ws.entities.beneficio.Role;
import com.gcoce.bc.ws.entities.beneficio.User;
import com.gcoce.bc.ws.entities.beneficio.UserDetailsImpl;
import com.gcoce.bc.ws.exceptions.AuthBadRequestException;
import com.gcoce.bc.ws.exceptions.BeneficioException;
import com.gcoce.bc.ws.exceptions.HcException;
import com.gcoce.bc.ws.payload.request.LoginRequest;
import com.gcoce.bc.ws.payload.request.SignupRequest;
import com.gcoce.bc.ws.payload.response.JwtResponse;
import com.gcoce.bc.ws.payload.response.SuccessResponse;
import com.gcoce.bc.ws.repositories.beneficio.RoleRepository;
import com.gcoce.bc.ws.repositories.beneficio.UserRepository;
import com.gcoce.bc.ws.security.configurations.JwtManager;
import com.gcoce.bc.ws.utils.HcaptchaError;
import com.gcoce.bc.ws.utils.Validations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(transactionManager = "beneficioTransactionManager")
public class AuthSvc {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtManager jwtUtils;
    private final UserDetailsSvcImpl userDetailsService;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @Value("${hcaptcha.verify.url}")
    private String hCaptchaVerifyUrl;

    @Value("${hcaptcha.verify.secret}")
    private String hCaptchaVerifySecret;

    @Value("${hcaptcha.user}")
    private String hCaptchaUser;
    private static final Logger logger = LoggerFactory.getLogger(AuthSvc.class);

    public AuthSvc(UserRepository userRepository, RoleRepository roleRepository,
                   PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtManager jwtUtils, UserDetailsSvcImpl userDetailsService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;

    }

    public ResponseEntity<?> verifyHCaptcha(String hCaptchaResponse) {

        Map<String, String> body = new HashMap<>();
        String errorMessage;
        body.put("secret", hCaptchaVerifySecret);
        body.put("response", hCaptchaResponse);
        logger.debug("Request body for hcaptcha: {}", body);
        ResponseEntity<Map> responseEntity = restTemplateBuilder.build().postForEntity(hCaptchaVerifyUrl + "?secret={secret}&response={response}", body, Map.class, body);
        logger.debug("Response from hcaptcha: {}", responseEntity);
        Map responseBody = responseEntity.getBody();
        boolean hcaptchaSuccess = (Boolean) Objects.requireNonNull(responseBody).get("success");
        if (!hcaptchaSuccess) {
            List<String> errorCodes = (List) responseBody.get("error-codes");
            if (errorCodes != null) {
                errorMessage = errorCodes.stream().map(HcaptchaError.HCAPTCHA_ERROR_CODE::get)
                        .collect(Collectors.joining(", "));
            } else {
                errorMessage = "Token ya fue utilizado, por favor intente de nuevo";
            }
            throw new HcException(errorMessage);
        }
        final UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(hCaptchaUser);
        final String jwt = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    public ResponseEntity<?> authenticateUserSvc(LoginRequest loginRequest) throws AuthBadRequestException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (DisabledException e) {
            throw new AuthBadRequestException(e.getMessage());
        } catch (BadCredentialsException e) {
            throw new AuthBadRequestException("Credenciales Invalidas");
        }

        final UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(loginRequest.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails);
        /*List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());*/
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    public ResponseEntity<?> registerUserSvc(SignupRequest signUpRequest) throws AuthBadRequestException {
        String message;
        if (existsUserSvc(signUpRequest.getUsername())) {
            message = String.format("El usuario %s ya existe", signUpRequest.getUsername());
            throw new AuthBadRequestException(message);
        }
        if (existsEmailSvc(signUpRequest.getEmail())) {
            message = String.format("El email %s ya existe", signUpRequest.getEmail());
            throw new AuthBadRequestException(message);
        }/**/

        try {
            User user = new User(signUpRequest.getUsername(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()));

            Set<String> strRoles = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();
            logger.info("strRoles " + strRoles);
            if (strRoles.isEmpty()) {
                logger.error("role esta vacio ");
                throw new AuthBadRequestException("El rol esta vacio");

            } else {
                logger.info("validando role ");
                strRoles.forEach(role -> {
                    if (role.isEmpty()) {
                        logger.info("role empty");
                        throw new AuthBadRequestException("Rol no valido");
                    }
                    switch (role) {
                        case "admin" -> {
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new AuthBadRequestException("Rol no valido"));
                            roles.add(adminRole);
                        }
                        case "beneficio" -> {
                            Role beneficioRole = roleRepository.findByName(ERole.ROLE_BENEFICIO)
                                    .orElseThrow(() -> new AuthBadRequestException("Rol no valido"));
                            roles.add(beneficioRole);
                        }
                        case "agricultor" -> {
                            Role agricultorRole = roleRepository.findByName(ERole.ROLE_AGRICULTOR)
                                    .orElseThrow(() -> new AuthBadRequestException("Rol no valido"));
                            roles.add(agricultorRole);
                        }
                        case "peso" -> {
                            Role pesoCabalRole = roleRepository.findByName(ERole.ROLE_PESO_CABAL)
                                    .orElseThrow(() -> new AuthBadRequestException("Rol no valido"));
                            roles.add(pesoCabalRole);
                        }
                        case "user" -> {
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new AuthBadRequestException("Rol no valido"));
                            roles.add(userRole);
                        }
                        default -> {
                        }
                    }
                });
            }
            user.setRoles(roles);
            logger.info("user " + user);
            if (user.getRoles().isEmpty()) {
                logger.error("rol no valido");
                throw new AuthBadRequestException("Rol no valido");
            }
            userRepository.save(user);
            message = String.format("Usuario %s a sido registrado exitosamente", user.getUsername());
            return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, message));
        } catch (AuthBadRequestException e) {
            throw new AuthBadRequestException(e.getMessage());
        }
    }

    public boolean existsUserSvc(String user) {
        return userRepository.existsByUsername(user);
    }

    public boolean existsEmailSvc(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean validateUserToken(String bearerToken, String user) {
        String token = bearerToken.substring(7);
        String userToken = jwtUtils.getUsernameFromToken(token);
        return !Validations.compareStrings(userToken, user);
    }

    public String userFromToken(String bearerToken) {
        String token = bearerToken.substring(7);
        return jwtUtils.getUsernameFromToken(token);
    }
}
