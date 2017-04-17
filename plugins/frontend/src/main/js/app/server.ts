import {Fartøy, Landingsdata, LandingsdataByLandingdate, Leveringslinje, Timespan} from "./domain/domain";
import * as WebRequest from 'web-request';
import {LocalDate} from 'js-joda';

type LandingsdataResponse = {
    landingsdata: {
        [index:string]: Array<{
            [index:string]:Array<{
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
        }>
    }
}

export function getData(timespan?: Timespan) : Promise<Landingsdata> {
    const params = timespan ? '?fraDato=' + timespan.from.toString() + '&tilDato=' + timespan.to.toString() : '';
    return new Promise<Landingsdata>((resolve, reject) => {
        WebRequest.json<LandingsdataResponse>(window.location.href + 'api/landingsdata' + params)
            .then(response => {
                const ld = response.landingsdata;
                const a: Fartøy[] = Object.keys(ld).map(f => {
                    return new Fartøy(f,
                        Object.keys(f).map(d =>
                            new LandingsdataByLandingdate(LocalDate.parse(d), f[d].map(ll => new Leveringslinje(
                                ll.id,
                                ll.fartoy,
                                LocalDate.parse(ll.landingsdato),
                                ll.mottak,
                                ll.fiskeslag,
                                ll.tilstand,
                                ll.storrelse,
                                ll.kvalitet,
                                ll.nettovekt
                            )))))
                });
                resolve(new Landingsdata(
                    a
                ))
            })
            .catch(rejection => reject(rejection));
    });

}