import {ActionType} from "../actions/actions";
import {Tilstand, Landingsdata} from "../domain/domain";
const InitialState = new Tilstand(new Landingsdata(null, null, [], []));

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
