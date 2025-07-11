package DailyJournal.DailyJournal.Service;

import DailyJournal.DailyJournal.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import DailyJournal.DailyJournal.Model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {


    private final UserRepository userRepo;

    public MyUserDetailsService(UserRepository userRepo)
    {
        this.userRepo=userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), new ArrayList<>());

        //When logging in → to find user by username for password check.
        //When validating JWT → to find user by username for token check.
    }
}
