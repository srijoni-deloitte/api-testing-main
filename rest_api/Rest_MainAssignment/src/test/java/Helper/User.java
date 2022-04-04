package Helper;

import org.json.JSONObject;

public class User {
    public String name, email, password;
    public int age;

    public User(String name, String email, String password, int age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public JSONObject getFullUserJson() {
        JSONObject res = new JSONObject();
        res.put("name", this.name);
        res.put("email", this.email);
        res.put("password", this.password);
        res.put("age", this.age);
        return res;
    }

    public JSONObject getLoginJson() {
        JSONObject res = new JSONObject();
        res.put("email", this.email);
        res.put("password", this.password);
        return res;
    }

    @Override
    public String toString() {
        return this.getFullUserJson().toString();
    }
}
