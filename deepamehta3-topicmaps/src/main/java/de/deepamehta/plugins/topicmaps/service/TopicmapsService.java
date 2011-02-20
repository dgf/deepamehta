package de.deepamehta.plugins.topicmaps.service;

import de.deepamehta.plugins.topicmaps.model.Topicmap;

import de.deepamehta.core.model.ClientContext;
import de.deepamehta.core.model.Topic;
import de.deepamehta.core.service.PluginService;



public interface TopicmapsService extends PluginService {

    public Topicmap getTopicmap(long topicmapId);

    public long addTopicToTopicmap(long topicmapId, long topicId, int x, int y);

    public long addRelationToTopicmap(long topicmapId, long relationId);

    public void removeRelationFromTopicmap(long topicmapId, long relationId, long refId);
}