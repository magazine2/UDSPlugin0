package com.undeadscythes.udsplugin;

/**
 * A plugin command.
 * @author UndeadScythes
 */
public class HelpFile {
    private String usage;
    private String description;
    private String permission1;
    private String permission2;
    private String group;

    /**
     *
     * @param name Name of the command, what follows the /.
     * @param usage Brief example of usage.
     * @param description Description of the command.
     * @param permission Permission node required to use the command.
     */
    public HelpFile(final String usage, final String description, final String permission1, final String permission2, final String group) {
        this.usage = usage;
        this.description = description;
        this.permission1 = permission1;
        this.permission2 = permission2;
        this.group = group;
    }

    /**
     *
     * @return Brief example of usage.
     */
    public String getUsage() {
        return usage;
    }

    /**
     *
     * @return Description of the command.
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return Permission node required to use the command.
     */
    public String[] getPermissions() {
        return new String[]{permission1, permission2};
    }

    public String getGroup() {
        return group;
    }
}
