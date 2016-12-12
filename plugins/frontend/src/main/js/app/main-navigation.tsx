import * as React from 'react';
import { Link} from 'react-router';

var activeSelection = '';


interface AppProps {children?: Array<React.ReactNode>}
export default class MainNavigation extends React.Component<AppProps, {}> {
    handleClick(event) {
        var href = event.target.href;
        var selection = href.substr(href.indexOf('#') + 1, href.length); // remove #
        activeSelection = selection;
    }
    render() {
        return (
            <nav className="container" id="mainNav">
                <ul className="navigation">
                    <li>
                        <Link to="landingsdata" onClick={ this.handleClick }
                              className={activeSelection === 'landingsdata' ? 'active' : ''}>
                            Landingsdata
                        </Link>
                    </li>
                    <li>
                        <Link to="aggregert"
                              onClick={ this.handleClick }
                              className={activeSelection === 'aggregert' ? 'active' : ''}>
                            Aggregert
                        </Link>
                    </li>
                </ul>
            </nav>
        );
    }
}