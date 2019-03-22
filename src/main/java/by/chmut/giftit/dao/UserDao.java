package by.chmut.giftit.dao;

import by.chmut.giftit.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The interface User dao.
 *
 * @author Sergei Chmut.
 */
public interface UserDao extends Dao<Long, User> {

    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     * @throws DaoException the dao exception
     */
    Optional<User> findByUsername(String username) throws DaoException;

    /**
     * Find by part of username list.
     *
     * @param username the username
     * @return the list
     * @throws DaoException the dao exception
     */
    List<User> findByPartOfUsername(String username) throws DaoException;

    /**
     * Find by init date list.
     *
     * @param initDate the init date
     * @return the list
     * @throws DaoException the dao exception
     */
    List<User> findByInitDate(LocalDate initDate) throws DaoException;

    /**
     * Block for days boolean.
     *
     * @param userId           the user id
     * @param countDayForBlock the count day for block
     * @return the boolean
     * @throws DaoException the dao exception
     */
    boolean blockForDays(long userId, int countDayForBlock) throws DaoException;
}
