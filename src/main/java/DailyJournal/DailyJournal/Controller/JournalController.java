package DailyJournal.DailyJournal.Controller;

import DailyJournal.DailyJournal.Model.JournalEntry;
import DailyJournal.DailyJournal.Repository.JournalEntryRepository;
import DailyJournal.DailyJournal.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import DailyJournal.DailyJournal.Model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalController {

    private final JournalEntryRepository journalRepo;
    private final UserRepository userRepo;

    public JournalController(JournalEntryRepository journalRepo, UserRepository userRepo) {
        this.journalRepo = journalRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllEntries() {
        List<JournalEntry> entries = journalRepo.findAll();
        return ResponseEntity.ok(entries);
    }

    @PostMapping
    public JournalEntry addEntry(Authentication authentication, @RequestBody JournalEntry entry){
    //Authentication authentication in your method:
    // Secures your endpoints — no one can pretend to be someone else.
        String username = authentication.getName(); // ✅ safe
        User user = userRepo.findByUsername(username).orElseThrow();
        entry.setUser(user);
        return journalRepo.save(entry);
    }

    @PutMapping("/{id}")
    public JournalEntry updateEntry(Authentication authentication, @PathVariable Long id, @RequestBody JournalEntry updated) {
        String username = authentication.getName();
        User user = userRepo.findByUsername(username).orElseThrow();
        JournalEntry entry = journalRepo.findById(id).orElseThrow();
        //findByUsername:When you need to look up the logged-in user by their username
        //findByUsername → "Who is the user making this request?"
        //findById → "Which entry are they trying to change/delete?"
        if (!entry.getUser().getId().equals(user.getId())) throw new RuntimeException("Unauthorized");
        entry.setTitle(updated.getTitle());
        entry.setContent(updated.getContent());
        return journalRepo.save(entry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEntry(Authentication authentication, @PathVariable Long id) {
        String username = authentication.getName();
        User user = userRepo.findByUsername(username).orElseThrow();
        JournalEntry entry = journalRepo.findById(id).orElseThrow();
        if (!entry.getUser().getId().equals(user.getId())) throw new RuntimeException("Unauthorized");
        journalRepo.delete(entry);
        return ResponseEntity.ok("Deleted");
    }
}
