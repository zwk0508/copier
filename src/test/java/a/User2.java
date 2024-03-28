package a;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/28 19:42
 */

public class User2 {
    private String userName;
    private Integer userAge;
    private Integer userSex;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public Integer getUserSex() {
        return userSex;
    }

    public void setUserSex(Integer userSex) {
        this.userSex = userSex;
    }

    @Override
    public String toString() {
        return "User2{" +
                "userName='" + userName + '\'' +
                ", userAge=" + userAge +
                ", userSex=" + userSex +
                '}';
    }
}
