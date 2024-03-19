package com.example.handler;

import com.example.model.MovieReview;
import com.example.repository.MovieReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class MovieReviewHandler {

    private final MovieReviewRepository repository;

    public Mono<ServerResponse> addReviews(ServerRequest request) {

        return request.bodyToMono(MovieReview.class)
                .doOnNext(review->log.debug("Review Received {}", review))
                .flatMap(repository::save)
                .doOnError(data->log.error("Error in adding review", data))
                .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
    }

    public Mono<ServerResponse> getReviews(ServerRequest request) {

        var reviewFlux = repository.findAll();
        return ServerResponse.ok().body(reviewFlux, MovieReview.class);
    }

    public Mono<ServerResponse> updateReview(ServerRequest request) {

        var reviewMono = repository.findById(request.pathVariable("id"));

        return reviewMono
                .doOnNext(a->log.debug("Updating {}", a))
                .flatMap(movieReview -> request.bodyToMono(MovieReview.class)
                        .map(mapReview->{
                            movieReview.setRating(mapReview.getRating());
                            movieReview.setReview(mapReview.getReview());
                            return movieReview;
                        })
                        .flatMap(repository::save)
                        .flatMap(savedR->ServerResponse.ok().bodyValue(savedR)));

    }

    public Mono<ServerResponse> deleteReview(ServerRequest request) {

        var id = request.pathVariable("id");
        return repository.deleteById(id)
                .doOnNext(a->log.debug("deleted {}", a))
                .then(ServerResponse.noContent().build());
    }
}
