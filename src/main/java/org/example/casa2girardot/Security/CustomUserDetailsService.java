package org.example.casa2girardot.Security;

import org.example.casa2girardot.Entities.Administrador;
import org.example.casa2girardot.Entities.Arrendatario;
import org.example.casa2girardot.Repositories.AdministradorRepository;
import org.example.casa2girardot.Repositories.ArrendatarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdministradorRepository adminRepo;
    @Autowired
    private ArrendatarioRepository arrendatarioRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Intentar buscar como Administrador
        Optional<Administrador> admin = adminRepo.findByEmail(email);
        if (admin.isPresent()) {
            return User.builder()
                    .username(admin.get().getEmail())
                    .password(admin.get().getPassword()) // El hash en BD
                    .roles("ADMIN")
                    .build();
        }

        // 2. Si no es admin, intentar como Arrendatario
        Optional<Arrendatario> arrendatario = arrendatarioRepo.findByEmail(email);
        if (arrendatario.isPresent()) {
            return User.builder()
                    .username(arrendatario.get().getEmail())
                    .password(arrendatario.get().getPassword()) // El hash en BD
                    .roles("ARRENDATARIO")
                    .build();
        }

        throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
    }
}