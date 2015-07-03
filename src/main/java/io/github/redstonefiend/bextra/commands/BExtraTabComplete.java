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
package io.github.redstonefiend.bextra.commands;

import io.github.redstonefiend.bextra.Main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class BExtraTabComplete implements TabCompleter {

    private final Main plugin;

    public BExtraTabComplete(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // /<command> [ver[sion] | mute [minutes] | protectboats [true | false]]
        List<String> list = new ArrayList<>();
        switch (args.length) {
            case 1:
                list.addAll(Arrays.asList("version", "mute", "protectboats", "clearmutelist"));
                break;
            case 2:
                if (args[0].equalsIgnoreCase("mute")) {
                    list.add(String.valueOf(plugin.muteMinutes));
                } else if (args[0].equalsIgnoreCase("protectboats")) {
                    list.add(String.valueOf(!plugin.protectBoats));
                    list.add(String.valueOf(plugin.protectBoats));
                }
        }
        return list;
    }
}
