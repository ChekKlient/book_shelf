package com.ChekKlient.book_shelf;

import com.ChekKlient.book_shelf.authentication.AccessControl;
import com.ChekKlient.book_shelf.authentication.BasicAccessControl;
import com.ChekKlient.book_shelf.authentication.LoginScreen;
import com.ChekKlient.book_shelf.authentication.LoginScreen.LoginListener;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Binder;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import java.util.List;

@SpringUI
public class BookShelfUI extends UI {
    private AccessControl accessControl = new BasicAccessControl();

    @Autowired
    private GenreService service;
    private Genre genre;
    private Binder<Genre> binder = new Binder<>(Genre.class);
    private Grid<Genre> grid = new Grid(Genre.class);
    private TextField title = new TextField("Title");
    private Button save = new Button("Save", e -> saveGenre());

    @Override
    protected void init(VaadinRequest request) {
        Responsive.makeResponsive(this);
        setLocale(request.getLocale());
        if (!accessControl.isUserSignedIn()) {
            setContent(new LoginScreen(accessControl, new LoginListener() {
                @Override
                public void loginSuccessful() {
                    showMainView();
                }
            }));
        } else {
            showMainView();
        }
    }

    private void showMainView() {
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
        if (grid.asSingleSelect().isEmpty()||!accessControl.isUserInRole("admin")) {
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
    public static BookShelfUI get() {
        return (BookShelfUI) UI.getCurrent();
    }
    public AccessControl getAccessControl() {
        return accessControl;
    }
    @WebServlet(urlPatterns = "/*", name = "BookShelfUIServelet", asyncSupported = true)
    @VaadinServletConfiguration(ui = BookShelfUI.class, productionMode = false)
    public static class BookShelfUIServelet extends VaadinServlet {
    }
}
