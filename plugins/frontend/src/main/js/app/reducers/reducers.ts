import {ActionType} from "../actions/actions";
import {Tilstand, Landingsdata} from "../domain/domain";
import {LocalDate} from 'js-joda';

const InitialState = new Tilstand(new Landingsdata(LocalDate.now(),LocalDate.now(), [], []));

export default (state = InitialState, action) => {
    console.info(ActionType[action.type] || 'INITIALIZING');
    let actionType : ActionType = action.type;
    switch (actionType) {
        case ActionType.FETCH_DATA:
            return state.withLaster(true);
        case ActionType.RECIEVE_DATA:
            return state
                .withLaster(false)
                .withData(action.data);
        default:
            return state;
    }
}
