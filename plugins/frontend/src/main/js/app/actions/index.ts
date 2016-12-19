
export enum ActionType {
    INIT,
    FETCH_DATA
}

export const fetchData = () => (dispatch) => {
    dispatch({
        type: ActionType.FETCH_DATA,
    });
    //return dispatch(fetchContract(contractId))
};