/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

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
@Tag(name = "readArticleStatus", description = "the readArticleStatus API")
public interface ReadArticleStatusApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * PUT /readArticleStatus/{id} : Sets the status to read or unread for an article for the current user
     *
     * @param id ID of the user that is retreived (required)
     * @param body  (required)
     * @return Successful set the \&quot;read\&quot;-status of an article for an user (status code 200)
     *         or The user is not logged in (status code 401)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "readArticleStatusIdPut",
        summary = "Sets the status to read or unread for an article for the current user",
        tags = { "articles" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful set the \"read\"-status of an article for an user"),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.PUT,
        value = "/readArticleStatus/{id}",
        consumes = { "application/json" }
    )
    default ResponseEntity<Void> readArticleStatusIdPut(
        @Parameter(name = "id", description = "ID of the user that is retreived", required = true, schema = @Schema(description = "")) @PathVariable("id") Integer id,
        @Parameter(name = "body", description = "", required = true, schema = @Schema(description = "")) @Valid @RequestBody Boolean body
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}