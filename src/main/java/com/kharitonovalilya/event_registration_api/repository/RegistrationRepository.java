package com.kharitonovalilya.event_registration_api.repository;

import com.kharitonovalilya.event_registration_api.entity.Event;
import com.kharitonovalilya.event_registration_api.entity.Registration;
import com.kharitonovalilya.event_registration_api.entity.User;
import com.kharitonovalilya.event_registration_api.model.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByEventAndUserAndStatusIn(Event event, User user, List<RegistrationStatus> statuses);

    long countByEventAndStatus(Event event, RegistrationStatus status);

    List<Registration> findByEvent(Event event);

    Optional<Registration> findByEventAndUserAndStatusIn(Event event, User user, List<RegistrationStatus> statuses);

    Optional<Registration> findFirstByEventAndStatusOrderByRegisteredAtAsc(Event event, RegistrationStatus status);
}
