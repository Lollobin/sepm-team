/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

import java.net.URI;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UsersPageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
@Validated
@Tag(name = "users", description = "the users API")
public interface UsersApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * DELETE /users : Deletes the user posessing the token.
     * Only the user itself can delete a given user account.
     *
     * @return Successful deletion of a user. (status code 204)
     *         or The user is not logged in (status code 401)
     *         or The user with the given ID was not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "usersDelete",
        summary = "Deletes the user posessing the token.",
        tags = { "userManagement" },
        responses = {
            @ApiResponse(responseCode = "204", description = "Successful deletion of a user."),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "404", description = "The user with the given ID was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.DELETE,
        value = "/users"
    )
    default ResponseEntity<Void> usersDelete(
        
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /users : Gets a list of users
     *
     * @param filterLocked Only return locked users if this is set to true (optional)
     * @param pageSize Number of items on requested page (optional, default to 10)
     * @param requestedPage Index of requested page (starts with 0) (optional, default to 0)
     * @param sort  (optional, default to ASC)
     * @return OK (status code 200)
     */
    @Operation(
        operationId = "usersGet",
        summary = "Gets a list of users",
        tags = { "userManagement" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  UsersPageDto.class)))
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/users",
        produces = { "application/json" }
    )
    default ResponseEntity<UsersPageDto> usersGet(
        @Parameter(name = "filterLocked", description = "Only return locked users if this is set to true", schema = @Schema(description = "")) @Valid @RequestParam(value = "filterLocked", required = false) Boolean filterLocked,
        @Parameter(name = "pageSize", description = "Number of items on requested page", schema = @Schema(description = "", defaultValue = "10")) @Valid @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
        @Parameter(name = "requestedPage", description = "Index of requested page (starts with 0)", schema = @Schema(description = "", defaultValue = "0")) @Valid @RequestParam(value = "requestedPage", required = false, defaultValue = "0") Integer requestedPage,
        @Parameter(name = "sort", description = "", schema = @Schema(description = "", allowableValues = { "ASC", "DESC" }, defaultValue = "ASC")) @Valid @RequestParam(value = "sort", required = false, defaultValue = "ASC") String sort
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"currentPage\" : 6, \"users\" : [ { \"firstName\" : \"firstName\", \"lastName\" : \"lastName\", \"lockedAccount\" : true, \"address\" : { \"country\" : \"country\", \"zipCode\" : \"zipCode\", \"city\" : \"city\", \"street\" : \"street\", \"houseNumber\" : \"houseNumber\" }, \"userId\" : 0, \"email\" : \"email\" }, { \"firstName\" : \"firstName\", \"lastName\" : \"lastName\", \"lockedAccount\" : true, \"address\" : { \"country\" : \"country\", \"zipCode\" : \"zipCode\", \"city\" : \"city\", \"street\" : \"street\", \"houseNumber\" : \"houseNumber\" }, \"userId\" : 0, \"email\" : \"email\" } ], \"numberOfResults\" : 1, \"pagesTotal\" : 5 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /users/{id} : Retreives information of an user with the given data.
     *
     * @param id ID of the user that is retreived (required)
     * @return Successful retreival of an user. (status code 200)
     *         or The user is not logged in (status code 401)
     *         or The user with the given ID was not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "usersIdGet",
        summary = "Retreives information of an user with the given data.",
        tags = { "userManagement" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of an user.", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  UserDto.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "404", description = "The user with the given ID was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/users/{id}",
        produces = { "application/json" }
    )
    default ResponseEntity<UserDto> usersIdGet(
        @Parameter(name = "id", description = "ID of the user that is retreived", required = true, schema = @Schema(description = "")) @PathVariable("id") Long id
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"firstName\" : \"firstName\", \"lastName\" : \"lastName\", \"lockedAccount\" : true, \"address\" : { \"country\" : \"country\", \"zipCode\" : \"zipCode\", \"city\" : \"city\", \"street\" : \"street\", \"houseNumber\" : \"houseNumber\" }, \"userId\" : 0, \"email\" : \"email\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /users : Registers an user with the given data.
     *
     * @param userWithPasswordDto  (required)
     * @return Successful creation of an user (status code 201)
     *         or Validation failed for an input (status code 422)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "usersPost",
        summary = "Registers an user with the given data.",
        tags = { "userManagement" },
        responses = {
            @ApiResponse(responseCode = "201", description = "Successful creation of an user"),
            @ApiResponse(responseCode = "422", description = "Validation failed for an input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/users",
        consumes = { "application/json" }
    )
    default ResponseEntity<Void> usersPost(
        @Parameter(name = "UserWithPasswordDto", description = "", required = true, schema = @Schema(description = "")) @Valid @RequestBody UserWithPasswordDto userWithPasswordDto
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * PUT /users : Updates information of a user possesing the token.
     *
     * @param userWithPasswordDto  (required)
     * @return Successful update of an user. (status code 204)
     *         or The user is not logged in (status code 401)
     *         or Validation failed for an input (status code 422)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "usersPut",
        summary = "Updates information of a user possesing the token.",
        tags = { "userManagement" },
        responses = {
            @ApiResponse(responseCode = "204", description = "Successful update of an user."),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "422", description = "Validation failed for an input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = "/users",
        consumes = { "application/json" }
    )
    default ResponseEntity<Void> usersPut(
        @Parameter(name = "UserWithPasswordDto", description = "", required = true, schema = @Schema(description = "")) @Valid @RequestBody UserWithPasswordDto userWithPasswordDto
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
