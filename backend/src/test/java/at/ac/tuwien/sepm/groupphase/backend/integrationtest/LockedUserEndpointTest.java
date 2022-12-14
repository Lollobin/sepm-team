package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADDRESS2_ENTITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADDRESS3_ENTITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADDRESS4_ENTITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADDRESS_ENTITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_ROLES;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_USER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTICLES_BASE_URI;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.DEFAULT_USER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER2_EMAIL;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER2_FNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER2_GENDER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER2_LNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER2_PASSWORD;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER3_EMAIL;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER3_FNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER3_GENDER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER3_LNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER3_PASSWORD;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER4_EMAIL;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_EMAIL;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_FNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_GENDER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_LNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_PASSWORD;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_ROLES;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UsersPageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class LockedUserEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();

    }

    @Test
    void shouldGetAllLockedUsers() throws Exception {

        saveFourUsers();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/users?filterLocked=true")
            .header(securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .accept(MediaType.APPLICATION_JSON)).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        UsersPageDto resultDto = objectMapper.readValue(response.getContentAsString(),
            UsersPageDto.class);
        List<UserDto> userDtos = resultDto.getUsers();

        assertThat(userDtos).asList().hasSize(3);
        assertThat(userDtos.get(0).getLockedAccount()).isTrue();
        assertThat(userDtos.get(0).getEmail()).isEqualTo(USER2_EMAIL);
        assertThat(userDtos.get(0).getLastName()).isEqualTo(USER2_LNAME);

        assertThat(userDtos.get(1).getLockedAccount()).isTrue();
        assertThat(userDtos.get(1).getEmail()).isEqualTo(USER_EMAIL);
        assertThat(userDtos.get(1).getFirstName()).isEqualTo(USER_FNAME);

        userRepository.deleteAll();

    }

    @Test
    void shouldReturn403DueToInvalidRoleToGetUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users?filterLocked=true")
            .header(securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }


    @Test
    void shouldChangeLockedStateToTrue() throws Exception {

        saveFourUsers();

        ApplicationUser beforeChange = userRepository.findUserByEmail(USER_EMAIL);

        assertThat(beforeChange.isLockedAccount()).isTrue();

        String json = objectMapper.writeValueAsString(false);

        mockMvc.perform(put("/lockStatus/{id}", beforeChange.getUserId())
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        ApplicationUser afterChange = userRepository.findUserByEmail(USER_EMAIL);

        assertThat(afterChange.isLockedAccount()).isFalse();
        assertThat(afterChange.getUserId()).isEqualTo(beforeChange.getUserId());
        assertThat(afterChange.getEmail()).isEqualTo(beforeChange.getEmail());

        userRepository.deleteAll();
    }

    @Test
    void shouldReturn403DueToInvalidRoleToChangeLocked() throws Exception {

        saveFourUsers();

        ApplicationUser beforeChange = userRepository.findUserByEmail(USER_EMAIL);

        assertThat(beforeChange.isLockedAccount()).isTrue();

        String json = objectMapper.writeValueAsString(false);

        mockMvc.perform(put("/lockStatus/{id}", beforeChange.getUserId())
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());

        ApplicationUser afterChange = userRepository.findUserByEmail(USER_EMAIL);

        assertThat(afterChange.getEmail()).isEqualTo(beforeChange.getEmail());
        assertThat(afterChange.getUserId()).isEqualTo(beforeChange.getUserId());

        assertThat(afterChange.isLockedAccount()).isTrue();

        userRepository.deleteAll();
    }

    @Test
    void put_shouldLockUser() throws Exception {

        ApplicationUser user1 = new ApplicationUser();
        user1.setLockedAccount(false);
        user1.setFirstName(USER_FNAME);
        user1.setLastName(USER_LNAME);
        user1.setGender(USER_GENDER);
        user1.setEmail(USER_EMAIL);

        ADDRESS_ENTITY.setAddressId(null);
        user1.setAddress(ADDRESS_ENTITY);
        user1.setPassword(USER_PASSWORD);
        user1.setHasAdministrativeRights(false);
        user1.setLoginTries(0);
        user1.setMustResetPassword(false);

        userRepository.save(user1);

        ApplicationUser beforeChange = userRepository.findUserByEmail(USER_EMAIL);

        assertThat(beforeChange.isLockedAccount()).isFalse();

        String json = objectMapper.writeValueAsString(true);

        mockMvc.perform(put("/lockStatus/{id}", beforeChange.getUserId())
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)).content(json)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        ApplicationUser afterChange = userRepository.findUserByEmail(USER_EMAIL);

        assertThat(afterChange.getEmail()).isEqualTo(beforeChange.getEmail());
        assertThat(afterChange.getUserId()).isEqualTo(beforeChange.getUserId());

        assertThat(afterChange.isLockedAccount()).isTrue();

        userRepository.deleteAll();

    }

    @Test
    void put_shouldNotLockAdmin() throws Exception {

        saveFourUsers();

        ApplicationUser beforeChange = userRepository.findUserByEmail(USER4_EMAIL);

        assertThat(beforeChange.isLockedAccount()).isFalse();

        String json = objectMapper.writeValueAsString(true);
        MvcResult mvcResult = this.mockMvc
            .perform(
                put("/lockStatus/{id}", beforeChange.getUserId())

                    .header(
                        securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)).content(json)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))

            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());




    }


    @Test
    void shouldSet404WhenUserNotPresent() throws Exception {

        String json = objectMapper.writeValueAsString(false);
        ResultActions resultAction = mockMvc.perform(put("/lockStatus/-100")
            .header(securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)).content(json)
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        resultAction.andExpect(status().isNotFound());

    }


    private void saveFourUsers() {

        ApplicationUser user1 = new ApplicationUser();
        user1.setLockedAccount(true);
        user1.setFirstName(USER_FNAME);
        user1.setLastName(USER_LNAME);
        user1.setGender(USER_GENDER);
        user1.setEmail(USER_EMAIL);

        ADDRESS_ENTITY.setAddressId(null);
        user1.setAddress(ADDRESS_ENTITY);
        user1.setPassword(USER_PASSWORD);
        user1.setHasAdministrativeRights(true);
        user1.setLoginTries(0);
        user1.setMustResetPassword(false);

        userRepository.save(user1);

        ADDRESS2_ENTITY.setAddressId(null);
        ApplicationUser user2 = new ApplicationUser();
        user2.setLockedAccount(true);
        user2.setFirstName(USER2_FNAME);
        user2.setLastName(USER2_LNAME);
        user2.setGender(USER2_GENDER);
        user2.setEmail(USER2_EMAIL);
        user2.setAddress(ADDRESS2_ENTITY);
        user2.setPassword(USER2_PASSWORD);
        user2.setHasAdministrativeRights(true);
        user2.setLoginTries(0);
        user2.setMustResetPassword(false);

        userRepository.save(user2);

        ADDRESS3_ENTITY.setAddressId(null);
        ApplicationUser user3 = new ApplicationUser();
        user3.setLockedAccount(true);
        user3.setFirstName(USER3_FNAME);
        user3.setLastName(USER3_LNAME);
        user3.setGender(USER3_GENDER);
        user3.setEmail(USER3_EMAIL);
        user3.setAddress(ADDRESS3_ENTITY);
        user3.setPassword(USER3_PASSWORD);
        user3.setHasAdministrativeRights(true);
        user3.setLoginTries(0);
        user3.setMustResetPassword(false);

        userRepository.save(user3);

        ADDRESS4_ENTITY.setAddressId(null);
        ApplicationUser user4 = new ApplicationUser();
        user4.setLockedAccount(false);
        user4.setFirstName("nicht");
        user4.setLastName("anzeigen");
        user4.setGender(USER3_GENDER);
        user4.setEmail("nicht@anzeigen.com");
        user4.setAddress(ADDRESS4_ENTITY);
        user4.setPassword(USER3_PASSWORD);
        user4.setHasAdministrativeRights(true);
        user4.setLoginTries(0);
        user4.setMustResetPassword(false);

        userRepository.save(user4);


    }


}
