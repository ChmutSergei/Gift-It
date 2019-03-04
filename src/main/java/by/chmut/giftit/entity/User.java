package by.chmut.giftit.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class User extends Entity {

    private long userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private BigDecimal account;
    private LocalDate initDate;
    private LocalDate blockedUntil;
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

    public enum Role {
        ADMIN, MODERATOR, DESIGNER, USER, GUEST
    }
}
