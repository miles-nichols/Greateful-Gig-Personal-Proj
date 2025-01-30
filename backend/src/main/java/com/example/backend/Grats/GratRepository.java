package com.example.backend.Grats;

import com.example.backend.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GratRepository extends JpaRepository<Grat, Long> {

    @Query("SELECT g FROM Grat g " +
            "JOIN g.users u " +
            "WHERE u.username = :username " +
            "OR u.username IN (SELECT f.username FROM User f JOIN f.friends friends WHERE friends.username = :username)")
    List<Grat> findGratsForUserAndFriends(@Param("username") String username);



    List<Grat> findAllByUsers_UsernameIn(List<String> usernames);

    @Query("SELECT g FROM Grat g WHERE g.user.username = :username ORDER BY g.gratDate DESC")
    List<Grat> findByUserUsernameOrderByGratDateDesc(@Param("username") String username);

}
