export class Leveringslinje {
    constructor(
        fartoy: string,
        landingsdato: Date,
        mottak: string,
        fiskeslag: string,
        tilstand: string,
        storrelse: string,
        kvalitet: string,
        nettovekt: number
    ){}
}

export class Landingsdata {
    constructor(
        fraDato: Date,
        tilDato: Date,
        fartoy: Array<string>,
        leveringslinjer: Array<Leveringslinje>
    ){}
}

export class Tilstand {
    constructor(
        public landingsdata: Landingsdata,
        public laster: boolean = false
    ) {}

    withLaster (laster: boolean) {
        return new Tilstand(this.landingsdata, laster)
    }
}