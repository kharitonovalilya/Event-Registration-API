package com.kharitonovalilya.event_registration_api.repository;

import com.kharitonovalilya.event_registration_api.entity.Event;
import com.kharitonovalilya.event_registration_api.entity.User;
import com.kharitonovalilya.event_registration_api.model.EventStatus;
import com.kharitonovalilya.event_registration_api.model.RegistrationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStatus(EventStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Event e where e.id = :id")
    Optional<Event> findByIdForUpdate(@Param("id") Long id);

    @Query("""
            select e
            from Event e
            where e.status = :eventStatus
             and e.capacity > (
                select count(r)
                from Registration r
                where r.event = e
                and r.status = :confirmedStatus
            )
            order by e.startsAt asc
            """)
    List<Event> findEventsWithAvailableSeats(
            @Param("eventStatus") EventStatus eventStatus,
            @Param("confirmedStatus")RegistrationStatus registrationStatus
            );

    @Query("""
            select e
            from Event e
            where e.status = :eventStatus
             and not exists (
                select r
                from Registration r
                where r.user = :user
                 and r.status in :activeStatuses
                 and (
                    r.event = e
                    or (
                        r.event.startsAt < e.endsAt
                        and e.startsAt < r.event.endsAt
                    )
                 )
             )
             order by e.startsAt asc
            """)
    List<Event> findAvailableEventsForUser(
            @Param("user") User user,
            @Param("eventStatus") EventStatus eventStatus,
            @Param("activeStatuses")List<RegistrationStatus> activeStatuses
            );

    @Query("""
            select e
            from Event e
            where e.status = :eventStatus
             and e.capacity > (
                select count(r)
                from Registration r
                where r.event = e
                and r.status = :confirmedStatus
             )
             and not exists (
                select r
                from Registration r
                where r.user = :user
                 and r.status in :activeStatuses
                 and (
                    r.event = e
                    or (
                        r.event.startsAt < e.endsAt
                        and e.startsAt < r.event.endsAt
                    )
                 )
             )
             order by e.startsAt asc
            """)
    List<Event> findAvailableSeatsWithSeatsForUser(
            @Param("user") User user,
            @Param("eventStatus") EventStatus eventStatus,
            @Param("activeStatuses")List<RegistrationStatus> activeStatuses,
            @Param("confirmedStatus")RegistrationStatus confirmedStatus
    );
}
