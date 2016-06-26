import React from 'react';
import Component from 'omniscient';
import { Link, Router } from 'react-router';

var activeSelection = '';

const MainNavigation = Component('MainNavigation', () => {
    return (
        <nav className="container" id="mainNav">
            <ul className="navigation">
                <li>
                    <Link to="landingsdata" onClick={ handleClick }
                          className={activeSelection === 'landingsdata' ? 'active' : ''}>
                        Landingsdata
                    </Link>
                </li>
                <li>
                    <Link to="aggregert"
                          onClick={ handleClick }
                          className={activeSelection === 'aggregert' ? 'active' : ''}>
                        Aggregert
                    </Link>
                </li>
            </ul>
        </nav>
    );
});

function handleClick(event) {
    var href = event.target.href;
    var selection = href.substr(href.indexOf('#') + 1, href.length); // remove #
    activeSelection = selection;
}

export default MainNavigation;
