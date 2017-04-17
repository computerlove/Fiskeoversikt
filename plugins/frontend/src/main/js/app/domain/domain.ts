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

export class Landingsdata {
    constructor(
        readonly fartøy: Fartøy[]
    ){}
}

export class Fartøy {
    constructor(
       readonly id: string,
       readonly dataByDate: LandingsdataByLandingdate[]
    ) {}
}

export class LandingsdataByLandingdate {
    constructor(
      readonly landingsdato: LocalDate,
      readonly leveringslinjer: Array<Leveringslinje>
    ){}
}

export class Tilstand {
    constructor(
        readonly landingsdata: Landingsdata,
        readonly laster: boolean = true
    ) {}

    withLaster (laster: boolean) {
        return new Tilstand(this.landingsdata, laster)
    }

    withData (data: Landingsdata) {
        return new Tilstand(data, this.laster);
    }
}

export type Timespan = {from: LocalDate, to: LocalDate};
