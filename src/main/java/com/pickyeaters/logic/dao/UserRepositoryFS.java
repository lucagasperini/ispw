package com.pickyeaters.logic.dao;

import com.pickyeaters.logic.model.User;
import com.pickyeaters.logic.utils.Logger;

import java.io.*;
import java.nio.file.*;
import java.util.Optional;


public class UserRepositoryFS implements UserRepository {

    private final Path dataDir;
    private final Logger logger;

    public UserRepositoryFS(Logger logger, String basePath) throws IOException {
        this.logger = logger;
        this.dataDir = Paths.get(basePath);
        // check if dir exists first
        if (!Files.exists(this.dataDir)) {
            Files.createDirectories(this.dataDir);
        }
    }

    public Optional<User> getUserByEmail(String email) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataDir, "*.user")) {
            for (Path filePath : stream) {
                User user = deserializeUser(filePath);
                if (email.equals(user.getEmail())) {
                    return Optional.of(user);
                }
            }
        } catch (IOException e) {
            logger.error("Cannot read user directory", e);
        }
        return Optional.empty();
    }

    public Optional<User> getUserByID(String id) {
        try {
            Path targetFile = dataDir.resolve(id + ".user");
            if (Files.exists(targetFile)) {
                User user = deserializeUser(targetFile);
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (IOException e) {
            logger.error("Cannot read user file", e);
            return Optional.empty();
        }
    }

    public void editUser(User user) {
        Path targetFile = dataDir.resolve(user.getID() + ".user");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(targetFile.toFile()))) {
            oos.writeObject(user);
        } catch (IOException e) {
            logger.error("Cannot write user file", e);
        }
    }

    private User deserializeUser(Path filePath) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            return (User) ois.readObject();
        } catch (ClassNotFoundException e) {
            logger.error("Cannot find class for this user", e);
            throw new IOException(e);
        }
    }

}