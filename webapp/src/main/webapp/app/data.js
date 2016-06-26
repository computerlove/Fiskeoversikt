import immstruct from 'immstruct';
import Immutable from 'immutable';

const data = immstruct({
    landingsdata : {
        fartoy : [],
        fraDato : null,
        tilDato: null,
        leveringslinjer: Immutable.fromJS([])
    }
});

export default data;
