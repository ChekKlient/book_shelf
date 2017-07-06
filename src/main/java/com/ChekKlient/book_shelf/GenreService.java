package com.ChekKlient.book_shelf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenreService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Genre> findAll() {
        return jdbcTemplate.query(
                "SELECT genre_id, title FROM genres",
                (rs, rowNum) -> new Genre(rs.getLong("id"),
                        rs.getString("title")));
    }

    public void update(Genre genre) {
        jdbcTemplate.update(
                "UPDATE genre SET title=? WHERE genre_id=?",
                genre.getTitle(), genre.getId());
    }

}
