package io.github.redstonefiend.bextra.commands;

/*
 * The MIT License
 *
 * Copyright 2015 Chris Courson.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
import io.github.redstonefiend.bextra.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Mute implements CommandExecutor {

    private final Main plugin;

    public Mute(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 2) {
            return false;
        }
        int duration;
        if (args.length == 2) {
            try {
                duration = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(ChatColor.RED + ex.getMessage().substring(18).replace("\"", "'") + " is not a number");
                return true;
            }
        } else {
            duration = plugin.muteMinutes;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "====== Mute List ======");
            for (Player player : plugin.muteList.keySet()) {
                long remaining = plugin.muteList.get(player) - System.currentTimeMillis();
                sender.sendMessage(String.format("%16s %tH:%tM:%tS remaining", player.getName(), remaining, remaining, remaining));
            }
        } else {
            Player player = plugin.getServer().getPlayerExact(args[0]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player " + args[0] + " not found.");
            } else {
                plugin.muteList.put(player, System.currentTimeMillis() + (duration * 60000));
                sender.sendMessage(ChatColor.YELLOW + "Player " + args[0] + " muted for " + duration + " minutes.");
            }
        }
        return true;
    }
}
