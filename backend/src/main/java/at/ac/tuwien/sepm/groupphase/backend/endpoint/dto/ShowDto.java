package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * ShowDto
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ShowDto   {

  @JsonProperty("showId")
  private Integer showId;

  @JsonProperty("date")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime date;

  @JsonProperty("event")
  private BigDecimal event;

  @JsonProperty("artists")
  @Valid
  private List<BigDecimal> artists = new ArrayList<>();

  public ShowDto showId(Integer showId) {
    this.showId = showId;
    return this;
  }

  /**
   * Get showId
   * @return showId
  */
  @NotNull 
  @Schema(name = "showId", required = true)
  public Integer getShowId() {
    return showId;
  }

  public void setShowId(Integer showId) {
    this.showId = showId;
  }

  public ShowDto date(OffsetDateTime date) {
    this.date = date;
    return this;
  }

  /**
   * Get date
   * @return date
  */
  @Valid 
  @Schema(name = "date", required = false)
  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  public ShowDto event(BigDecimal event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
  */
  @NotNull @Valid 
  @Schema(name = "event", required = true)
  public BigDecimal getEvent() {
    return event;
  }

  public void setEvent(BigDecimal event) {
    this.event = event;
  }

  public ShowDto artists(List<BigDecimal> artists) {
    this.artists = artists;
    return this;
  }

  public ShowDto addArtistsItem(BigDecimal artistsItem) {
    if (this.artists == null) {
      this.artists = new ArrayList<>();
    }
    this.artists.add(artistsItem);
    return this;
  }

  /**
   * Get artists
   * @return artists
  */
  @NotNull @Valid 
  @Schema(name = "artists", required = true)
  public List<BigDecimal> getArtists() {
    return artists;
  }

  public void setArtists(List<BigDecimal> artists) {
    this.artists = artists;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShowDto show = (ShowDto) o;
    return Objects.equals(this.showId, show.showId) &&
        Objects.equals(this.date, show.date) &&
        Objects.equals(this.event, show.event) &&
        Objects.equals(this.artists, show.artists);
  }

  @Override
  public int hashCode() {
    return Objects.hash(showId, date, event, artists);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShowDto {\n");
    sb.append("    showId: ").append(toIndentedString(showId)).append("\n");
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    artists: ").append(toIndentedString(artists)).append("\n");
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
