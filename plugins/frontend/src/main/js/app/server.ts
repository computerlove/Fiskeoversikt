import {Landingsdata, Leveringslinje} from "./domain/domain";
import * as WebRequest from 'web-request';

type LandingsdataResponse = {
    fraDato: string,
    tilDato: string,
    fartoy: Array<string>,
    leveringslinjer: Array<{
        fartoy: string,
        landingsdato: string,
        mottak: string,
        fiskeslag: string,
        tilstand: string,
        storrelse: string,
        kvalitet: string,
        nettovekt: number
    }>
}
export function getData() : Promise<Landingsdata> {
    return new Promise<Landingsdata>((resolve, reject) => {
        WebRequest.json<LandingsdataResponse>(window.location.href + 'api/landingsdata')
            .then(response =>
                resolve(new Landingsdata(
                    new Date(response.fraDato),
                    new Date(response.tilDato),
                    response.fartoy,
                    response.leveringslinjer
                        .map(ll => new Leveringslinje(
                            ll.fartoy,
                            new Date(ll.landingsdato),
                            ll.mottak,
                            ll.fiskeslag,
                            ll.tilstand,
                            ll.storrelse,
                            ll.kvalitet,
                            ll.nettovekt
                        ))
                )))
            .catch(rejection => reject(rejection));
    });

}