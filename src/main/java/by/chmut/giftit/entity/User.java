package by.chmut.giftit.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The User class provides encapsulation of User data in the system.
 *
 * @author Sergei Chmut.
 */
public class User extends Entity {

    /**
     * The User id.
     */
    private long userId;
    /**
     * The Username.
     */
    private String username;
    /**
     * The Password.
     */
    private String password;
    /**
     * The First name.
     */
    private String firstName;
    /**
     * The Last name.
     */
    private String lastName;
    /**
     * The Email.
     */
    private String email;
    /**
     * The Phone.
     */
    private String phone;
    /**
     * The Address.
     */
    private String address;
    /**
     * The Account contains the amount of money earned
     * by the Designer and possible to withdraw.
     */
    private BigDecimal account;
    /**
     * The Init date is the date when User was created.
     */
    private LocalDate initDate;
    /**
     * The Blocked until - date until which the user will be blocked.
     */
    private LocalDate blockedUntil;
    /**
     * The User Role.
     */
    private Role role;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getAccount() {
        return account;
    }

    public void setAccount(BigDecimal account) {
        this.account = account;
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }

    public LocalDate getBlockedUntil() {
        return blockedUntil;
    }

    public void setBlockedUntil(LocalDate blockedUntil) {
        this.blockedUntil = blockedUntil;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Compares this user to the specified object.
     * The result is {@code true} if and only if the argument is not {@code null} and is a
     * {@code User} object that represents the same fields as this
     * object.
     *
     * @param object the object to compare
     * @return {@code true} if the given object represents a {@code User}
     * equivalent to this user, {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        User user = (User) object;
        if (userId != user.userId) return false;
        if (username == null) {
            if (user.username != null) {
                return false;
            }
        } else if (!username.equals(user.username)) {
            return false;
        }
        if (password == null) {
            if (user.password != null) {
                return false;
            }
        } else if (!password.equals(user.password)) {
            return false;
        }
        if (firstName == null) {
            if (user.firstName != null) {
                return false;
            }
        } else if (!firstName.equals(user.firstName)) {
            return false;
        }
        if (lastName == null) {
            if (user.lastName != null) {
                return false;
            }
        } else if (!lastName.equals(user.lastName)) {
            return false;
        }
        if (email == null) {
            if (user.email != null) {
                return false;
            }
        } else if (!email.equals(user.email)) {
            return false;
        }
        if (phone == null) {
            if (user.phone != null) {
                return false;
            }
        } else if (!phone.equals(user.phone)) {
            return false;
        }
        if (address == null) {
            if (user.address != null) {
                return false;
            }
        } else if (!address.equals(user.address)) {
            return false;
        }
        if (account == null) {
            if (user.account != null) {
                return false;
            }
        } else if (!account.equals(user.account)) {
            return false;
        }
        if (initDate == null) {
            if (user.initDate != null) {
                return false;
            }
        } else if (!initDate.equals(user.initDate)) {
            return false;
        }
        if (blockedUntil == null) {
            if (user.blockedUntil != null) {
                return false;
            }
        } else if (!blockedUntil.equals(user.blockedUntil)) {
            return false;
        }
        return role == user.role;
    }

    /**
     * Returns a hash code for this user.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int result = 31;
        result = result * (int) (userId - (userId >>> 32));
        result = result * 31 + (username != null ? username.hashCode() * result : 0);
        result = result * 31 + (password != null ? password.hashCode() * result : 0);
        result = result * 31 + (firstName != null ? firstName.hashCode() * result : 0);
        result = result * 31 + (lastName != null ? lastName.hashCode() * result : 0);
        result = result * 31 + (email != null ? email.hashCode() * result : 0);
        result = result * 31 + (phone != null ? phone.hashCode() * result : 0);
        result = result * 31 + (address != null ? address.hashCode() * result : 0);
        result = result * 31 + (account != null ? account.hashCode() * result : 0);
        result = result * 31 + (initDate != null ? initDate.hashCode() * result : 0);
        result = result * 31 + (blockedUntil != null ? blockedUntil.hashCode() * result : 0);
        result = result * 31 + (role != null ? role.hashCode() * result : 0);
        return result;
    }

    /**
     * Returns user representation as a string
     *
     * @return the string which contains values of user's fields
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", account=" + account +
                ", initDate=" + initDate +
                ", blockedUntil=" + blockedUntil +
                ", role=" + role +
                '}';
    }

    /**
     * The enum Role sets possible roles for the user.
     *
     * @author Sergei Chmut.
     */
    public enum Role {
        /**
         * Admin role.
         */
        ADMIN,
        /**
         * Moderator role.
         */
        MODERATOR,
        /**
         * Designer role.
         */
        DESIGNER,
        /**
         * User role.
         */
        USER,
        /**
         * Guest role.
         */
        GUEST
    }
}
