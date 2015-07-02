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

public class Fly implements CommandExecutor {

    private final Main plugin;

    public Fly(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ((!(sender instanceof Player)) && (args.length == 0)) {
            sender.sendMessage(ChatColor.RED + "ERROR: fly cannot be called from console without specifying player.");
            return false;
        }
        if (args.length > 1) {
            return false;
        }
        Player player;
        if (args.length == 1) {
            if (!sender.hasPermission("bextra.fly.other")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission.");
                return true;
            }
            player = plugin.getServer().getPlayerExact(args[0]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }
        } else {
            player = (Player) sender;
        }
        if (!player.getAllowFlight()) {
            player.setAllowFlight(true);
            player.sendMessage(ChatColor.YELLOW + "Flight has been enabled for you. Double-press SPACE to fly.");
        } else {
            player.setAllowFlight(false);
            player.sendMessage(ChatColor.YELLOW + "Flight has been disabled for you.");
        }
        return true;
    }
}
