package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.github.javafaker.Faker;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class UserGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final AddressDataGenerator addressDataGenerator;
    private final UserRepository userRepository;
    private final Faker faker = new Faker();

    public UserGenerator(
        AddressDataGenerator addressDataGenerator,
        UserRepository userRepository) {
        this.addressDataGenerator = addressDataGenerator;
        this.userRepository = userRepository;
    }

    public void generateData(int numberOfUsers) {

        if (!userRepository.findAll().isEmpty()) {
            LOGGER.debug("users already generated");
            return;
        }

        LOGGER.debug("generating {} users", numberOfUsers);

        ApplicationUser admin = generateApplicationUser();
        admin.setEmail("admin@email.com");
        admin.setPassword("password");
        admin.setHasAdministrativeRights(true);
        admin.setLoginTries(0);
        admin.setMustResetPassword(false);
        admin.setLockedAccount(false);
        userRepository.save(admin);

        ApplicationUser user = generateApplicationUser();
        user.setEmail("user@email.com");
        user.setPassword("password");
        user.setHasAdministrativeRights(false);
        user.setLoginTries(0);
        user.setMustResetPassword(false);
        user.setLockedAccount(false);
        userRepository.save(user);

        for (int i = 0; i < numberOfUsers; i++) {
            ApplicationUser randomUser = generateApplicationUser();
            LOGGER.trace("saving user {}", randomUser);
            userRepository.save(randomUser);
        }
    }

    private ApplicationUser generateApplicationUser() {
        ApplicationUser user = new ApplicationUser();
        user.setEmail(faker.internet().emailAddress());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setGender(faker.options().option(Gender.class));
        user.setAddress(addressDataGenerator.generateRandomAddress());
        user.setPassword(faker.internet().password(10, 30));
        user.setHasAdministrativeRights(false);
        user.setLoginTries(0);
        user.setMustResetPassword(false);
        double randomDouble = faker.number().randomDouble(3, 0, 1);
        user.setLockedAccount(randomDouble < 0.1);
        return user;
    }
}