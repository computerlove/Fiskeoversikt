import {ActionType} from "../actions/index";
import {Tilstand, Landingsdata} from "../domain/index";
const InitialState = new Tilstand(new Landingsdata(null, null, [], []));

export default (state = InitialState, action) => {
    console.info(ActionType[action.type] || 'INITIALIZING');
    let actionType : ActionType = action.type;
    switch (actionType) {
        case ActionType.FETCH_DATA:
            return state.withLaster(true);
        default:
            return state;
    }
}
