package org.example.casa2girardot.Controllers;

import jakarta.validation.Valid;
import org.example.casa2girardot.Dtos.AuthResponseDTO;
import org.example.casa2girardot.Dtos.LoginDTO;
import org.example.casa2girardot.Security.CustomUserDetailsService;
import org.example.casa2girardot.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody LoginDTO loginRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Email o contraseña incorrectos", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();

        final String jwt = jwtUtil.generateToken(userDetails, role);

        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(jwt)
                .email(userDetails.getUsername())
                .tipoUsuario(role)
                .build());
    }
}