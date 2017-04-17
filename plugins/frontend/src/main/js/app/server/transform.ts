import {Fartøy, Landingsdata, LandingsdataByLandingdate, Leveringslinje} from "../domain/domain";
import {LocalDate} from "js-joda";
export type LandingsdataResponse = {
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
export function transformLandingsdatoReponse(response: LandingsdataResponse): Landingsdata {
    return new Landingsdata(
        Object.keys(response.landingsdata).map(f => {
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
        })
    )
}