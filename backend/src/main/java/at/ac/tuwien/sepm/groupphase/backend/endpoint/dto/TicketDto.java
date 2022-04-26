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
 * TicketDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-26T12:09:28.088881827+02:00[Europe/Vienna]")
public class TicketDto   {

  @JsonProperty("ticketId")
  private BigDecimal ticketId;

  @JsonProperty("rowNumber")
  private BigDecimal rowNumber;

  @JsonProperty("seatNumber")
  private BigDecimal seatNumber;

  @JsonProperty("sector")
  private BigDecimal sector;

  public TicketDto ticketId(BigDecimal ticketId) {
    this.ticketId = ticketId;
    return this;
  }

  /**
   * Get ticketId
   * @return ticketId
  */
  @NotNull @Valid 
  @Schema(name = "ticketId", required = true)
  public BigDecimal getTicketId() {
    return ticketId;
  }

  public void setTicketId(BigDecimal ticketId) {
    this.ticketId = ticketId;
  }

  public TicketDto rowNumber(BigDecimal rowNumber) {
    this.rowNumber = rowNumber;
    return this;
  }

  /**
   * Get rowNumber
   * @return rowNumber
  */
  @Valid 
  @Schema(name = "rowNumber", required = false)
  public BigDecimal getRowNumber() {
    return rowNumber;
  }

  public void setRowNumber(BigDecimal rowNumber) {
    this.rowNumber = rowNumber;
  }

  public TicketDto seatNumber(BigDecimal seatNumber) {
    this.seatNumber = seatNumber;
    return this;
  }

  /**
   * Get seatNumber
   * @return seatNumber
  */
  @Valid 
  @Schema(name = "seatNumber", required = false)
  public BigDecimal getSeatNumber() {
    return seatNumber;
  }

  public void setSeatNumber(BigDecimal seatNumber) {
    this.seatNumber = seatNumber;
  }

  public TicketDto sector(BigDecimal sector) {
    this.sector = sector;
    return this;
  }

  /**
   * Get sector
   * @return sector
  */
  @NotNull @Valid 
  @Schema(name = "sector", required = true)
  public BigDecimal getSector() {
    return sector;
  }

  public void setSector(BigDecimal sector) {
    this.sector = sector;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TicketDto ticket = (TicketDto) o;
    return Objects.equals(this.ticketId, ticket.ticketId) &&
        Objects.equals(this.rowNumber, ticket.rowNumber) &&
        Objects.equals(this.seatNumber, ticket.seatNumber) &&
        Objects.equals(this.sector, ticket.sector);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticketId, rowNumber, seatNumber, sector);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TicketDto {\n");
    sb.append("    ticketId: ").append(toIndentedString(ticketId)).append("\n");
    sb.append("    rowNumber: ").append(toIndentedString(rowNumber)).append("\n");
    sb.append("    seatNumber: ").append(toIndentedString(seatNumber)).append("\n");
    sb.append("    sector: ").append(toIndentedString(sector)).append("\n");
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

