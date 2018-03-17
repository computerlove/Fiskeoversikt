import {LocalDate} from 'js-joda';

export class Leveringslinje {
    constructor(
        readonly id: string,
        readonly fartoy: string,
        readonly landingsdato: LocalDate,
        readonly mottak: string,
        readonly fiskeslag: string,
        readonly tilstand: string,
        readonly storrelse: string,
        readonly kvalitet: string,
        readonly nettovekt: number
    ){}
}

export class LandingsdataByLandingdate {
    constructor(
      readonly fartøy: string,
      readonly landingsdato: LocalDate,
      readonly leveringslinjer: Array<Leveringslinje>
    ){}
}

export class Tilstand {
    constructor(
        readonly landingsdata: LandingsdataByLandingdate[] = [],
        readonly laster: boolean = true
    ) {}

    withLaster (laster: boolean) {
        return new Tilstand(this.landingsdata, laster)
    }

    withData (landingsdata: LandingsdataByLandingdate[]) {
        return new Tilstand(this.landingsdata.concat(landingsdata), this.laster);
    }

    withAllData (landingsdata: LandingsdataByLandingdate[]) {
        return new Tilstand(landingsdata, this.laster);
    }
}

export type Timespan = {from: LocalDate, to: LocalDate};
