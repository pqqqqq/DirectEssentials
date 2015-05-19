package com.pqqqqq.directessentials.commands.essentials;

import com.google.common.collect.Sets;
import com.pqqqqq.directessentials.DirectEssentials;
import com.pqqqqq.directessentials.util.pagination.PaginatedList;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.*;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandExecutor;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.util.Comparator;
import java.util.Set;

/**
 * Created by Kevin on 2015-05-18.
 */
public class CommandHelp implements CommandExecutor {
    private DirectEssentials plugin;

    private CommandHelp(DirectEssentials plugin) {
        this.plugin = plugin;
    }

    public static CommandSpec build(DirectEssentials plugin) {
        return CommandSpec.builder().executor(new CommandHelp(plugin)).description(Texts.of(TextColors.AQUA, "A list of commands for this plugin."))
                .arguments(GenericArguments.optional(GenericArguments.integer(Texts.of("Page")))).build();
    }

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        PluginContainer pluginContainer = plugin.getGame().getPluginManager().fromInstance(plugin).get(); // Get the plugin container for DirectEssentials (this should never EVER be absent, lolwut?)
        Set<CommandMapping> commandMappingSet = plugin.getGame().getCommandDispatcher().getOwnedBy(pluginContainer); // Get a set of command mappings.

        // Sort alphabetically
        Set<CommandMapping> sortedCommandMappingSet = Sets.newTreeSet(new Comparator<CommandMapping>() {
            public int compare(CommandMapping o1, CommandMapping o2) {
                return o1.getPrimaryAlias().compareTo(o2.getPrimaryAlias());
            }
        });
        sortedCommandMappingSet.addAll(commandMappingSet);

        PaginatedList paginatedList = new PaginatedList("/ess help");
        paginatedList.setHeader(Texts.of(paginatedList.fill(15, paginatedList.getPaginationType()), TextColors.AQUA, " DirectEssentials Help ", TextColors.WHITE, paginatedList.fill(15, paginatedList.getPaginationType())));
        paginatedList.displayLineNumbers(false); // No line numbers
        paginatedList.setItemsPerPage(6); // 6 commands per page

        for (CommandMapping commandMapping : sortedCommandMappingSet) {
            CommandSpec commandSpec = (CommandSpec) commandMapping.getCallable(); // All commands are CommandSpecs.

            // Check the permission first. If they don't have the permission, don't show them this command.
            if (!commandSpec.testPermission(src)) {
                continue;
            }

            // If not, add the usage to the paginated list
            TextBuilder builder = Texts.builder();
            builder.append(commandSpec.getShortDescription(src).get()); // Append description
            builder.append(CommandMessageFormatting.NEWLINE_TEXT); // New line
            builder.append(Texts.of("/" + commandMapping.getPrimaryAlias() + " ")); // Append primary alias
            builder.append(commandSpec.getUsage(src)); // Append further usage

            paginatedList.add(builder.build());
        }

        // Pages
        int page = 1;
        if (args.hasAny("Page")) {
            page = args.<Integer>getOne("Page").get();
        }

        // Checks bounds of pages
        if (page < 1) {
            src.sendMessage(Texts.of(TextColors.RED, "The page number must be greater than 0.")); // Lower bounds error
        } else if (page > paginatedList.getTotalPages()) {
            src.sendMessage(Texts.of(TextColors.RED, "There are only ", TextColors.WHITE, paginatedList.getTotalPages(), TextColors.RED, " page(s)."));
        } else {
            src.sendMessage(paginatedList.getPage(page)); // Display the page
        }
        return CommandResult.success();
    }
}
