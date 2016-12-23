import {Landingsdata, Leveringslinje, Timespan} from "./domain/domain";
import * as WebRequest from 'web-request';
import {LocalDate} from 'js-joda';

type LandingsdataResponse = {
    fraDato: string,
    tilDato: string,
    fartoy: Array<string>,
    leveringslinjer: Array<{
        id: string,
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

export function getData(timespan?: Timespan) : Promise<Landingsdata> {
    const params = timespan ? '?fraDato=' + timespan.from.toString() + '&tilDato=' + timespan.to.toString() : '';
    return new Promise<Landingsdata>((resolve, reject) => {
        WebRequest.json<LandingsdataResponse>(window.location.href + 'api/landingsdata' + params)
            .then(response =>
                resolve(new Landingsdata(
                    LocalDate.parse(response.fraDato),
                    LocalDate.parse(response.tilDato),
                    response.fartoy,
                    response.leveringslinjer
                        .map(ll => new Leveringslinje(
                            ll.id,
                            ll.fartoy,
                            LocalDate.parse(ll.landingsdato),
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