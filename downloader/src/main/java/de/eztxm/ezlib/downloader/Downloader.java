package de.eztxm.ezlib.downloader;

import de.eztxm.ezlib.api.http.Authentication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class Downloader {
    private final String url;
    private final Path saveDirectory;
    private Authentication authentication;

    public Downloader(String url, Path saveDirectory) {
        this.url = url;
        this.saveDirectory = saveDirectory;
    }

    public Downloader(String url, Path saveDirectory, Authentication authentication) {
        this.url = url;
        this.saveDirectory = saveDirectory;
        this.authentication = authentication;
    }

    public File downloadFile() {
        HttpURLConnection connection = null;
        try {
            URL downloadUrl = URI.create(url).toURL();
            connection = (HttpURLConnection) downloadUrl.openConnection();
            connection.setRequestMethod("GET");

            // Handle authentication
            if (authentication != null && authentication instanceof BasicAuthentication basicAuth) {
                String auth = basicAuth.getUsername() + ":" + basicAuth.getPassword();
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
                connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
                connection.setRequestProperty("Content-Type", "application/json");
            }

            int responseCode = connection.getResponseCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK -> {
                    String fileName = extractFileName(connection);
                    if (fileName.isEmpty()) {
                        fileName = downloadUrl.getPath().substring(downloadUrl.getPath().lastIndexOf("/") + 1);
                    }

                    Path saveFilePath = saveDirectory.resolve(fileName);

                    // Ensure directory exists
                    if (Files.notExists(saveDirectory)) {
                        Files.createDirectories(saveDirectory);
                    }

                    try (InputStream inputStream = connection.getInputStream()) {
                        Files.copy(inputStream, saveFilePath);
                    }

                    System.out.println("File downloaded to " + saveFilePath);
                    return saveFilePath.toFile();
                }
                case HttpURLConnection.HTTP_FORBIDDEN -> System.out.println("Access denied (403)");
                case HttpURLConnection.HTTP_UNAUTHORIZED -> System.out.println("Unauthorized (401)");
                case HttpURLConnection.HTTP_NOT_FOUND -> System.out.println("Not found (404)");
                default -> System.out.println("Unexpected response code: " + responseCode);
            }

        } catch (IOException e) {
            System.err.println("Download failed: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    private String extractFileName(HttpURLConnection connection) {
        String disposition = connection.getHeaderField("Content-Disposition");
        String filenameStar = null;
        String filename = null;

        if (disposition != null) {
            String[] parts = disposition.split(";");
            for (String part : parts) {
                part = part.trim();
                String trimmed = part.substring(part.indexOf('=') + 1).trim();
                if (part.startsWith("filename*=")) {
                    filenameStar = trimmed;
                    int encodingIdx = filenameStar.indexOf("''");
                    if (encodingIdx != -1) {
                        filenameStar = filenameStar.substring(encodingIdx + 2);
                    }
                    filenameStar = filenameStar.replaceAll("\"", "");
                }
                if (part.startsWith("filename=")) {
                    filename = trimmed.replaceAll("\"", "");
                }
            }
        }

        if (filenameStar != null && !filenameStar.isEmpty()) {
            return filenameStar;
        }
        if (filename != null && !filename.isEmpty()) {
            return filename;
        }

        return "";
    }
}
