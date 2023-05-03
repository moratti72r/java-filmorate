package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmorateApplicationTests {

    private final UserService userService;
    private final FilmService filmService;

    private final GenreDao genreDao;

    private final MpaDao mpaDao;

    private User newUser(Integer n) {
        User user = new User();
        user.setEmail("email@email" + n + ".ru");
        user.setLogin("login" + n);
        user.setName("name" + n);
        user.setBirthday(LocalDate.of(2000, 12, 15));
        return user;
    }

    @Test
    public void testFindUserById() {

        userService.create(newUser(1));
        userService.create(newUser(2));
        userService.create(newUser(3));

        User resultUser1 = userService.findById(1);
        User resultUser2 = userService.findById(2);
        User resultUser3 = userService.findById(3);

        assertThat(resultUser1).isNotNull();
        assertThat(resultUser1.getId()).isEqualTo(1);
        assertThat(resultUser1.getName()).isEqualTo("name1");
        assertThat(resultUser2.getId()).isEqualTo(2);
        assertThat(resultUser3.getId()).isEqualTo(3);
    }

    @Test
    public void testFindAllUser() {

        userService.create(newUser(1));
        userService.create(newUser(2));
        userService.create(newUser(3));

        List<User> users = userService.findAll();

        assertThat(users.size()).isEqualTo(3);
    }

    @Test
    public void testUpdateUser() {
        userService.create(newUser(1));
        userService.create(newUser(2));
        userService.create(newUser(3));

        User updateUser = newUser(4);
        updateUser.setId(2);

        User resultUser = userService.upDate(updateUser);

        assertThat(resultUser).isNotNull();
        assertThat(resultUser.getId()).isEqualTo(2);
        assertThat(resultUser.getName()).isEqualTo("name4");
        assertThat(resultUser.getLogin()).isEqualTo("login4");
        assertThat(resultUser.getEmail()).isEqualTo("email@email4.ru");
    }

    @Test
    public void testGetAllFriends() {
        User user = newUser(1);
        User friend1 = newUser(2);
        User friend2 = newUser(3);
        User friend3 = newUser(4);

        userService.create(user);
        userService.create(friend1);
        userService.create(friend2);
        userService.create(friend3);

        userService.addToFriends(1, 2);
        userService.addToFriends(1, 3);
        userService.addToFriends(1, 4);

        List<User> friends = userService.findAllFriends(1);
        assertThat(friends.size()).isEqualTo(3);

        userService.removeToFriends(1, 4);
        friends = userService.findAllFriends(1);
        assertThat(friends.size()).isEqualTo(2);

        List<Integer> idFriend = new ArrayList<>();
        for (User userr : friends) {
            idFriend.add(userr.getId());
        }
        assertThat(idFriend.contains(3)).isTrue();
        assertThat(idFriend.contains(2)).isTrue();
    }

    @Test
    public void testGetMutualFriends() {
        User user1 = newUser(1);
        User user2 = newUser(2);
        User friend1 = newUser(3);
        User friend2 = newUser(4);
        User friend3 = newUser(5);

        userService.create(user1);
        userService.create(user2);
        userService.create(friend1);
        userService.create(friend2);
        userService.create(friend3);

        userService.addToFriends(1, 3);
        userService.addToFriends(1, 4);
        userService.addToFriends(1, 5);
        userService.addToFriends(2, 3);
        userService.addToFriends(2, 5);

        List<User> friends = userService.findMutualFriends(1, 2);
        assertThat(friends.size()).isEqualTo(2);

        List<Integer> idFriend = new ArrayList<>();
        for (User user : friends) {
            idFriend.add(user.getId());
        }
        assertThat(idFriend.contains(3)).isTrue();
        assertThat(idFriend.contains(5)).isTrue();
    }

    private Film newFilm(Integer num) {
        Film film = new Film();
        film.setName("name" + num);
        film.setDescription("description" + num);
        film.setReleaseDate(LocalDate.of(2005, 12, 15));
        film.setDuration(150);
        film.setMpa(new MPA(1, "PG"));
        return film;
    }

    @Test
    public void testFindAllFilms() {
        filmService.create(newFilm(1));
        filmService.create(newFilm(2));
        filmService.create(newFilm(3));

        List<Film> films = filmService.findAll();
        assertThat(films.size()).isEqualTo(3);
    }

    @Test
    public void testFindFilmById() {
        filmService.create(newFilm(1));
        filmService.create(newFilm(2));
        filmService.create(newFilm(3));

        Film resultFilm = filmService.findById(3);

        assertThat(resultFilm.getId()).isEqualTo(3);
        assertThat(resultFilm.getName()).isEqualTo("name3");
    }

    @Test
    public void testUpdateFilm() {
        filmService.create(newFilm(1));
        filmService.create(newFilm(2));
        filmService.create(newFilm(3));

        Film upDateFilm = newFilm(4);
        upDateFilm.setId(2);

        filmService.upDate(upDateFilm);

        assertThat(upDateFilm.getName()).isEqualTo("name4");
        assertThat(upDateFilm.getDescription()).isEqualTo("description4");
        assertThat(upDateFilm.getLikes()).isEqualTo(0);
    }

    @Test
    public void testAddLikes() {
        filmService.create(newFilm(1));
        userService.create(newUser(1));
        userService.create(newUser(2));
        userService.create(newUser(3));

        filmService.addLike(1, 1);
        filmService.addLike(1, 2);
        filmService.addLike(1, 3);

        Film filmWithLikes = filmService.findById(1);
        User userWithLikes = userService.findById(2);

        assertThat(filmWithLikes.getLikes()).isEqualTo(3);
    }

    @Test
    public void testRemoveLike() {
        filmService.create(newFilm(1));
        userService.create(newUser(1));
        userService.create(newUser(2));
        userService.create(newUser(3));

        filmService.addLike(1, 1);
        filmService.addLike(1, 2);
        filmService.addLike(1, 3);

        filmService.removeLike(1, 2);

        Film filmWithoutLikes = filmService.findById(1);
        User userWithoutLikes = userService.findById(2);

        assertThat(filmWithoutLikes.getLikes()).isEqualTo(2);
    }

    @Test
    public void testFindTopFilms() {
        filmService.create(newFilm(1));
        filmService.create(newFilm(2));
        filmService.create(newFilm(3));
        filmService.create(newFilm(4));
        filmService.create(newFilm(5));

        userService.create(newUser(1));
        userService.create(newUser(2));
        userService.create(newUser(3));
        userService.create(newUser(4));
        userService.create(newUser(5));
        userService.create(newUser(6));

        filmService.addLike(1, 1);
        filmService.addLike(1, 2);
        filmService.addLike(1, 3);
        filmService.addLike(1, 4);
        filmService.addLike(1, 5);
        filmService.addLike(1, 6);

        filmService.addLike(4, 1);
        filmService.addLike(4, 2);
        filmService.addLike(4, 3);
        filmService.addLike(4, 4);
        filmService.addLike(4, 5);

        List<Film> top = filmService.findTopFilms(2);
        assertThat(top.size()).isEqualTo(2);

        ArrayList<Integer> idTop = new ArrayList<>();
        for (Film film : top) {
            idTop.add(film.getId());
        }

        assertThat(idTop.contains(1)).isTrue();
        assertThat(idTop.contains(4)).isTrue();
    }

    @Test
    public void testGetAllGenres() {
        List<Genres> genres = genreDao.getAll();

        ArrayList<Integer> idGenres = new ArrayList<>();
        ArrayList<String> namesGenres = new ArrayList<>();
        for (Genres genre : genres) {
            idGenres.add(genre.getId());
            namesGenres.add(genre.getName());
        }
        assertThat(idGenres.size()).isEqualTo(6);
        assertThat(idGenres.contains(1)).isTrue();
        assertThat(idGenres.contains(6)).isTrue();
        assertThat(idGenres.contains(7)).isFalse();
        assertThat(namesGenres.contains("Комедия")).isTrue();
        assertThat(namesGenres.contains("Триллер")).isTrue();
        assertThat(namesGenres.contains("Пуп")).isFalse();
    }

    @Test
    public void testGetGenreById() {
        Genres genre = genreDao.getById(3);

        assertThat(genre.getId()).isEqualTo(3);
        assertThat(genre.getName()).isEqualTo("Мультфильм");
    }

    @Test
    public void testGetAllMPA() {
        List<MPA> mpa = mpaDao.getAll();

        ArrayList<Integer> idMpa = new ArrayList<>();
        ArrayList<String> namesMpa = new ArrayList<>();
        for (MPA mp : mpa) {
            idMpa.add(mp.getId());
            namesMpa.add(mp.getName());
        }
        assertThat(idMpa.size()).isEqualTo(5);
        assertThat(idMpa.contains(1)).isTrue();
        assertThat(idMpa.contains(5)).isTrue();
        assertThat(idMpa.contains(6)).isFalse();
        assertThat(namesMpa.contains("PG")).isTrue();
        assertThat(namesMpa.contains("G")).isTrue();
        assertThat(namesMpa.contains("PGG")).isFalse();
    }

    @Test
    public void testGetMPAById() {
        MPA mpa = mpaDao.getById(1);

        assertThat(mpa.getId()).isEqualTo(1);
        assertThat(mpa.getName()).isEqualTo("G");
    }
}
