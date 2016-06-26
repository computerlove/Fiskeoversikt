import React from 'react';
import Component from 'omniscient';
import fetchData from './data-request'

const Landingsdata = Component('landingsdata', ({ cursor }) => {
    let result = [];
    let landingsdata;
    if (( landingsdata = cursor.get('landingsdata')) == null) {
        fetchData(cursor.cursor('landingsdata'), '/landingsdata/');
        return <span>Henter data...</span>
    }

    let fartoy = landingsdata.get('fartoy');
    let fraDato = landingsdata.get('fraDato');
    let tilDato = landingsdata.get('tilDato');
    let leveringslinjer = landingsdata.get('leveringslinjer');
    leveringslinjer.forEach((entry, key) => {
        result.push(
            <li>
                <span>{entry.get('fartoy')}</span>
                <span>{entry.get('landingsdato')}</span>
                <span>{entry.get('mottak')}</span>
                <span>{entry.get('fiskeslag')}</span>
                <span>{entry.get('tilstand')}</span>
                <span>{entry.get('storrelse')}</span>
                <span>{entry.get('kvalitet')}</span>
                <span>{entry.get('nettovekt')}</span>
            </li>
        );
    });

    return <ul className="column-container">
        {result}
    </ul>
});

export default Landingsdata;
