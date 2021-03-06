import * as WebRequest from 'web-request';
import {LandingsdataResponse, transformLandingsdatoReponse} from "./transform";
import {LandingsdataByLandingdate} from "../domain/domain";

export function getData(num: number, start: number) : Promise<LandingsdataByLandingdate[]> {
    const params = `?num=${num}&start=${start}`;
    return new Promise<LandingsdataByLandingdate[]>((resolve, reject) => {
        const url = (window.location.origin + '/api/landingsdata');
        WebRequest.json<LandingsdataResponse>(url + params)
            .then(response => {
                resolve(transformLandingsdatoReponse(response))
            })
            .catch(rejection => reject(rejection));
    });

}