package com.epam.it_company.command.util;


import com.epam.it_company.command.ICommand;
import com.epam.it_company.command.impl.authorize.LoginCommand;
import com.epam.it_company.command.impl.authorize.LogoutCommand;
import com.epam.it_company.command.impl.authorize.RegistrationCommand;
import com.epam.it_company.command.impl.content.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Command pattern. Allow get any command by name.
 */
public class CommandHelper {

    private static final CommandHelper instance = new CommandHelper();
    private Map<CommandName,ICommand> commands;

    public CommandHelper(){
        commands = new HashMap<CommandName, ICommand>();
        commands.put(CommandName.REGISTRATION,new RegistrationCommand());
        commands.put(CommandName.LOGIN,new LoginCommand());
        commands.put(CommandName.CHANGE_LOCAL,new ChangeLocalCommand());
        commands.put(CommandName.LOGOUT,new LogoutCommand());
        commands.put(CommandName.GET_CLIENT_ORDERS,new GetClientOrdersCommand());
        commands.put(CommandName.NEW_ORDER,new CreateOrderCommand());
        commands.put(CommandName.REFERENCE,new ReferenceCommand());
        commands.put(CommandName.GET_CLIENT_ORDERS, new GetClientOrdersCommand());
        commands.put(CommandName.GET_CLIENTS,new GetClientsCommand());
        commands.put(CommandName.CREATE_PROJECT, new CreateProjectCommand());
        commands.put(CommandName.CHECK_ORDER, new CheckOrderCommand());
        commands.put(CommandName.GET_PROJECTS,new GetProjectsCommand());
        commands.put(CommandName.ADD_PROJECT_TIMEWORKING,new AddProjectTimeCommand());
        commands.put(CommandName.GET_ALL_USERS,new GetAllUsersCommand());
        commands.put(CommandName.DELETE_USER, new DeleteUserCommand());
        commands.put(CommandName.ADD_USER,new AddUserCommand());
    }
    public static CommandHelper getInstance(){
        return instance;
    }
    public ICommand getCommand(String cName){
        CommandName commandName = CommandName.valueOf(cName.toUpperCase());
        ICommand command;
        command = commands.get(commandName);
        return command;
    }
}
