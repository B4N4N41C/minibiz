package ru.miniprog.minicrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.miniprog.minicrm.model.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

}
