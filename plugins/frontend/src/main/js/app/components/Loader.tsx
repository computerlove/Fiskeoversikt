import * as React from 'react';

export default class Loader extends React.Component<{}, {}> {
    render(){
        return (
            <div className="loader">
                <svg viewBox="0 0 32 32" width="32" height="32">
                    <circle id="spinner" cx="16" cy="16" r="14" fill="none"></circle>
                </svg>
            </div>
        );
    }
}