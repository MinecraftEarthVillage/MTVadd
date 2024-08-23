package top.earthvillage.mtv;

import nl.mtvehicles.core.events.VehicleEnterEvent;
import nl.mtvehicles.core.events.VehicleLeaveEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class MTVadd extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {

        if (!MTV载具在吗()) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "未检测到插件: MTVehicle, 插件卸载中（一定要两个一起用）");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // 注册事件监听器
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("事件监听器注册成功"); // 确保这行代码出现在控制台中
        // 加载配置文件
        saveDefaultConfig();
        this.getCommand("mtvaddreload").setExecutor(this);
    }
    //加载前置插件
    private boolean MTV载具在吗() {
        if (getServer().getPluginManager().getPlugin("MTVehicle") == null) {
            return false;
        }
        return false;
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        Player player = event.getPlayer();
        // 获取配置中的指令
        String command = getConfig().getString("enter-command");
executeCommands(player,getConfig().getStringList("上车指令"));
        getLogger().info(player.getName() + "上车了");

    }

    @EventHandler
    public void onVehicleLeave(VehicleLeaveEvent event) {
        Player player = event.getPlayer();
        // 获取配置中的指令
        executeCommands(player, getConfig().getStringList("下车指令"));
        getLogger().info(player + "下车了");
    }





    // 根据配置执行指令的方法
    private void executeCommands(Player player, java.util.List<String> commands) {
        for (String command : commands) {
            if (command.startsWith("op:")) {
                executeCommandAsOp(player, command.substring(3).trim());
            } else {
                Bukkit.dispatchCommand(player, command.replace("%玩家%", player.getName()));
            }
        }
    }


    // 以OP身份执行指令的方法
    private void executeCommandAsOp(Player player, String command) {
        boolean isOp = player.isOp();
        try {
            // 临时提升为OP权限
            player.setOp(true);
            // 执行指令
            Bukkit.dispatchCommand(player, command.replace("%玩家%", player.getName()));
        } finally {
            // 恢复原来的OP状态
            player.setOp(isOp);
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mtvreload")) {
            this.reloadConfig();
            sender.sendMessage("MTV-add 插件配置已重载！");
            return true;
        }
        return false;
    }
}

