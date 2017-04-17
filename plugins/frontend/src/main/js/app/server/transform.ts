import {LandingsdataByLandingdate, Leveringslinje} from "../domain/domain";
import {LocalDate} from "js-joda";
export type LandingsdataResponse = [{
    fartøy: string,
    landingsdato: string,
    leveringslinjer:Array<{
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
}]

export function transformLandingsdatoReponse(response: LandingsdataResponse): LandingsdataByLandingdate[] {
    return response
        .map(x => new LandingsdataByLandingdate(
            x.fartøy,
            LocalDate.parse(x.landingsdato),
            x.leveringslinjer
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
        ))

}