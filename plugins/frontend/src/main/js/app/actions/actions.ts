
import {getData} from "../server/server";

export enum ActionType {
    INIT,
    FETCH_DATA,
    RECIEVE_DATA
}

export type fetchDateT = (num?: number, start?: number) => any;
export const fetchData = (num: number = 3, start: number = 0) => (dispatch) => {
    dispatch({
        type: ActionType.FETCH_DATA,
    });
    getData(num, start)
        .then(landingsdata => dispatch({
            type: ActionType.RECIEVE_DATA,
            data: landingsdata
        }))
};

export type loadMoreT = (start: number) => any;
export const loadMore = (start: number) => (dispatch) => {
    dispatch({
        type: ActionType.FETCH_DATA,
    });
    getData(3, start)
        .then(landingsdata => dispatch({
            type: ActionType.RECIEVE_DATA,
            data: landingsdata
        }))
};