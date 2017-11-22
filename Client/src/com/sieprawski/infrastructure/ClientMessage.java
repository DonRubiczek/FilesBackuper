package com.sieprawski.infrastructure;

public enum ClientMessage {

    SENDING_FILE_FINISHED,
    BACKUP_FINISHED,
    END_OF_FILE_LIST,
    CONNECTED,
    LIST_MY_FILES,
    LIST_MY_FILES_WITHOUT_HASHES,
    BACKUP_FILES,
    RECOVER_FILES,
    RECOVER_FILES_FINISHED,
    REMOVE_ALL_FILES,
    REMOVE_SELECTED_FILE,
    NOT_RECOGNIZED,
    EXIT;



    public static boolean contains(String text) {

        for (ClientMessage state : ClientMessage.values()) {

            if (state.name().equals(text)) {

                return true;

            }

        }

        return false;

    }
}
