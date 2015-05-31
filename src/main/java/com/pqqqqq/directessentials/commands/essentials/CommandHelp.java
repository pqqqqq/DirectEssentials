package com.pqqqqq.directessentials.commands.essentials;

import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.util.pagination.PaginatedList;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.*;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Kevin on 2015-05-18.
 */
public class CommandHelp implements CommandExecutor {
    private DirectEssentials plugin;
    private final Map<CommandMapping, Text> paginationMap;

    private CommandHelp(DirectEssentials plugin) {
        this.plugin = plugin;

        CommandSource console = plugin.getGame().getServer().getConsole(); // Use console for usage/description retrieval
        PluginContainer pluginContainer = plugin.getGame().getPluginManager().fromInstance(plugin).get(); // Get the plugin container for DirectEssentials (this should never EVER be absent, lolwut?)
        Set<CommandMapping> commandMappingSet = plugin.getGame().getCommandDispatcher().getOwnedBy(pluginContainer); // Get a set of command mappings.

        // Alphabetic sorting
        this.paginationMap = new TreeMap<CommandMapping, Text>(new Comparator<CommandMapping>() {
            public int compare(CommandMapping o1, CommandMapping o2) {
                return o1.getPrimaryAlias().compareTo(o2.getPrimaryAlias());
            }
        });

        for (CommandMapping commandMapping : commandMappingSet) {
            CommandSpec commandSpec = (CommandSpec) commandMapping.getCallable(); // All commands are CommandSpecs.

            // If not, add the usage to the paginated list
            TextBuilder builder = Texts.builder();
            builder.append(commandSpec.getShortDescription(console).get()); // Append description
            builder.append(CommandMessageFormatting.NEWLINE_TEXT); // New line
            builder.append(Texts.of("/" + commandMapping.getPrimaryAlias() + " ")); // Append primary alias
            builder.append(commandSpec.getUsage(console)); // Append further usage
            builder.onClick(TextActions.runCommand("/" + commandMapping.getPrimaryAlias())); // Run command on click
            builder.onHover(TextActions.showText(Texts.of(TextColors.WHITE, "Run this command."))); // Display hover (tool-tip) text

            paginationMap.put(commandMapping, builder.build());
        }
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandHelp(plugin)).description(Texts.of(TextColors.AQUA, "A list of commands for this plugin."))
                .arguments(GenericArguments.optional(GenericArguments.integer(Texts.of("Page")), 1)).build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        PaginatedList paginatedList = new PaginatedList("/ess help");
        paginatedList.setHeader(Texts.of(paginatedList.fill(15, paginatedList.getPaginationType()), TextColors.AQUA, " DirectEssentials Help ", TextColors.WHITE, paginatedList.fill(15, paginatedList.getPaginationType())));
        paginatedList.displayLineNumbers(false); // No line numbers
        paginatedList.setItemsPerPage(6); // 6 commands per page

        for (Map.Entry<CommandMapping, Text> entry : this.paginationMap.entrySet()) {
            CommandSpec commandSpec = (CommandSpec) entry.getKey().getCallable(); // All commands are CommandSpecs.

            // Check the permission first. If they don't have the permission, don't show them this command.
            if (commandSpec.testPermission(src)) {
                paginatedList.add(entry.getValue()); // Add corresponding Text entry to paginated list.
            }
        }

        int page = args.<Integer>getOne("Page").get();

        // Checks bounds of pages
        if (page < 1) {
            src.sendMessage(Texts.of(TextColors.RED, "The page number must be greater than 0.")); // Lower bounds error
        } else if (page > paginatedList.getTotalPages()) {
            src.sendMessage(Texts.of(TextColors.RED, "There are only ", TextColors.WHITE, paginatedList.getTotalPages(), TextColors.RED, " page(s).")); // Upper bounds error
        } else {
            TextBuilder builder = Texts.builder();

            // Clear the current chat
            for (int i = 0; i < 20; i++) {
                builder.append(CommandMessageFormatting.NEWLINE_TEXT);
            }

            builder.append(paginatedList.getPage(page)); // Append the page
            src.sendMessage(builder.build()); // Build and send the message
        }
        return CommandResult.success();
    }
}
