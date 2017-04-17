import {Landingsdata} from "../domain/domain";
import * as WebRequest from 'web-request';
import {LandingsdataResponse, transformLandingsdatoReponse} from "./transform";

export function getData(num: number, start: number) : Promise<Landingsdata> {
    const params = `?num=${num}&start=${start}`;
    return new Promise<Landingsdata>((resolve, reject) => {
        WebRequest.json<LandingsdataResponse>(window.location.href + 'api/landingsdata' + params)
            .then(response => {
                resolve(transformLandingsdatoReponse(response))
            })
            .catch(rejection => reject(rejection));
    });

}