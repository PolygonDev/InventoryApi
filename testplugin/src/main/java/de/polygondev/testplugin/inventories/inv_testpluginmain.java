package de.polygondev.testplugin.inventories;

import de.polygondev.inventoryapi.inventory.Inventory;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class inv_testpluginmain extends Inventory
{
    private int page = 0;
    public int getPage() {
        return page;
    }

    public inv_testpluginmain(String title, String name) {
        super(title, name);
        main_page();
    }

    private void cleanInv() {
        this.removeAll();
        this.removeAllExecutors();
    }

    private void main_page() {
        page = 1;
        this.setTitle("§2Mechanika§r  §4Page 1");
        ItemStack is = new ItemStack(Material.IRON_BARS);

        ItemMeta ism = is.getItemMeta();
        ism.setLocalizedName("Gate");
        ism.displayName(Component.text(ChatColor.BLUE + "Gate"));

        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(ChatColor.RED + "Erstelle ein Tor!"));
        ism.lore(lore);

        is.setItemMeta(ism);

        this.setItem("1:5", is);

        ItemStack soc = new ItemStack(Material.STONE);
        ItemMeta meta_soc = soc.getItemMeta();
        meta_soc.displayName(Component.text("§3Stone of §6Change"));
        ArrayList<Component> soc_lore = new ArrayList<>();
        soc_lore.add(Component.text("§cCan be moved!"));
        meta_soc.lore(soc_lore);
        soc.setItemMeta(meta_soc);
        this.setItem("1:1", soc);

        this.setAreaSlotProtection("1:1", "1:9", false);
        this.setAreaSlotProtection("3:1", "3:9", true);
        this.setExecutor("1:5", this::exec_gate);
        this.setAllowPlayerInventory(false);
    }

    private void exec_main_page(Player player, ClickType clickType)
    {
        if (clickType.isLeftClick())
        {
            //Todo: hier kommt die logik hin die es speichert und beim close wenn der spieler schließt
            saveItemsInRow(player);

            player.sendMessage(ChatColor.GREEN + "Alles zurück auf Anfang!");
            this.cleanInv();
            this.main_page();
            this.setRows(1);
        }
    }

    private void exec_gate(Player player, ClickType clickType)
    {
        if (clickType.isLeftClick()) {
            page = 2;
            this.setTitle("§2Gate§r  §4Page 2");

            player.sendMessage(ChatColor.GREEN + "Das ist ein Gate, EY!!!");
            this.cleanInv();

            ItemStack is = new ItemStack(Material.BARRIER);

            ItemMeta ism = is.getItemMeta();
            ism.setLocalizedName("Gate");
            ism.displayName(Component.text(ChatColor.BLUE + "Zurück"));

            ArrayList<Component> lore = new ArrayList<>();
            lore.add(Component.text(ChatColor.RED + "Gehe zurück zur Main-Page"));
            ism.lore(lore);

            is.setItemMeta(ism);

            this.setItem("1:5", is);
            this.setExecutor("1:5", this::exec_main_page);
            this.setAreaSlotProtection("1:1", "1:9", true);
            this.setAreaSlotProtection("3:1", "3:9", false);
            this.setAllowPlayerInventory(true);
            this.setRows(3);

            addItemsFromRow();
        }
    }

    public void addItemsFromRow() {
        int z = 1;
        if (row != null && row.length > 0) {
            for (ItemStack isxs : row)
            {
                if (z <= 9)
                {
                    this.setItem("3:" + z, isxs);
                    z++;
                }
            }
        }
    }

    private void saveItemsInRow(Player player) {
        org.bukkit.inventory.Inventory invx = player.getOpenInventory().getTopInventory();

        int x = 0;
        for (int i = 0; i < invx.getSize(); i++) {
            if (!isSlotProtected(i)) {
                ItemStack cache = invx.getItem(i);

                row[x] = cache;
                if (cache == null || cache.getType().equals(Material.AIR)) {
                    row[x] = null;
                }
                x++;
            }
        }
    }

    @Override
    public void clickEvent(InventoryClickEvent inventoryClickEvent)
    {

    }

    ItemStack[] row = new ItemStack[9];

    @Override
    public void closeEvent(InventoryCloseEvent inventoryCloseEvent)
    {
        //Todo: aufräumen
        if (!inventoryCloseEvent.getReason().equals(InventoryCloseEvent.Reason.PLUGIN) && getPage() == 2)
        {
            saveItemsInRow((Player) inventoryCloseEvent.getPlayer());
        }
    }
}
