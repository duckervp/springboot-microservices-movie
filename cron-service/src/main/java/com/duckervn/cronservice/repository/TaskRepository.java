package com.duckervn.cronservice.repository;

import com.duckervn.cronservice.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, String> {
}
