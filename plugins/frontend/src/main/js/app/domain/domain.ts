import {LocalDate} from 'js-joda';

export class Leveringslinje {
    constructor(
        public id: string,
        public fartoy: string,
        public landingsdato: LocalDate,
        public mottak: string,
        public fiskeslag: string,
        public tilstand: string,
        public storrelse: string,
        public kvalitet: string,
        public nettovekt: number
    ){}
}

export class Landingsdata {
    constructor(
        public fraDato: LocalDate,
        public tilDato: LocalDate,
        public fartoy: Array<string>,
        public leveringslinjer: Array<Leveringslinje>
    ){}
}

export class Tilstand {
    constructor(
        public landingsdata: Landingsdata,
        public laster: boolean = true
    ) {}

    withLaster (laster: boolean) {
        return new Tilstand(this.landingsdata, laster)
    }

    withData (data: Landingsdata) {
        return new Tilstand(data, this.laster);
    }
}

export type Timespan = {from: LocalDate, to: LocalDate};
