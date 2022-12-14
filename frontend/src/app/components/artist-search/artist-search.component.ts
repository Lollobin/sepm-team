import { Component, OnInit } from '@angular/core';
import {
  Artist,
  ArtistsSearchResult,
  ArtistsService,
  EventSearch,
  EventSearchResult,
  EventsService
} from "../../generated-sources/openapi";
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-artist-search',
  templateUrl: './artist-search.component.html',
  styleUrls: ['./artist-search.component.scss']
})
export class ArtistSearchComponent implements OnInit {
  data: ArtistsSearchResult = null;
  page = 1;


  pageSize = 10;
  artists: Artist[];
  search: string;
  sort: 'ASC' | 'DESC' = 'ASC';
  eventsOfClickedArtist: EventSearchResult = null;
  clickedArtist: Artist;
  eventPageSize = 5;

  constructor(private artistService: ArtistsService, private eventsService: EventsService,
    private toastrService: ToastrService) {
  }

  ngOnInit() {
    this.onSearch();
  }

  onSearch() {
    this.eventsOfClickedArtist = null;
    this.clickedArtist = null;
    return this.artistService.artistsGet(
      this.search,
      this.pageSize,
      this.page - 1
    ).subscribe({
      next: result => {
        this.data = result;
        this.artists = result.artists;
        if (!this.data?.numberOfResults) {
          this.toastrService.info("There are no artists fitting your input!");
        }
      },
      error: (error) => {
        console.log(error);
        if (error.status === 0 || error.status === 500) {
          this.toastrService.error(error.message);
        } else {
          this.toastrService.warning(error.error);
        }
      }
    }
    );
  }

  onPageChange(ngbpage: number) {
    this.page = ngbpage;
    this.onSearch();
  }



  artistGetEvents(artist: Artist, childpage: number) {
    const id = artist.artistId;
    this.clickedArtist = artist;
    const searchParams: EventSearch = { artist: id };
    this.eventsService.eventsGet(searchParams, this.eventPageSize, childpage - 1).subscribe({
      next: response => {
        this.eventsOfClickedArtist = response;
        if (!this.eventsOfClickedArtist?.numberOfResults) {
          let artistName = "";
          if (this.clickedArtist.bandName) {
            artistName = this.clickedArtist.bandName;
          } else {
            if (this.clickedArtist.firstName && this.clickedArtist.lastName) {
              artistName += this.clickedArtist.firstName;
            }
            if (this.clickedArtist.knownAs) {
              if (this.clickedArtist.firstName && this.clickedArtist.lastName && this.clickedArtist.knownAs) {
                artistName += " \"" + this.clickedArtist.knownAs + "\"";
              } else {
                artistName = this.clickedArtist.knownAs;
              }
            }
            if (this.clickedArtist.lastName && this.clickedArtist.firstName) {
              artistName += " " + this.clickedArtist.lastName;
            }
          }
          this.toastrService.info("There are no events with " + artistName + "!");
        }
      },
      error: (error) => {
        console.log(error);
        if (error.status === 0 || error.status === 500) {
          this.toastrService.error(error.message);
        } else {
          this.toastrService.warning(error.error);
        }
      }

    });
  }

  handleEventPageEmit(number) {
    this.artistGetEvents(this.clickedArtist, number);
  }

  resetAll() {
    this.search = null;
    this.onSearch();
  }

}
