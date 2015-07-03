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
import java.util.Iterator;
import java.util.Map;
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
            sender.sendMessage(ChatColor.YELLOW + "---------" + ChatColor.WHITE + " Mute List " + ChatColor.YELLOW + "----------------------------");
            for (Iterator<Map.Entry<Player, Long>> it = plugin.muteList.entrySet().iterator(); it.hasNext();) {
                Map.Entry<Player, Long> entry = it.next();
                if (entry.getValue() > System.currentTimeMillis()) {
                    long remaining = entry.getValue() - System.currentTimeMillis();
                    sender.sendMessage(String.format("%-16s %2d hours, %2d minutes, %2d seconds remaining",
                            entry.getKey().getName(), remaining / 3600000 % 24, remaining / 60000 % 60, remaining / 1000 % 60));
                } else {
                    it.remove();
                }
            }
        } else {
            Player player = plugin.getServer().getPlayerExact(args[0]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player " + args[0] + " not found.");
            } else {
                plugin.muteList.put(player, System.currentTimeMillis() + ((long) duration * 60000L));
                sender.sendMessage(ChatColor.YELLOW + "Player " + player.getName() + " muted for " + duration + " minute" + (duration > 1 ? "s" : "") + ".");
            }
        }
        return true;
    }
}
