package me.hysong.dev.site.modules.docsign;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.hysong.libhycore.CoreAES;
import me.hysong.libhycore.CoreBase64;
import lombok.Getter;
import lombok.Setter;
import me.hysong.libhycore.CoreSHA;

@Getter
public class Sign {

    public static final String HEADER = "<SIGHEAD>";
    public static final String FOOTER = "<SIGFOOT>";
    public static final String VERSION = "1.0";

    private String version;
    private String name;
    private String email;
    private long signedAt;
    private long validthrough;
    @Setter @Getter private String mininote = "";
    @Setter @Getter private String unsignedHash;
    
    @Getter @Setter private SignState signState;

    private void makeNullSafe() {
        if (this.version == null) this.version = "";
        if (this.name == null) this.name = "";
        if (this.email == null) this.email = "";
        if (this.mininote == null) this.mininote = "";
        if (this.unsignedHash == null) this.unsignedHash = "";
    }

    public Sign(String version, String name, String email, long signedAt, long validthrough, String mininote, String unsignedHash) throws Exception {
        this.version = version;
        this.name = name;
        this.email = email;
        this.signedAt = signedAt;
        this.validthrough = validthrough;
        this.mininote = mininote;
        this.unsignedHash = unsignedHash;
        makeNullSafe();
    }

    public Sign(Identity identity, int validityInDays) {
        this.name = identity.getName();
        this.email = identity.getEmail();
        this.signedAt = System.currentTimeMillis() / 1000;
        this.validthrough = this.signedAt + (validityInDays * 24 * 60 * 60);
        makeNullSafe();
    }

    public boolean equals(Object obj) {
        if (obj instanceof Sign) {
            Sign other = (Sign) obj;
            return this.unsignedHash.equals(other.unsignedHash);
        }
        return false;
    }

    public JsonObject toJson() {
        makeNullSafe();
        JsonObject obj = new JsonObject();
        obj.addProperty("version", VERSION);
        obj.addProperty("name", name);
        obj.addProperty("email", email);
        obj.addProperty("signedAt", signedAt);
        obj.addProperty("validthrough", validthrough);
        obj.addProperty("mininote", mininote);
        obj.addProperty("unsignedHash", unsignedHash);
        return obj;
    }

    public String toString() {
        String s = "";
        try {
            s += CoreAES.encrypt(toJson().toString(), email);
        }catch(Exception e) {
            s = "null";
        }

        return HEADER + s + FOOTER;
    }

    public String toReadableString() {
        makeNullSafe();
        String s = "Signature Information: ";
        s += "\nVersion: " + version;
        s += "\nName: " + name;
        s += "\nEmail: " + email;
        s += "\nSigned At: " + getSignedDate();
        s += "\nValid Through: " + getValidDate();
        s += "\nCurrently valid: " + ((System.currentTimeMillis() / 1000L < validthrough) ? "Yes" : "No");
        s += "\nMini note: " + mininote;
        s += "\nUnsigned Hash: " + unsignedHash;
        return s;
    }

    public String getValidDate() {
        return String.format("%tF", validthrough * 1000L);
    }

    public String getSignedDate() {
        return String.format("%tF", signedAt * 1000L);
    }

    public String getSign() {
        String s = toJson().toString();
        return CoreBase64.encode(s);
    }

    public String getSignHashed() {
        return CoreSHA.hash512(getSign());
    }

    public static Sign parse(String json) throws Exception {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        return new Sign(
                obj.get("version").getAsString(),
                obj.get("name").getAsString(),
                obj.get("email").getAsString(),
                obj.get("signedAt").getAsLong(),
                obj.get("validthrough").getAsLong(),
                obj.get("mininote").getAsString(),
                obj.get("unsignedHash").getAsString()
        );
    }
}
