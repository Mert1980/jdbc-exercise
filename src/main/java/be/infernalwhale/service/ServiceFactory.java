package be.infernalwhale.service;

import be.infernalwhale.service.fake.MockBrewerService;
import be.infernalwhale.service.fake.MockCategoryService;
import be.infernalwhale.service.fake.MockConnectionManager;
import be.infernalwhale.service.fake.MockBeerService;
import be.infernalwhale.service.implementations.BeerServiceImpl;
import be.infernalwhale.service.implementations.BrewerServiceImpl;
import be.infernalwhale.service.implementations.CategoryServiceImpl;
import be.infernalwhale.service.implementations.ConnectionManagerImpl;

/**
 * This is the ServiceFactory. This class will provide the implementations for the different Service interfaces
 * as defined in the be.infernalwhale.service package to the view layer. The mock (or fake) implementations can
 * be found in the fake subpackage. You will need to write the actual implementations for these interfaces,
 * implementations that actually make use of a real database.
 *
 * You are allowed to add code in this class. The only thing you shouldn't do is change the method signatures
 * of the currently existing methods. You may add methods and properties as you see fit
 *
 * At the moment, this class is 100% static. We'll look into a better solution later in the course.
 */
public class ServiceFactory {

    private static ConnectionManager connectionManager;
    public static ConnectionManager createConnectionManager() {
        if (connectionManager == null) connectionManager = new ConnectionManagerImpl();
        return connectionManager;
    }

    public static CategoryService createCategoryService() {
        return new CategoryServiceImpl();
    }

    public static BrewersService createBrewersService() {
        return new BrewerServiceImpl();
    }

    public static BeerService createBeerService() {
        return new BeerServiceImpl();
    }
}
