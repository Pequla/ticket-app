package com.pequla.ticket.repository;

import com.pequla.ticket.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

}
