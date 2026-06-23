package com.google.firebase.database.tubesock;

/* JADX INFO: loaded from: classes.dex */
public interface WebSocketEventHandler {
    void onClose();

    void onError(WebSocketException webSocketException);

    void onLogMessage(String str);

    void onMessage(WebSocketMessage webSocketMessage);

    void onOpen();
}
