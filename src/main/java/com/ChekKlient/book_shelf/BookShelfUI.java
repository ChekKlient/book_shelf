package com.ChekKlient.book_shelf;

import com.vaadin.data.Binder;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SpringUI
public class BookShelfUI extends UI {

    @Autowired
    private GenreService service;

    private Genre genre;
    private Binder<Genre> binder = new Binder<>(Genre.class);

    private Grid<Genre> grid = new Grid(Genre.class);
    private TextField title = new TextField("Title");
    private Button save = new Button("Save", e -> saveGenre());

    @Override
    protected void init(VaadinRequest request) {
        updateGrid();
        grid.setColumns("title");
        grid.addSelectionListener(e -> updateForm());

        binder.bindInstanceFields(this);

        VerticalLayout layout = new VerticalLayout(grid, title, save);
        setContent(layout);
    }

    private void updateGrid() {
        List<Genre> genres = service.findAll();
        grid.setItems(genres);
        setFormVisible(false);
    }

    private void updateForm() {
        if (grid.asSingleSelect().isEmpty()) {
            setFormVisible(false);
        } else {
            genre = grid.asSingleSelect().getValue();
            binder.setBean(genre);
            setFormVisible(true);
        }
    }

    private void setFormVisible(boolean visible) {
        title.setVisible(visible);
        save.setVisible(visible);
    }

    private void saveGenre() {
        service.update(genre);
        updateGrid();
    }
}
