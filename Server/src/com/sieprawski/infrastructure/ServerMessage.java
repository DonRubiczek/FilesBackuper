package com.sieprawski.infrastructure;

public enum ServerMessage {

    USER_DOES_NOT_EXIST,
    SEND_ME_USER_LOGIN,
    SEND_ME_USER_NAME,
    SEND_ME_FILE,
    SEND_ME_FILEPATH,
    SEND_ME_FILESIZE,
    SEND_ME_HASH,
    SENDING_FILEPATH,
    SENDING_FILE_HASH,
    SEND_FILE_LIST,
    SENDING_FILELIST,
    SENDING_FILELIST_FINISHED,
    WAITING_FOR_COMMANDS
    ;

    public static boolean contains(String text) {

        for (ServerMessage state : ServerMessage.values()) {

            if (state.name().equals(text)) {

                return true;

            }

        }

        return false;

    }
}
