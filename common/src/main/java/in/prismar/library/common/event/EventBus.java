package in.prismar.library.common.event;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class EventBus {

    private Map<Class<? extends Event>, List<EventSubscriber<? extends Event>>> subscribers;

    public EventBus() {
        this.subscribers = new HashMap<>();
    }

    public <T extends Event> boolean hasSubscribers(Class<T> type) {
        return this.subscribers.containsKey(type);
    }

    public <T extends Event> void subscribe(Class<T> type, EventSubscriber<T> subscriber) {
        if(!this.subscribers.containsKey(type)) {
            this.subscribers.put(type, new ArrayList<>());
        }
        List<EventSubscriber<? extends Event>> subscribers = this.subscribers.get(type);
        subscribers.add(subscriber);
    }

    public <T extends Event> void publish(T event) {
        if(this.subscribers.containsKey(event.getClass())) {
            List<EventSubscriber<? extends Event>> subscribers = this.subscribers.get(event.getClass());
            for(EventSubscriber<? extends Event> subscriber : subscribers) {
                EventSubscriber<T> eventSubscriber = (EventSubscriber<T>)subscriber;
                eventSubscriber.onEvent(event);
            }
        }
    }
}
