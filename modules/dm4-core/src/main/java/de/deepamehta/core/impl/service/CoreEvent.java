package de.deepamehta.core.impl.service;

import de.deepamehta.core.service.Listener;
import de.deepamehta.core.service.event.*;

import java.util.HashMap;
import java.util.Map;



/**
 * Events fired by the DeepaMehta core.
 * Plugins can listen to these events by implementing the respective listener interfaces.
 *
 * There are 2 types of events:
 *   - regular events: are fired (usually) by the core and then delivered to all registered listeners (plugins).
 *   - internal plugin events: are fired by a plugin and then delivered only to itself. There are 5 internal events:
 *     - POST_INSTALL_PLUGIN
 *     - INTRODUCE_TOPIC_TYPE (has a double nature)
 *     - INITIALIZE_PLUGIN
 *     - PLUGIN_SERVICE_ARRIVED
 *     - PLUGIN_SERVICE_GONE
 *
 * @see de.deepamehta.core.service.event
 */
enum CoreEvent {

    PRE_CREATE_TOPIC(PreCreateTopicListener.class),
    PRE_CREATE_ASSOCIATION(PreCreateAssociationListener.class),

    POST_CREATE_TOPIC(PostCreateTopicListener.class),
    POST_CREATE_ASSOCIATION(PostCreateAssociationListener.class),

    PRE_UPDATE_TOPIC(PreUpdateTopicListener.class),
    POST_UPDATE_TOPIC(PostUpdateTopicListener.class),

    PRE_DELETE_ASSOCIATION(PreDeleteAssociationListener.class),
    POST_DELETE_ASSOCIATION(PostDeleteAssociationListener.class),

    // ### TODO: remove this event. Retype is special case of update
    POST_RETYPE_ASSOCIATION(PostRetypeAssociationListener.class),

    PRE_SEND_TOPIC(PreSendTopicListener.class),
    PRE_SEND_ASSOCIATION(PreSendAssociationListener.class),
    PRE_SEND_TOPIC_TYPE(PreSendTopicTypeListener.class),

    ALL_PLUGINS_ACTIVE(AllPluginsActiveListener.class),

    // === Internal plugin events ===

    // ### TODO: remove this event. Use migration 1 instead.
    POST_INSTALL_PLUGIN(PostInstallPluginListener.class),

    INTRODUCE_TOPIC_TYPE(IntroduceTopicTypeListener.class),

    INITIALIZE_PLUGIN(InitializePluginListener.class),

    PLUGIN_SERVICE_ARRIVED(PluginServiceArrivedListener.class),
    PLUGIN_SERVICE_GONE(PluginServiceGoneListener.class);

    // ------------------------------------------------------------------------------------------------- Class Variables

    private static final Map<String, CoreEvent> events = new HashMap<String, CoreEvent>();

    static {
        for (CoreEvent event : CoreEvent.values()) {
            events.put(event.listenerInterface.getSimpleName(), event);
        }
    }

    // ---------------------------------------------------------------------------------------------- Instance Variables

    final Class<? extends Listener> listenerInterface;

    // ---------------------------------------------------------------------------------------------------- Constructors

    private CoreEvent(Class<? extends Listener> listenerInterface) {
        this.listenerInterface = listenerInterface;
    }

    // ----------------------------------------------------------------------------------------- Package Private Methods

    static CoreEvent fromListenerInterface(Class<Listener> listenerInterface) {
        return events.get(listenerInterface.getSimpleName());
    }

}
