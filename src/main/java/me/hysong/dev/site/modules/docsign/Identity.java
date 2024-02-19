package me.hysong.dev.site.modules.docsign;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Getter;

@Getter
public class Identity {
    private String name;
    private String email;

    public Identity(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Identity) {
            Identity other = (Identity) obj;
            return this.email.equals(other.getEmail());
        }
        return false;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        obj.addProperty("email", email);
        return obj;
    }

    public static Identity parse(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        return new Identity(
                obj.get("name").getAsString(),
                obj.get("email").getAsString()
        );
    }
}
