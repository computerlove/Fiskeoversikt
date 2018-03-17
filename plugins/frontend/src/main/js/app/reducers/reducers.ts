import {ActionType} from "../actions/actions";
import {Tilstand} from "../domain/domain";

const InitialState = new Tilstand();

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
        case ActionType.RELOAD_DATA:
            return state
                .withLaster(false)
                .withAllData(action.data);
        default:
            return state;
    }
}
