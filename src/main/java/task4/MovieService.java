package task4;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class MovieService {

    private final Map<Movie, List<Rating<? extends Number>>> ratingsMap = new ConcurrentHashMap<>();

    public void addRating(Movie movie, Rating<? extends Number> rating) {
        if (rating.getValue().doubleValue() < 1 || rating.getValue().doubleValue() > 10){
            throw new IllegalArgumentException("Рейтинг должен быть от 1 до 10");
        }
        ratingsMap.computeIfAbsent(movie, k -> new CopyOnWriteArrayList<>()).add(rating);
    }

    public double calculateAverageRating(Movie movie) {
        List<Rating<? extends Number>> ratings = ratingsMap.get(movie);
        if (ratings == null || ratings.isEmpty()) {
            return 0;
        }
        return ratings.stream()
                .mapToDouble(r -> r.getValue().doubleValue())
                .average()
                .orElse(0);
    }

    public List<Movie> getMoviesSortedByAverageRating() {
        return ratingsMap.keySet().stream()
                .sorted((m1, m2) -> Double.compare(calculateAverageRating(m1), calculateAverageRating(m2)))
                .collect(Collectors.toList());
    }

}