
import {getData} from "../server";
export enum ActionType {
    INIT,
    FETCH_DATA,
    RECIEVE_DATA
}

export const doFetchData = () => (dispatch) => {
    getData()
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