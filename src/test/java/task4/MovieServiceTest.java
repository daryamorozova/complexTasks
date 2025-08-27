package task4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovieServiceTest {
    private MovieService movieService;
    private Movie movie1;
    private Movie movie2;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
        movie1 = new Movie("Movie 1");
        movie2 = new Movie("Movie 2");
    }

    @Test
    public void testAddRatingAndCalculateAverage() {
        movieService.addRating(movie1, new Rating<>(8));
        movieService.addRating(movie1, new Rating<>(10));
        double average = movieService.calculateAverageRating(movie1);
        assertEquals(9.0, average);
    }

    @Test
    public void testAddInvalidRating() {
        assertThrows(IllegalArgumentException.class, () ->
                movieService.addRating(movie1, new Rating<>(11)));
    }

    @Test
    public void testMoviesSortedByAverageRating() {
        movieService.addRating(movie1, new Rating<>(8));
        movieService.addRating(movie2, new Rating<>(9));
        movieService.addRating(movie2, new Rating<>(10));

        List<Movie> sortedMovies = movieService.getMoviesSortedByAverageRating();
        assertEquals(movie2, sortedMovies.get(0));
        assertEquals(movie1, sortedMovies.get(1));
    }
}
