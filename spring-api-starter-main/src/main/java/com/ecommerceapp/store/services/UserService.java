package com.ecommerceapp.store.services;


import com.ecommerceapp.store.dtos.AuthRequest;
import com.ecommerceapp.store.dtos.AuthResponse;
import com.ecommerceapp.store.dtos.UserDto;
import com.ecommerceapp.store.entities.Role;
import com.ecommerceapp.store.entities.User;
import com.ecommerceapp.store.mappers.UserMapper;
import com.ecommerceapp.store.mappers.UserMapperImpl;
import com.ecommerceapp.store.repositories.UserRepository;
import com.ecommerceapp.store.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private UserMapper userMapper = new UserMapperImpl();
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    //  Register
    public AuthResponse register(AuthRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));

        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER); // default
        }

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token,userMapper.toDto(user));
    }

    //  Login
    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token,userMapper.toDto(user));
    }
}
