package de.eztxm.ezlib.downloader;

import de.eztxm.ezlib.api.http.Authentication;
import lombok.Getter;

@Getter
public class BasicAuthentication implements Authentication {
    private final String username;
    private final String password;

    public BasicAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
