package DailyJournal.DailyJournal.Controller;

import DailyJournal.DailyJournal.Model.User;
import DailyJournal.DailyJournal.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import DailyJournal.DailyJournal.util.JwtUtil;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final UserRepository userRepo;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepo,AuthenticationManager authManager,JwtUtil jwtUtil,PasswordEncoder passwordEncoder){
        this.userRepo=userRepo;
        this.authManager=authManager;
        this.jwtUtil=jwtUtil;
        this.passwordEncoder=passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        log.info(">>> REGISTER endpoint hit with: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // uses passwordEncoder.encode(...) to hash the plain text password before saving to DB.
        userRepo.save(user);
        return ResponseEntity.ok("User registered!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) throws Exception {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials", e);
        }
        //authManager.authenticate(...) uses MyUserDetailsService + passwordEncoder behind the scenes to check the password.
        final String jwt = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(jwt);
    }
}
