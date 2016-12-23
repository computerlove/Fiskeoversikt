
import {getData} from "../server";
import {Timespan} from "../domain/domain";
export enum ActionType {
    INIT,
    FETCH_DATA,
    RECIEVE_DATA
}

export const doFetchData = (timespan?: Timespan) => (dispatch) => {
    getData(timespan)
        .then(landingsdata => dispatch({
            type: ActionType.RECIEVE_DATA,
            data: landingsdata
        }))
};

export const fetchData = () => (dispatch) => {
    dispatch({
        type: ActionType.FETCH_DATA,
    });
    return dispatch(doFetchData())
};
export const updateTimespan = (timespan: Timespan) => (dispatch) => {
    dispatch({
        type: ActionType.FETCH_DATA,
    });
    return dispatch(doFetchData(timespan))
};