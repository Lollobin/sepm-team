import { Component, OnInit, ViewChild } from "@angular/core";
import { SectorBuilder } from "src/app/shared_modules/seatingPlanGraphics";
import {faPlus, faXmark} from "@fortawesome/free-solid-svg-icons";
import { SeatingPlanEditorComponent } from "../seating-plan-editor/seating-plan-editor.component";
import {
  LocationsService,
  SeatingPlanWithoutId,
  Location,
  SeatingPlanSector,
  SeatingPlanSeat,
  SeatingPlanStaticElement,
  SeatingPlansService,
  SeatingPlanLayout,
} from "src/app/generated-sources/openapi";
import { ActivatedRoute, Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";

type ClickElement =
  | {
      data: SeatingPlanSector;
      type: "SectorWithLocation";
    }
  | { data: SeatingPlanSeat; type: "Seat" }
  | { data: SeatingPlanStaticElement; type: "StaticElement" };

@Component({
  selector: "app-create-seating-plan",
  templateUrl: "./create-seating-plan.component.html",
  styleUrls: ["./create-seating-plan.component.scss"],
})
export class CreateSeatingPlanComponent implements OnInit {
  @ViewChild(SeatingPlanEditorComponent) seatingPlanEditor: SeatingPlanEditorComponent;
  locationId: number;
  page: number;
  faXmark = faXmark;
  sectors: SectorBuilder[] = [];
  chosenElement: ClickElement;
  selectedSector: number;
  seatInformation: { [provisionalId: number]: { rowNumber: number; seatNumber: number } } = {};
  seatingPlanName = "";
  location: Location;
  plus = faPlus;

  constructor(
    private route: ActivatedRoute,
    private locationsService: LocationsService,
    private seatingPlansService: SeatingPlansService,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.page = 1;
    this.route.params.subscribe((params) => {
      this.locationId = params["id"];
      this.locationsService.locationsIdGet(this.locationId).subscribe({
        next: (location) => {
          this.location = location;
          console.log(location);
        },
        error: (error) => {
          console.log(error);
          if (error.status === 0 || error.status === 500) {
            this.toastr.error(error.message);
          } else {
            this.toastr.warning(error.error);
          }
        }
      });
    });
    this.addSector();
  }
  removeSector(index: number) {
    this.sectors.splice(index, 1);
  }
  numberToCssColorString(color: number) {
    return `#${color.toString(16).padStart(6, "0")}`;
  }
  parseColor(color: string): number {
    return Number.parseInt(color.substring(1), 16);
  }
  addSector() {
    this.sectors.push({
      color: "#000000",
      standingSector: true,
      description: "",
      seatCount: 1,
    });
  }
  nextPage() {
    this.page++;
  }
  previousPage() {
    this.page--;
  }
  finish() {
    const seatingPlanLayout: SeatingPlanLayout = this.seatingPlanEditor.seatingPlan;
    const seatingPlan: SeatingPlanWithoutId = {
      name: this.seatingPlanName,
      locationId: this.location.locationId,
      sectors: seatingPlanLayout.sectors.map((sector) => ({ id: sector.id })),
      seats: seatingPlanLayout.seats.map((seat) => ({
        id: seat.id,
        seatNumber: this.seatInformation[seat.id]?.seatNumber,
        rowNumber: this.seatInformation[seat.id]?.rowNumber,
        sector: seat.sectorId,
      })),
      seatingPlanLayout,
    };
    this.seatingPlansService.seatingPlansPost(seatingPlan).subscribe({
      next: () => {
        this.router.navigate(["/", "locations", this.location.locationId]);
        this.toastr.success("Successfully added seating plan!");
      },
      error: (error) => {
        console.log(error);
        if (error.status === 0 || error.status === 500) {
          this.toastr.error(error.message);
        } else {
          this.toastr.warning(error.error);
        }
      }
    });
  }
  convertToCurrency(value: number) {
    return value.toLocaleString(undefined, { style: "currency", currency: "EUR" });
  }
  handleChosenElement(element: ClickElement) {
    if (element.type === "Seat" && this.seatInformation[element.data.id] === undefined) {
      this.seatInformation[element.data.id] = { rowNumber: 0, seatNumber: 0 };
    }
    this.chosenElement = element;
  }
}
