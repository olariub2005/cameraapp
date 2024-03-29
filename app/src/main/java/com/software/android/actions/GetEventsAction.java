package com.software.android.actions;

import com.facebook.GraphResponse;
import com.google.gson.reflect.TypeToken;
import com.software.android.cameraapp.SessionManager;
import com.software.android.entities.Event;
import com.software.android.entities.Event.EventDecision;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;

/**
 * Get the events of the profile/group/page. <br>
 * <br>
 * The default retrieved events will be events that the user is attending. If
 * you want to get events that the user said 'maybe' or totally declined, then
 * you need to set the method: {@link #setEventDecision(EventDecision)}.
 *
 * 
 * // @see https://developers.facebook.com/docs/graph-api/reference/user/events/
 */
public class GetEventsAction extends GetAction<List<Event>> {

    private EventDecision mEventDesicion = EventDecision.ATTENDING; // default

    public GetEventsAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    /**
     * Get the events of the user based on his/her decision. You can not ask for
     * all events of the user and only then filter, you have to filter by
     * attendance of the user.
     *
     * @param eventDesicion
     */
    public void setEventDecision(EventDecision eventDesicion) {
        mEventDesicion = eventDesicion;
    }

    @Override
    protected String getGraphPath() {
        // example path: {user-id}/events/attending
        return getTarget() + "/" + GraphPath.EVENTS + "/" + mEventDesicion.getGraphNode();
    }

    @Override
    protected List<Event> processResponse(GraphResponse response) {
        Utils.DataResult<Event> dataResult = Utils.convert(response, new TypeToken<Utils.DataResult<Event>>() {
        }.getType());
        return dataResult.data;
    }

}
