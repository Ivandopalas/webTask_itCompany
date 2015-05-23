package com.epam.it_company.command.util;


/**
 * Name of commands also contain access type of current command.
 */
public enum CommandName{
    REGISTRATION(CommandAccess.COMMON),
    LOGIN(CommandAccess.COMMON),
    CHANGE_LOCAL(CommandAccess.COMMON),
    LOGOUT(CommandAccess.COMMON),
    REFERENCE(CommandAccess.COMMON),

    GET_ALL_USERS(CommandAccess.ADMIN),
    DELETE_USER(CommandAccess.ADMIN),
    ADD_USER(CommandAccess.ADMIN),

    GET_CLIENT_ORDERS(CommandAccess.CLIENT),
    NEW_ORDER(CommandAccess.CLIENT),

    GET_CLIENTS(CommandAccess.MANAGER),
    CREATE_PROJECT(CommandAccess.MANAGER),
    CHECK_ORDER(CommandAccess.MANAGER),

    GET_PROJECTS(CommandAccess.DEVELOPER),
    ADD_PROJECT_TIMEWORKING(CommandAccess.DEVELOPER);

    private CommandAccess accessType;

    CommandName(CommandAccess accessType){
        this.accessType = accessType;
    }

    public CommandAccess getAccessType(){
        return accessType;
    }
}
