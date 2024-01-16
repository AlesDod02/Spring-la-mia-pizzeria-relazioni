package org.learning.springlamiapizzeriacrudrelazioni.repository;

import org.learning.springlamiapizzeriacrudrelazioni.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Integer> {
}
