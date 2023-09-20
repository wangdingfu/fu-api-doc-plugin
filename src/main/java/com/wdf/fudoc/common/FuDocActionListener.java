package com.wdf.fudoc.common;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

/**
 * @author wangdingfu
 * @date 2023-09-19 22:24:23
 */
public interface FuDocActionListener extends EventListener {

    @Topic.AppLevel
    Topic<FuDocActionListener> TOPIC = new Topic<>("fudoc actions", FuDocActionListener.class, Topic.BroadcastDirection.NONE);


    void action(String action);
}
