import * as React from 'react';
import {connect} from "react-redux";
import {Tilstand} from "../domain/domain";

interface LoadMoreProps {
    numLoaded: number
}
class LoadMore extends React.Component<LoadMoreProps, {}> {
    render(){
        return (
            <div className="loadmore">

            </div>
        );
    }
}
const mapStateToProps = (state: Tilstand) => {
    return {
        numLoaded: state.landingsdata.length
    }
};
const mapDispatchToProps =  ({
});
export default connect(mapStateToProps, mapDispatchToProps)(LoadMore);