package com.on_class.bootcamp.infrastructure.entrypoints.documentation;

import com.on_class.bootcamp.domain.model.PaginationResponse;
import com.on_class.bootcamp.infrastructure.entrypoints.dto.BootcampRequestDto;
import com.on_class.bootcamp.infrastructure.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@RouterOperations({
        @RouterOperation(
                method = RequestMethod.POST,
                path = Constants.ROUTE_BOOTCAMP,
                operation = @Operation(
                        summary = "Create a new bootcamp",
                        description = "Registers a bootcamp with its relevant data.",
                        operationId = "createBootcamp",
                        tags = {"Bootcamp"},
                        requestBody = @RequestBody(
                                required = true,
                                content = @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = BootcampRequestDto.class)
                                )
                        ),
                        responses = {
                                @ApiResponse(
                                        responseCode = "201",
                                        description = "bootcamp created successfully"
                                ),
                                @ApiResponse(
                                        responseCode = "400",
                                        description = "Invalid request data"
                                ),
                                @ApiResponse(
                                        responseCode = "404",
                                        description = "Not all Capabilities found, please verify data "

                                ),
                                @ApiResponse(
                                        responseCode = "500",
                                        description = "Unexpected server error"
                                )
                        }
                )
        ),
        @RouterOperation(
                method = RequestMethod.GET,
                path = Constants.ROUTE_BOOTCAMP,
                operation = @Operation(
                        summary = "Get bootcamp list",
                        description = "Retrieve a paginated list of bootcamps",
                        operationId = "getBootcamps",
                        tags = {"Bootcamps"},
                        parameters = {
                                @Parameter(name = Constants.QUERY_PARAM_PAGE, description = "Page number", in = ParameterIn.QUERY, required = true),
                                @Parameter(name = Constants.QUERY_PARAM_SIZE, description = "Number of records per page", in = ParameterIn.QUERY, required = true),
                                @Parameter(name = Constants.QUERY_PARAM_SORT_DIRECTION, description = "Sort direction (ASC/DESC)", in = ParameterIn.QUERY, required = true),
                                @Parameter(name = Constants.QUERY_PARAM_SORT_FIELD, description = "Field to sort by", in = ParameterIn.QUERY, required = true)
                        },
                        responses = {
                                @ApiResponse(
                                        responseCode = "200",
                                        description = "Bootcamp retrieved successfully",
                                        content = @Content(
                                                mediaType = "application/json",
                                                schema = @Schema(implementation = PaginationResponse.class)
                                        )
                                ),
                                @ApiResponse(responseCode = "400", description = "Invalid query parameters"),
                                @ApiResponse(responseCode = "500", description = "Unexpected server error")
                        }
                )
        )
})
public @interface BootcampApiInfo {}
