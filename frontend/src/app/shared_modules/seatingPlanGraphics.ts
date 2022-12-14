import { countBy } from "lodash";
import { Container, Graphics, Text } from "pixi.js";
import { SeatingPlanLayout, SeatingPlanSector } from "../generated-sources/openapi";

interface Location {
  x: number;
  y: number;
  w: number;
  h: number;
}
interface Color {
  baseColor: number;
  strokeColor: number;
}

interface SectorBuilder {
  color: string;
  standingSector: boolean;
  description: string;
  seatCount: number;
}

const generateSeatId = (id: number) => `seat${id}`;
const generateStandingAreaId = (id: number) => `standingArea${id}`;
const generateStaticAreaId = (id: number) => `staticArea${id}`;
const drawSeatingPlan = (stage: Container, seatingPlan: SeatingPlanLayout) => {
  drawSeats(stage, seatingPlan);
  drawStandingAreas(stage, seatingPlan);
  drawStaticAreas(stage, seatingPlan);
};
const drawSeatingPlanPreview = (stage: Container, seatingPlan: SeatingPlanLayout) => {
  drawSeats(stage, seatingPlan);
  drawStandingAreasPreview(stage, seatingPlan);
  drawStaticAreas(stage, seatingPlan);
};
const drawSeats = (stage: Container, seatingPlan: SeatingPlanLayout) => {
  const sectorMap: { [id: number]: SeatingPlanSector } = {};
  for (const sector of seatingPlan.sectors) {
    sectorMap[sector.id] = sector;
  }
  for (const seat of seatingPlan.seats) {
    if (sectorMap[seat.sectorId].noSeats === false) {
      const seatGraphics = drawArea(
        seat.location,
        { baseColor: 0xf0f0f0, strokeColor: sectorMap[seat.sectorId].color },
        4
      );
      seatGraphics.name = generateSeatId(seat.id);
      stage.addChild(seatGraphics);

      const cover = drawArea(
        seat.location,
        { baseColor: 0x000055, strokeColor: sectorMap[seat.sectorId].color },
        4
      );
      cover.name = `${generateSeatId(seat.id)}_cover`;
      cover.visible = false;
      stage.addChild(cover);
    }
  }
};
const drawStandingAreas = (stage: Container, seatingPlan: SeatingPlanLayout) => {
  const standingAreas = seatingPlan.sectors.filter(
    (sector) => sector.noSeats
  ) as Array<SeatingPlanSector>;
  const seatCounts = countBy(seatingPlan.seats, "sectorId");
  for (const standingArea of standingAreas) {
    const numberOfAvailableSeats = seatCounts[standingArea.id];
    const standingAreaGraphics = drawStandingArea(
      standingArea.location,
      { baseColor: 0xf0f0f0, strokeColor: standingArea.color },
      numberOfAvailableSeats,
      0,
      standingArea.description ? standingArea.description : ""
    );
    standingAreaGraphics.name = generateStandingAreaId(standingArea.id);
    stage.addChild(standingAreaGraphics);
  }
};
const drawStaticAreas = (stage: Container, seatingPlan: SeatingPlanLayout) => {
  for (const staticArea of seatingPlan.staticElements) {
    const staticGraphics = drawNoSeatArea(
      staticArea.location,
      { baseColor: 0xf0f0f0, strokeColor: staticArea.color },
      staticArea.description ? staticArea.description : ""
    );
    staticGraphics.name = generateStaticAreaId(staticArea.id);
    stage.addChild(staticGraphics);
  }
};

const drawPlus = (color: Color, w = 20, h = 20, strokeWidth = 3) => {
  const scaleFactor = 0.2;
  const plusContainer = drawArea({ x: 0, y: 0, w, h }, color, w);
  const plusSign = new Graphics()
    .lineStyle({ width: strokeWidth, color: color.strokeColor })
    .moveTo(w / 2, h - h * scaleFactor)
    .lineTo(w / 2, h * scaleFactor)
    .moveTo(w - w * scaleFactor, h / 2)
    .lineTo(w * scaleFactor, h / 2);
  plusContainer.addChild(plusSign);
  return plusContainer;
};
const drawMinus = (color: Color, w = 20, h = 20, strokeWidth = 3) => {
  const scaleFactor = 0.2;
  const plusContainer = drawArea({ x: 0, y: 0, w, h }, color, w);
  const plusSign = new Graphics()
    .lineStyle({ width: strokeWidth, color: color.strokeColor })
    .moveTo(w - w * scaleFactor, h / 2)
    .lineTo(w * scaleFactor, h / 2);
  plusContainer.addChild(plusSign);
  return plusContainer;
};

const calculateBoxCenterPoint = (parentWidth: number, childWidth: number) =>
  (parentWidth - childWidth) / 2;

const centerText = (text: Text, parentWidth: number) => {
  text.anchor.set(0.5, 0);
  text.setTransform(parentWidth / 2);
};

const drawStandingArea = (
  location: Location,
  color: Color,
  numberOfSeatsAvailable: number,
  numberOfSeatsUnavailable: number,
  text?: string
) => {
  const areaGraphics = drawArea(location, color, 0);
  const seatAvailability = drawText(
    `${numberOfSeatsUnavailable}/${numberOfSeatsAvailable}`,
    15,
    areaGraphics.width
  );
  seatAvailability.name = "seatAvailability";
  seatAvailability.setTransform(
    calculateBoxCenterPoint(areaGraphics.width, seatAvailability.width),
    location.h * 0.05
  );
  areaGraphics.addChild(seatAvailability);

  const plusMinusContainer = new Graphics();
  plusMinusContainer.name = "plusMinusContainer";
  plusMinusContainer.setTransform(
    0,
    seatAvailability.position.y + seatAvailability.height + location.h * 0.05
  );
  areaGraphics.addChild(plusMinusContainer);

  const plus = drawPlus(color);
  plus.name = "plus";
  plus.setTransform(areaGraphics.width / 2 + areaGraphics.width / 3 - plus.width);
  plusMinusContainer.addChild(plus);

  const minus = drawMinus(color);
  minus.name = "minus";
  minus.setTransform(areaGraphics.width / 2 - areaGraphics.width / 3);
  plusMinusContainer.addChild(minus);

  const ticketCounter = drawText("0", 15, 100);
  ticketCounter.name = "ticketCounter";
  ticketCounter.anchor.set(0.5, 0);
  ticketCounter.setTransform(areaGraphics.width / 2, 0);
  plusMinusContainer.addChild(ticketCounter);

  if (text) {
    const additionalText = drawText(text, 15, areaGraphics.width);
    centerText(additionalText, areaGraphics.width);
    additionalText.setTransform(
      additionalText.position.x,
      plusMinusContainer.position.y + plusMinusContainer.height + location.h * 0.05
    );
    areaGraphics.addChild(additionalText);
  }
  return areaGraphics;
};
const drawNoSeatArea = (location: Location, color: Color, text?: string) => {
  const areaGraphics = drawArea(location, color, 0);
  if (text) {
    const additionalText = drawText(text, 15, areaGraphics.width);
    centerText(additionalText, areaGraphics.width);
    additionalText.setTransform(additionalText.position.x, location.h * 0.05);
    areaGraphics.addChild(additionalText);
  }
  return areaGraphics;
};
const drawText = (text: string, fontSize: number, maxWidth: number) => {
  const textGraphics = new Text(text, {
    fontSize,
    wordWrap: true,
    wordWrapWidth: maxWidth,
    breakWords: true,
    align: "center",
  });
  return textGraphics;
};
const drawArea = (location: Location, color: Color, radius: number) => {
  const areaGraphics = new Graphics();
  const lineWidth = 3;
  areaGraphics
    .beginFill(color.baseColor)
    .lineStyle({ width: lineWidth, color: color.strokeColor, alignment: 0 })
    .setTransform(location.x, location.y)
    .drawRoundedRect(0, 0, location.w, location.h, radius);
  return areaGraphics;
};
const drawStandingAreaPreview = (
  location: Location,
  color: Color,
  text?: string
) => {
  const areaGraphics = drawArea(location, color, 0);
  if (text) {
    const additionalText = drawText(text, 15, areaGraphics.width);
    centerText(additionalText, areaGraphics.width);
    additionalText.setTransform(
      additionalText.position.x, location.h * 0.05
    );
    areaGraphics.addChild(additionalText);
  }
  return areaGraphics;
};
const drawStandingAreasPreview = (stage: Container, seatingPlan: SeatingPlanLayout) => {
  const standingAreas = seatingPlan.sectors.filter(
    (sector) => sector.noSeats
  ) as Array<SeatingPlanSector>;
  const seatCounts = countBy(seatingPlan.seats, "sectorId");
  for (const standingArea of standingAreas) {
    const standingAreaGraphics = drawStandingAreaPreview(
      standingArea.location,
      { baseColor: 0xf0f0f0, strokeColor: standingArea.color },
      standingArea.description ? standingArea.description : ""
    );
    standingAreaGraphics.name = generateStandingAreaId(standingArea.id);
    stage.addChild(standingAreaGraphics);
  }
};

export {
  drawSeatingPlan,
  drawSeatingPlanPreview,
  generateStandingAreaId,
  generateSeatId,
  generateStaticAreaId,
  SectorBuilder,
  Location,
};
