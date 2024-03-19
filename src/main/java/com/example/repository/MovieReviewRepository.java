package com.example.repository;

import com.example.model.MovieReview;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovieReviewRepository extends ReactiveMongoRepository<MovieReview, String> {
}
