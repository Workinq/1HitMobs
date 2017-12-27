package kr.kieran.mobs;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Mobs extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this);

		final File file = new File(this.getDataFolder(), "config.yml");
		if (!file.exists()) {
			this.saveDefaultConfig();
		}
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!sender.hasPermission(this.getConfig().getString("MobKillPermission"))) {
			sender.sendMessage(this.getConfig().getString(Messages.format("NoPermission")));
		}

		if (cmd.getName().equalsIgnoreCase("mobs")) {
			if (args.length != 1) {
				sender.sendMessage(Messages.format(this.getConfig().getString("InvalidSyntax")));
				return true;
			}

			if (args[0].equalsIgnoreCase("reload")) {
				this.reloadConfig();
				sender.sendMessage(Messages.format(this.getConfig().getString("ConfigReloaded")));
				return true;
			}
		}

		return true;
	}

	@EventHandler
	public void onHit(final EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player))
			return;
		if (this.getConfig().getBoolean("EnableInstantKill")) {
			if (e.getCause().equals(DamageCause.ENTITY_ATTACK) && (!(e.getEntity() instanceof Player))) {
				e.setDamage(e.getDamage() * 1000);
				e.getEntity().sendMessage(Messages.format(this.getConfig().getString("InstantKill")));
			}
		}
	}

}
