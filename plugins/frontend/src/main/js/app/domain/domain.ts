export class Leveringslinje {
    constructor(
        public fartoy: string,
        public landingsdato: Date,
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
        public fraDato: Date,
        public tilDato: Date,
        public fartoy: Array<string>,
        public leveringslinjer: Array<Leveringslinje>
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

    withData (data: Landingsdata) {
        return new Tilstand(data, this.laster);
    }
}