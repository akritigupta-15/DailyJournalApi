package DailyJournal.DailyJournal.Repository;
import DailyJournal.DailyJournal.Model.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByUser(User user);
    //findByUser → lets you get only the logged-in user’s journal entries.
}
