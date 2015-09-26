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
package io.github.redstonefiend.bextra;

import io.github.redstonefiend.bextra.commands.BExtra;
import io.github.redstonefiend.bextra.commands.Fly;
import io.github.redstonefiend.bextra.commands.Mute;
import io.github.redstonefiend.bextra.commands.NightVision;
import io.github.redstonefiend.bextra.commands.Unmute;
import io.github.redstonefiend.bextra.listeners.AsyncPlayerChat;
import io.github.redstonefiend.bextra.listeners.VehicleDestroy;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin implements Listener {

    public int muteMinutes = 0;
    public boolean protectBoats = false;
    public Map<Player, Long> muteList = new HashMap<>();

    @Override
    public void onEnable() {
        this.getDataFolder().mkdir();
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        this.muteMinutes = this.getConfig().getInt("mute_minutes");
        this.protectBoats = this.getConfig().getBoolean("protect_boats");

        getCommand("bExtra").setExecutor(new BExtra(this));
        getCommand("fly").setExecutor(new Fly(this));
        getCommand("nightvision").setExecutor(new NightVision(this));
        getCommand("mute").setExecutor(new Mute(this));
        getCommand("unmute").setExecutor(new Unmute(this));

        getServer().getPluginManager().registerEvents(new VehicleDestroy(this), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChat(this), this);

        this.getLogger().log(Level.INFO, "BExtra version {0} loaded.", this.getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    @Override
    public void saveConfig() {
        String str = this.getConfig().saveToString();
        StringBuilder sb = new StringBuilder(str);

        sb.insert(sb.indexOf("\nversion:") + 1,
                "\n# Configuration version used during upgrade. Do not change.\n");

        sb.insert(sb.indexOf("\nmute_minutes") + 1,
                "\n# The default number of minutes a player will remain muted.\n");

        sb.insert(sb.indexOf("\nprotect_boats") + 1,
                "\n# When true, protects boats from breakage unless deliberately broken.\n");

        final File cfg_file = new File(this.getDataFolder(), "config.yml");
        final String cfg_str = sb.toString();
        final Logger logger = this.getLogger();

        new BukkitRunnable() {
            @Override
            public void run() {
                cfg_file.delete();
                try (PrintWriter writer = new PrintWriter(cfg_file, "UTF-8")) {
                    cfg_file.createNewFile();
                    writer.write(cfg_str);
                    writer.close();
                } catch (IOException ex) {
                    logger.severe("Error saving configuration!");
                }
            }
        }.runTaskAsynchronously(this);
    }
}
