package com.carolinarollergirls.scoreboard.core;
/**
 * Copyright (C) 2008-2012 Mr Temper <MrTemper@CarolinaRollergirls.com>
 *
 * This file is part of the Carolina Rollergirls (CRG) ScoreBoard.
 * The CRG ScoreBoard is licensed under either the GNU General Public
 * License version 3 (or later), or the Apache License 2.0, at your option.
 * See the file COPYING for details.
 */

import com.carolinarollergirls.scoreboard.event.ScoreBoardEvent.AddRemoveProperty;
import com.carolinarollergirls.scoreboard.event.ScoreBoardEvent.PermanentProperty;
import com.carolinarollergirls.scoreboard.event.ScoreBoardEvent.ValueWithId;
import com.carolinarollergirls.scoreboard.event.ScoreBoardEventProvider;

public interface Clients extends ScoreBoardEventProvider {
    public enum Child implements AddRemoveProperty {
        CLIENT(Client.class),
        DEVICE(Device.class);

        private Child(Class<? extends ValueWithId> t) { type = t; }

        private final Class<? extends ValueWithId> type;

        @Override
        public Class<? extends ValueWithId> getType() { return type; }
    }

    public Device getDevice(String sessionId);
    public Device getOrAddDevice(String sessionId);
    public int gcOldDevices(long threshold);

    public Client addClient(String deviceId, String remoteAddr, String source, String platform);
    public void removeClient(Client c);

    // An active websocket client.
    public static interface Client extends ScoreBoardEventProvider {
        public void write();

        public enum Value implements PermanentProperty {
            DEVICE(Device.class, null),
            REMOTE_ADDR(String.class, ""),
            PLATFORM(String.class, ""),
            SOURCE(String.class, ""),
            CREATED(Long.class, 0L),
            WROTE(Long.class, 0L);

            private Value(Class<?> t, Object dv) { type = t; defaultValue = dv; }

            private final Class<?> type;
            private final Object defaultValue;

            @Override
            public Class<?> getType() { return type; }
            @Override
            public Object getDefaultValue() { return defaultValue; }
        }
    }

    // A device is a HTTP cookie.
    public static interface Device extends ScoreBoardEventProvider {
        public String getName();

        public void access();
        public void write();

        public enum Value implements PermanentProperty {
            SESSION_ID_SECRET(String.class, ""), // The cookie.
            NAME(String.class, ""), // A human-readable name.
            REMOTE_ADDR(String.class, ""),
            PLATFORM(String.class, ""),
            COMMENT(String.class, ""),
            CREATED(Long.class, 0L),
            WROTE(Long.class, 0L),
            ACCESSED(Long.class, 0L);

            private Value(Class<?> t, Object dv) { type = t; defaultValue = dv; }

            private final Class<?> type;
            private final Object defaultValue;

            @Override
            public Class<?> getType() { return type; }
            @Override
            public Object getDefaultValue() { return defaultValue; }
        }

        public enum Child implements AddRemoveProperty {
            CLIENT(Client.class);

            private Child(Class<? extends ValueWithId> t) { type = t; }

            private final Class<? extends ValueWithId> type;

            @Override
            public Class<? extends ValueWithId> getType() { return type; }
        }
    }
}