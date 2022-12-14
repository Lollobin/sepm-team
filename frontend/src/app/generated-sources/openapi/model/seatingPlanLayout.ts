/**
 * Ticketline API
 * Documentation for the Ticketline API
 *
 * The version of the OpenAPI document: 0.0.1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { SeatingPlanLayoutGeneral } from './seatingPlanLayoutGeneral';
import { SeatingPlanSector } from './seatingPlanSector';
import { SeatingPlanStaticElement } from './seatingPlanStaticElement';
import { SeatingPlanSeat } from './seatingPlanSeat';


export interface SeatingPlanLayout { 
    general: SeatingPlanLayoutGeneral;
    seats: Array<SeatingPlanSeat>;
    sectors: Array<SeatingPlanSector>;
    staticElements: Array<SeatingPlanStaticElement>;
}

