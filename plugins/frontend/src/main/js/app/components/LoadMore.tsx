import * as React from 'react';
import {connect} from "react-redux";
import {Tilstand} from "../domain/domain";
import {loadMore, loadMoreT} from "../actions/actions";

interface LoadMoreProps {
    numLoaded: number,
    loadMore: loadMoreT
}
class LoadMore extends React.Component<LoadMoreProps, {}> {
    render(){
        return (
            <button className="loadmore" onClick={(e) => this.props.loadMore(this.props.numLoaded)}>
                Mer
            </button>
        );
    }
}
const mapStateToProps = (state: Tilstand) => {
    return {
        numLoaded: state.landingsdata.length
    }
};
const mapDispatchToProps =  ({
    loadMore: loadMore
});
export default connect(mapStateToProps, mapDispatchToProps)(LoadMore);