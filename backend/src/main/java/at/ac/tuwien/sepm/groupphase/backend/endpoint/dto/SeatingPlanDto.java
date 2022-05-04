package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * SeatingPlanDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class SeatingPlanDto   {

  @JsonProperty("seatingPlanId")
  private Integer seatingPlanId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("seatingPlanLayoutId")
  private BigDecimal seatingPlanLayoutId;

  @JsonProperty("locationId")
  private Integer locationId;

  public SeatingPlanDto seatingPlanId(Integer seatingPlanId) {
    this.seatingPlanId = seatingPlanId;
    return this;
  }

  /**
   * Get seatingPlanId
   * @return seatingPlanId
  */
  @NotNull 
  @Schema(name = "seatingPlanId", required = true)
  public Integer getSeatingPlanId() {
    return seatingPlanId;
  }

  public void setSeatingPlanId(Integer seatingPlanId) {
    this.seatingPlanId = seatingPlanId;
  }

  public SeatingPlanDto name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  @NotNull 
  @Schema(name = "name", required = true)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SeatingPlanDto seatingPlanLayoutId(BigDecimal seatingPlanLayoutId) {
    this.seatingPlanLayoutId = seatingPlanLayoutId;
    return this;
  }

  /**
   * Get seatingPlanLayoutId
   * @return seatingPlanLayoutId
  */
  @NotNull @Valid 
  @Schema(name = "seatingPlanLayoutId", required = true)
  public BigDecimal getSeatingPlanLayoutId() {
    return seatingPlanLayoutId;
  }

  public void setSeatingPlanLayoutId(BigDecimal seatingPlanLayoutId) {
    this.seatingPlanLayoutId = seatingPlanLayoutId;
  }

  public SeatingPlanDto locationId(Integer locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * Get locationId
   * @return locationId
  */
  @NotNull 
  @Schema(name = "locationId", required = true)
  public Integer getLocationId() {
    return locationId;
  }

  public void setLocationId(Integer locationId) {
    this.locationId = locationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SeatingPlanDto seatingPlan = (SeatingPlanDto) o;
    return Objects.equals(this.seatingPlanId, seatingPlan.seatingPlanId) &&
        Objects.equals(this.name, seatingPlan.name) &&
        Objects.equals(this.seatingPlanLayoutId, seatingPlan.seatingPlanLayoutId) &&
        Objects.equals(this.locationId, seatingPlan.locationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seatingPlanId, name, seatingPlanLayoutId, locationId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SeatingPlanDto {\n");
    sb.append("    seatingPlanId: ").append(toIndentedString(seatingPlanId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    seatingPlanLayoutId: ").append(toIndentedString(seatingPlanLayoutId)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
