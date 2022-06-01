package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TicketCancellationEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private JwtTokenizer jwtTokenizer;
    @Autowired
    ShowRepository showRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void post_shouldCancelAvailableTickets() throws Exception {
        TicketStatusDto ticketStatusDto = new TicketStatusDto();
        ticketStatusDto.setPurchased(List.of(1L, 2L));
        ticketStatusDto.setReserved(List.of());

        String body = objectMapper.writeValueAsString(ticketStatusDto);

        MvcResult mvcResult = this.mockMvc.perform(
            post(CANCELLATION_BASE_URI).header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void post_shouldDeReserveAvailableTickets() throws Exception {
        TicketStatusDto ticketStatusDto = new TicketStatusDto();
        ticketStatusDto.setPurchased(List.of());
        ticketStatusDto.setReserved(List.of(1L, 2L));

        String body = objectMapper.writeValueAsString(ticketStatusDto);

        MvcResult mvcResult = this.mockMvc.perform(
            post(CANCELLATION_BASE_URI).header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void post_shouldReturn422_whenInputWithoutTicket() throws Exception{
        TicketStatusDto ticketStatusDto = new TicketStatusDto();
        ticketStatusDto.setPurchased(List.of());
        ticketStatusDto.setReserved(List.of());

        String body = objectMapper.writeValueAsString(ticketStatusDto);

        MvcResult mvcResult = this.mockMvc.perform(
            post(CANCELLATION_BASE_URI).header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    void post_shouldReturn422_whenInputWithReservedAndPurchasedTicket() throws Exception{
        TicketStatusDto ticketStatusDto = new TicketStatusDto();
        ticketStatusDto.setPurchased(List.of(1L));
        ticketStatusDto.setReserved(List.of(1L));

        String body = objectMapper.writeValueAsString(ticketStatusDto);

        MvcResult mvcResult = this.mockMvc.perform(
            post(CANCELLATION_BASE_URI).header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    void post_shouldReturn403_whenLoggedOut() throws Exception{
        TicketStatusDto ticketStatusDto = new TicketStatusDto();
        ticketStatusDto.setPurchased(List.of());
        ticketStatusDto.setReserved(List.of(1L));

        String body = objectMapper.writeValueAsString(ticketStatusDto);

        MvcResult mvcResult = this.mockMvc.perform(
            post(CANCELLATION_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }
}