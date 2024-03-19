package com.example;

import com.example.config.RouteConfig;
import com.example.handler.MovieReviewHandler;
import com.example.model.MovieReview;
import com.example.repository.MovieReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.just;



@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles(profiles = "tests")
class SpringReactiveWebHandlerApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private MovieReviewRepository repository;

	final String API_URI = "/v1/movie-review";

	@BeforeEach
	public void setUp(){
		repository.deleteAll();
	}

	@Test
	void testAddMovieReview(){

		var moviewReview = new MovieReview(null, "1", "Nice Moview", 4.0);

	    when(repository.save(any())).thenReturn(just(moviewReview));
		webTestClient.post()
				.uri(API_URI)
				.bodyValue(moviewReview)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectBody(MovieReview.class)
				.consumeWith(r->{
					var savedReview = r.getResponseBody();
					assert savedReview != null;
					assert savedReview.getMovieId().equals("1");
				});
	}

}
