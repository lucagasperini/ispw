package com.pickyeaters.logic.view;

import com.pickyeaters.logic.bean.SystemParameterBean;
import com.pickyeaters.logic.bean.request.Request;
import com.pickyeaters.logic.controller.*;
import com.pickyeaters.logic.dao.*;
import com.pickyeaters.logic.factory.DishFactory;
import com.pickyeaters.logic.factory.UserFactory;
import com.pickyeaters.logic.utils.FileLogger;
import com.pickyeaters.logic.utils.Logger;
import com.pickyeaters.logic.utils.Printer;
import com.pickyeaters.logic.utils.VoidLogger;
import com.pickyeaters.logic.view.dish.AddDishView;
import com.pickyeaters.logic.view.dish.ChangeDishView;
import com.pickyeaters.logic.view.dish.ShowDishView;
import com.pickyeaters.logic.view.eatingpreference.EditEatingPreferenceView;
import com.pickyeaters.logic.view.eatingpreference.ShowEatingPreferenceView;
import com.pickyeaters.logic.view.restaurant.EditRestaurantView;
import com.pickyeaters.logic.view.restaurant.ShowRestaurantView;

public class Application {
    private final SystemParameterBean systemParameter = new SystemParameterBean();

    private final Printer printer;
    private Logger logger;

    private final ConfigView configView;
    private final LocaleView localeView;

    private LoginView loginView;
    private Request baseRequest = null;

    private final DatabaseController databaseController;
    private LoginController loginController;
    private MenuController menuController;
    private RestaurantController restaurantController;
    private PickieController pickieController;
    private UserController userController;

    @SuppressWarnings("java:S1450")
    private IngredientRepository ingredientRepository;

    @SuppressWarnings("java:S1450")
    private MenuRepository menuRepository;

    @SuppressWarnings("java:S1450")
    private RestaurantRepository restaurantRepository;

    @SuppressWarnings("java:S1450")
    private UserRepository userRepository;

    @SuppressWarnings("java:S1450")
    private PickieRepository pickieRepository;

    @SuppressWarnings("java:S1450")
    private DishFactory dishFactory;

    @SuppressWarnings("java:S1450")
    private UserFactory userFactory;

    public Application(String[] args) {
        printer = new Printer();
        processSystemParameter(args);
        setupLogger();
        databaseController = new DatabaseController(logger);

        configView = new ConfigView(systemParameter, databaseController);
        localeView = new LocaleView(systemParameter);
    }

    private void setupLogger() {
        if(systemParameter.getLogFile().isEmpty()) {
            logger = new VoidLogger();
        } else {
            logger = new FileLogger(printer, systemParameter.getLogFile());
        }
    }

    private void processSystemParameter(String[] args) {
        for (String arg : args) {
            if (arg != null && arg.contains("=")) {
                String[] parts = arg.split("=", 2);
                String key = parts[0].trim();
                String value = parts[1].trim();

                systemParameter.setupFromKey(key, value);
            }
        }
    }

    public Printer getPrinter() {
        return printer;
    }

    public void systemStart() {
        parameterCheck();

        userFactory = new UserFactory(logger);
        dishFactory = new DishFactory(logger);

        if(configView.checkedProviderDatabase()) {
            logger.info("Selected data mode: database connection");
            ingredientRepository = new IngredientRepositoryDB(logger, databaseController);
            userRepository = new UserRepositoryDB(logger, databaseController, userFactory);
            menuRepository = new MenuRepositoryDB(logger, databaseController, ingredientRepository, dishFactory);
            restaurantRepository = new RestaurantRepositoryDB(logger, databaseController, userRepository);
            pickieRepository = new PickieRepositoryDB(logger, databaseController, ingredientRepository);
        } else {
            logger.info("Selected data mode: memory connection");
            ingredientRepository = new IngredientRepositoryRAM(logger);
            userRepository = new UserRepositoryRAM(logger);
            menuRepository = new MenuRepositoryRAM(logger, ingredientRepository);
            restaurantRepository = new RestaurantRepositoryRAM(logger, userRepository);
            pickieRepository = new PickieRepositoryRAM(logger, ingredientRepository);
        }

        loginController = new LoginController(logger, userRepository);
        menuController = new MenuController(
                logger, loginController, menuRepository, restaurantRepository,
                ingredientRepository, dishFactory
        );
        restaurantController = new RestaurantController(logger, loginController, restaurantRepository);
        pickieController = new PickieController(logger, loginController, pickieRepository,
                ingredientRepository, restaurantRepository, menuRepository);
        userController = new UserController(logger, loginController, userRepository);

        loginView = new LoginView(loginController);
    }


    private void parameterCheck() {
        localeView.loadLocale();

        if(configView.checkedProviderDatabase()) {
            final String configFile = systemParameter.getConfigFile();
            if (configFile != null && !configFile.trim().isEmpty()) {
                configView.loadConfigByFile(configFile);
            } else {
                configView.loadConfig();
            }
        }
    }
    public ConfigView displayConfigView() {
        return configView;
    }

    public LocaleView displayLocaleView() {
        return localeView;
    }

    public LoginView displayLoginView() {
        return loginView;
    }

    public void login() {
        loginView.login();
        baseRequest = new Request(loginView.getToken());
    }

    // Le view già create continuano a funzionare, quelle future no se non viene rifatto il login.
    public void logout() {
        loginView = new LoginView(loginController);
        baseRequest = null;
    }

    public MenuView displayMenuView() {
        return displayMenuView("");
    }

    public MenuView displayMenuView(String restaurantID) {
        loginView.throwIfUserNotLogged();
        return new MenuView(baseRequest, menuController, restaurantController, restaurantID);
    }

    public AddDishView displayAddDishView() {
        loginView.throwIfUserNotLogged();
        return new AddDishView(baseRequest, menuController);
    }


    public ChangeDishView displayChangeDishView(String dishID) {
        loginView.throwIfUserNotLogged();
        return new ChangeDishView(baseRequest, menuController, dishID);
    }


    public ShowDishView displayShowDishView(String dishID) {
        loginView.throwIfUserNotLogged();
        return new ShowDishView(baseRequest, menuController, dishID);
    }

    public ShowRestaurantView displayShowRestaurantView() {
        return displayShowRestaurantView("");
    }

    public ShowRestaurantView displayShowRestaurantView(String restaurantID) {
        loginView.throwIfUserNotLogged();
        return new ShowRestaurantView(baseRequest, restaurantController, restaurantID);
    }


    public EditRestaurantView displayEditRestaurantView() {
        loginView.throwIfUserNotLogged();
        return new EditRestaurantView(baseRequest, restaurantController, "");
    }

    public ShowEatingPreferenceView displayShowEatingPreferenceView() {
        loginView.throwIfUserNotLogged();
        return new ShowEatingPreferenceView(baseRequest, pickieController, menuController);
    }

    public EditEatingPreferenceView displayEditEatingPreferenceView() {
        loginView.throwIfUserNotLogged();
        return new EditEatingPreferenceView(baseRequest, pickieController, menuController);
    }

    public FindRestaurantView displayFindRestaurantView() {
        loginView.throwIfUserNotLogged();
        return new FindRestaurantView(baseRequest, pickieController);
    }

    public UserView displayUserView() {
        loginView.throwIfUserNotLogged();
        return new UserView(baseRequest, userController);
    }

}
