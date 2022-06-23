package in.prismar.library.common;

import in.prismar.library.common.event.Event;
import in.prismar.library.common.event.EventBus;
import in.prismar.library.common.event.EventSubscriber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class EventBusTest {

    @Test
    public void onSubscribeTest() {
        EventBus bus = new EventBus();

        bus.subscribe(TestEvent.class, new TestEventSubscriber());

        bus.publish(new TestEvent("Testing"));
    }

    @Getter
    @AllArgsConstructor
    class TestEvent implements Event {

        private String name;
    }

    class TestEventSubscriber implements EventSubscriber<TestEvent> {

        @Override
        public void onEvent(TestEvent entity) {
            Assertions.assertEquals("Testing", entity.getName());
        }
    }
}
