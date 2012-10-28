package com.undeadscythes.udsplugin.commands;

import com.undeadscythes.udsplugin.HelpFile;
import com.undeadscythes.udsplugin.Color;
import com.undeadscythes.udsplugin.UDSPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCmd implements CommandExecutor {
    private final transient UDSPlugin plugin;

    public HelpCmd(final UDSPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(sender.hasPermission("udsplugin.help")) {
            int page = 1;
            String tag = "";
            if(args.length != 0) {
                if(args[0].matches("[0-9][0-9]*")) {
                    page = Integer.parseInt(args[0]);
                } else {
                    tag = args[0];
                }
            }
            if("".equals(tag)) {
                int available = 0;
                for(final HelpFile helpFile : UDSPlugin.getHelpFiles()) {
                    final boolean a = sender.hasPermission(helpFile.getPermissions()[0]);
                    final boolean b = "".equals(helpFile.getPermissions()[1]);
                    final boolean c = sender.hasPermission(helpFile.getPermissions()[1]);
                    final boolean d = helpFile.getGroup().equals("");
                    if(a && (b || c) && d) {
                        available++;
                    }
                }
                final int pages = (int)Math.ceil(available / 9.0);
                if(page > pages || page == 0) {
                    sender.sendMessage(Color.ERROR + "No help to display.");
                } else {
                    sender.sendMessage(Color.MESSAGE + "--- Help Page " + page + "/" + pages + " ---");
                    int skipped = 0;
                    int printed = 0;
                    for(final HelpFile helpFile : UDSPlugin.getHelpFiles()) {
                        final boolean a = sender.hasPermission(helpFile.getPermissions()[0]);
                        final boolean b = "".equals(helpFile.getPermissions()[1]);
                        final boolean c = sender.hasPermission(helpFile.getPermissions()[1]);
                        final boolean d = helpFile.getGroup().equals("");
                        if(a && (b || c) && d) {
                            if(skipped >= 9 * (page - 1)) {
                                sender.sendMessage(Color.COMMAND + helpFile.getUsage() + " - " + Color.TEXT + helpFile.getDescription());
                                printed++;
                            } else {
                                skipped++;
                            }
                            if(printed == 9) {
                                break;
                            }
                        }
                    }
                }
            } else {
                page = 1;
                if(args.length == 2 && args[1].matches("[0-9][0-9]*")) {
                    page = Integer.parseInt(args[1]);
                }
                int available = 0;
                for(final HelpFile helpFile : UDSPlugin.getHelpFiles()) {
                    final boolean a = sender.hasPermission(helpFile.getPermissions()[0]);
                    final boolean b = "".equals(helpFile.getPermissions()[1]);
                    final boolean c = sender.hasPermission(helpFile.getPermissions()[1]);
                    final boolean d = helpFile.getGroup().equalsIgnoreCase(tag);
                    if(a && (b || c) && d) {
                        available++;
                    }
                }
                final int pages = (int)Math.ceil(available / 9.0);
                if(page > pages || page == 0 || pages == 0) {
                    sender.sendMessage(Color.ERROR + "No help to display.");
                } else {
                    tag = tag.substring(0, 1).toUpperCase() + tag.substring(1).toLowerCase();
                    sender.sendMessage(Color.MESSAGE + "--- " + tag + " Help Page " + page + "/" + pages + " ---");
                    int skipped = 0;
                    int printed = 0;
                    for(final HelpFile helpFile : UDSPlugin.getHelpFiles()) {
                        final boolean a = sender.hasPermission(helpFile.getPermissions()[0]);
                        final boolean b = "".equals(helpFile.getPermissions()[1]);
                        final boolean c = sender.hasPermission(helpFile.getPermissions()[1]);
                        final boolean d = helpFile.getGroup().equalsIgnoreCase(tag);
                        if(a && (b || c) && d) {
                            if(skipped >= 9 * (page - 1)) {
                                sender.sendMessage(Color.COMMAND + helpFile.getUsage() + " - " + Color.TEXT + helpFile.getDescription());
                                printed++;
                            } else {
                                skipped++;
                            }
                            if(printed == 9) {
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            sender.sendMessage(Color.ERROR + "Wow, you can't even use the help command. Tell a Mod or Admin.");
        }
        return true;
    }
}
