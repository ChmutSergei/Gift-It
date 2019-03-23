package by.chmut.giftit.dao;

import by.chmut.giftit.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The interface User dao implements the access mechanism
 * required to work with the data source specifically for User entity.
 *
 * @author Sergei Chmut.
 */
public interface UserDao extends Dao<Long, User> {

    /**
     * Find user by username.
     *
     * @param username the username for search
     * @return the optional user
     * @throws DaoException if find user by username can't be handled
     */
    Optional<User> findByUsername(String username) throws DaoException;

    /**
     * Find all users with such part of username.
     *
     * @param partOfUsername the part of username for search
     * @return the list of users
     * @throws DaoException if find user by part of username can't be handled
     */
    List<User> findByPartOfUsername(String partOfUsername) throws DaoException;

    /**
     * Find all users with such init date.
     *
     * @param initDate the date when user init
     * @return the list of users
     * @throws DaoException if find user by init date can't be handled
     */
    List<User> findByInitDate(LocalDate initDate) throws DaoException;

    /**
     * Block user with such id for a given number of days.
     *
     * @param userId           the user id
     * @param countDayForBlock the count day for block
     * @return true if set block otherwise false
     * @throws DaoException if block user can't be handled
     */
    boolean blockForDays(long userId, int countDayForBlock) throws DaoException;
}
