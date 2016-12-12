import request from 'qwest';
import Immutable from 'immutable';

export const postData = (data, queryUrl, onSuccess) => {
    console.log('Posting data to: ' + queryUrl);
    const options = {
        dataType: 'json'
    };
    request.post(queryUrl, data, options, function (err, httpResponse, body) {
        if ( err ) {
            console.log('PostError: ', err);
            return false;
        }
        return body;
    }).then(function (responseData) {
        onSuccess(responseData);
    }).catch(function (e, response) {
        console.log('Error posting data: ' + e);
    });
};

export const fetchDataNoUpdate = (queryUrl, onSuccess) => {
    console.log('Fetching data from: ' + queryUrl);
    request.get(queryUrl)
        .then(function (responseData) {
            onSuccess(responseData)
        })
        .catch(function (e, response) {
            console.log('Error: ', e);
        });
};

export const fetchData = (cursor, queryUrl, postProcess) => {
    console.log('Fetching data from: ' + queryUrl);
    request.get(queryUrl)
        .then(function (responseData) {
            if ( postProcess != undefined ) {
                responseData = postProcess(responseData);
            }
            cursor.update(() => (Immutable.fromJS(responseData)));
        })
        .catch(function (e, response) {-
            console.log('Error: ', e);
            if (e.indexOf("404") != -1) {
                cursor.update(() => Immutable.fromJS({ notfound: { message: e, url: queryUrl } }));
            } else {
                cursor.update(() => Immutable.fromJS({ error: { message: e, url: queryUrl } }));
            }
        });
};

export default fetchData;
