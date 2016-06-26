import React from 'react';
import Component from 'omniscient';

const Aggregert = Component('aggregert', ({ cursor }) => {
    let result = [];

    cursor.cursor(['aggregert']).forEach((entry, key) => {
        result.push(<li />);
    });

    return <ul className="column-container">
        {result}
    </ul>
});

export default Aggregert;
