package com.kharitonovalilya.event_registration_api.repository;

import com.kharitonovalilya.event_registration_api.entity.Event;
import com.kharitonovalilya.event_registration_api.entity.Registration;
import com.kharitonovalilya.event_registration_api.entity.User;
import com.kharitonovalilya.event_registration_api.model.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByEventAndUserAndStatusIn(Event event, User user, List<RegistrationStatus> statuses);

    long countByEventAndStatus(Event event, RegistrationStatus status);

    List<Registration> findByEvent(Event event);

    Optional<Registration> findByEventAndUserAndStatusIn(Event event, User user, List<RegistrationStatus> statuses);

    Optional<Registration> findFirstByEventAndStatusOrderByRegisteredAtAsc(Event event, RegistrationStatus status);

    List<Registration> findByUserAndStatusIn(User user, List<RegistrationStatus> statuses);

    @Query("""
            select case when count(r) > 0 then true else false end
            from Registration r
            where r.user = :user
             and r.status in :statuses
             and r.event.startsAt < :targetEndsAt
             and :targetStartsAt < r.event.endsAt
            """)
    boolean existsOverlappingActiveRegistration(
            @Param("user") User user,
            @Param("statuses") List<RegistrationStatus> statuses,
            @Param("targetStartsAt")LocalDateTime targetStartsAt,
            @Param("targetEndsAt") LocalDateTime targetEndsAt
            );
}
