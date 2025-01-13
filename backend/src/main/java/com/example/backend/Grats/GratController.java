package com.example.backend.Grats;

import com.example.backend.Users.User;
import com.example.backend.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/grat")
public class GratController {

    @Autowired
    private GratService gratService;

    // POST endpoint to add a Grat for a user
    @PostMapping("/addGrat/{username}")
    public ResponseEntity<Map<String, String>> addGrat(@PathVariable String username, @RequestBody Grat grat) {
        try {
            gratService.addGrat(username, grat);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Grat added successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    // GET endpoint to get all Grats for CURRENT user
    @GetMapping("/getGrats/{username}")
    public ResponseEntity<List<Grat>> getGrats(@PathVariable String username) {
        List<Grat> grats = gratService.getGratsByUser(username);
        return ResponseEntity.ok(grats);
    }

    //GET endpoint to get all Grats for friends and current user
    @GetMapping("/userfriends/{username}")
    public ResponseEntity<List<Grat>> getUserAndFriendsGrats(@PathVariable String username) {
        List<Grat> grats = gratService.getGratsForUserAndFriends(username);

        if (grats.isEmpty()) {
            return ResponseEntity.noContent().build();  // 204 NO_CONTENT if no grats found
        }
        return ResponseEntity.ok(grats);  // 200 OK if grats are found
    }
}
