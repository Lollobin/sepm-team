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


export interface LocationSearch { 
    /**
     * Finds locations that contains the given string in their name
     */
    name?: string;
    /**
     * Finds locations that contains the given string in their street
     */
    street?: string;
    /**
     * Finds locations that contains the given string in their city
     */
    city?: string;
    /**
     * Finds locations that contains the given string in their country
     */
    country?: string;
    /**
     * Finds locations that contains the given string in their zipCode
     */
    zipCode?: string;
}

