package task4;

import java.util.Objects;

public class Movie {

    private final String title;

    public String getTitle() {
        return title;
    }

    public Movie(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(title, movie.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return "Фильм{" +
                "Название='" + title + '\'' +
                '}';
    }
}
