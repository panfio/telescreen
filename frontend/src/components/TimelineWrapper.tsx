import React from 'react';
import Timeline, {
    TimelineMarkers,
    CustomMarker,
    CursorMarker
} from 'react-calendar-timeline'
import 'react-calendar-timeline/lib/Timeline.css'
import moment from 'moment'

const now = Date.now()

interface Props {
    items: any,
    groups: any
}

interface State {
    items: any
}

class TimelineWrapper extends React.Component<Props, State> {
    render() {
        const items  = this.props.items;
        const groups = this.props.groups;
        return (
            <div>
                <Timeline
                    groups={groups}
                    items={items}
                    defaultTimeStart={moment().add(-24, 'hour')}
                    defaultTimeEnd={moment()}
                    minZoom={60000}>
                    <TimelineMarkers>
                        <CustomMarker date={now} />
                        <CursorMarker />
                    </TimelineMarkers>
                </Timeline>
            </div>

        );
    }
}

export default TimelineWrapper;
