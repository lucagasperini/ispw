package com.pickyeaters.logic;

import com.pickyeaters.logic.model.Pickie;
import com.pickyeaters.logic.view.Application;
import com.pickyeaters.logic.view.FindRestaurantView;
import com.pickyeaters.logic.view.LoginView;
import com.pickyeaters.logic.view.eatingpreference.EditEatingPreferenceView;
import com.pickyeaters.logic.view.eatingpreference.ShowEatingPreferenceView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

class LoginFSTest {
    private Application app;
    @BeforeEach
    void setUp() {
        app = new Application(new String[]{"provider=fs"});
        app.systemStart();
        Pickie pickie = new Pickie(
                "1",
                "lucaP",
                "d70f47790f689414789eeff231703429c7f88a10210775906460edbf38589d90",
                "Luca",
                "Bianchi"
        );
        String homeDir = System.getProperty("user.home");
        Path dataDir = Paths.get(homeDir);
        Path targetFile = dataDir.resolve("1.user");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(targetFile.toFile()))) {
            oos.writeObject(pickie);
        } catch (IOException e) {
            throw new AssertionFailedError();
        }
    }

    @Test
    void login() {
        LoginView loginView = app.displayLoginView();
        loginView.insertEmail("lucaP");
        loginView.insertPassword("luca");
        app.login();
    }
}
