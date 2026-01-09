package me.kodysimpson.vaulteconomy.news.command.auction;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AuctionSell {

    public void sell(Player p, String priceStr) {

        if (VaultEconomy.getInstance().getPvpManager().isInPvp(p)) {
            p.sendMessage(AuctionMessages.get("pvp-block"));
            return;
        }

        AuctionInventory.cleanup();

        if (!AuctionInventory.canSell(p)) {
            p.sendMessage(AuctionMessages.get("no-slots"));
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (Exception e) {
            p.sendMessage(AuctionMessages.get("invalid-price"));
            return;
        }

        ItemStack hand = p.getInventory().getItemInMainHand();
        if (hand.getType() == Material.AIR) {
            p.sendMessage(AuctionMessages.get("no-item-hand"));
            return;
        }

        String category = getCategory(hand.getType());
        AuctionItem item = new AuctionItem(p.getUniqueId(), hand, price, category);

        AuctionInventory.addItem(item);
        p.getInventory().setItemInMainHand(null);

        p.sendMessage(AuctionMessages.get("item-listed",
                "player", p.getName(),
                "price", String.valueOf((int) price),
                "category", category));

        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.2f);
    }

    private String getCategory(Material mat) {
        String n = mat.name();
        if (n.contains("SWORD") || n.contains("BOW") || n.contains("ARMOR")) return "ОРУЖИЕ";
        if (n.contains("PICKAXE") || n.contains("SHOVEL")) return "ИНСТРУМЕНТЫ";
        return "РЕСУРСЫ";
    }
}