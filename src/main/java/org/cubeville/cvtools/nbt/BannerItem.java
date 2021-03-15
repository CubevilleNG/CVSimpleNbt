package org.cubeville.cvtools.nbt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

public class BannerItem {

    private ItemStack itemStack = null;
    private BannerMeta bannerMeta = null;

    private HashSet<Material> bannerMaterials = new HashSet<>(Arrays.asList(Material.BLACK_BANNER,
                                                           Material.BLACK_WALL_BANNER,
                                                           Material.BLUE_BANNER,
                                                           Material.BLUE_WALL_BANNER,
                                                           Material.BROWN_BANNER,
                                                           Material.BROWN_WALL_BANNER,
                                                           Material.CYAN_BANNER,
                                                           Material.CYAN_WALL_BANNER,
                                                           Material.GRAY_BANNER,
                                                           Material.GRAY_WALL_BANNER,
                                                           Material.GREEN_BANNER,
                                                           Material.GREEN_WALL_BANNER,
                                                           Material.LIGHT_BLUE_BANNER,
                                                           Material.LIGHT_BLUE_WALL_BANNER,
                                                           Material.LIGHT_GRAY_BANNER,
                                                           Material.LIGHT_GRAY_WALL_BANNER,
                                                           Material.LIME_BANNER,
                                                           Material.LIME_WALL_BANNER,
                                                           Material.MAGENTA_BANNER,
                                                           Material.MAGENTA_WALL_BANNER,
                                                           Material.ORANGE_BANNER,
                                                           Material.ORANGE_WALL_BANNER,
                                                           Material.PINK_BANNER,
                                                           Material.PINK_WALL_BANNER,
                                                           Material.PURPLE_BANNER,
                                                           Material.PURPLE_WALL_BANNER,
                                                           Material.RED_BANNER,
                                                           Material.RED_WALL_BANNER,
                                                           Material.WHITE_BANNER,
                                                           Material.WHITE_WALL_BANNER,
                                                           Material.YELLOW_BANNER,
                                                           Material.YELLOW_WALL_BANNER));

    public BannerItem(ItemStack item) {
        if (bannerMaterials.contains(item.getType()) || item.getType() == Material.SHIELD) {
            itemStack = item;
            bannerMeta = (BannerMeta) item.getItemMeta();
        }
    }
	
    public void setBaseColor(DyeColor color) {
        bannerMeta.setBaseColor(color);
    }
	
    public void addPattern(Pattern pattern) {
        bannerMeta.addPattern(pattern);
    }
	
    public void addPattern(DyeColor color, PatternType type) {
        Pattern pattern = new Pattern(color, type);
        bannerMeta.addPattern(pattern);
    }
	
    public void removePattern(int i) {
        if (i > bannerMeta.getPatterns().size())
            i = bannerMeta.getPatterns().size();
        else if (i < 0)
            i = 0;
		
        bannerMeta.removePattern(i);
    }
	
    public void setPattern(int i, Pattern pattern) {
        if (i > bannerMeta.getPatterns().size())
            i = bannerMeta.getPatterns().size();
        else if (i < 0)
            i = 0;
		
        bannerMeta.setPattern(i, pattern);
    }
	
    public void setPattern(int i, DyeColor color, PatternType type) {
        bannerMeta.setPattern(i, new Pattern(color, type));
    }
	
    public void insertPattern(int i, Pattern pattern) {
        if (i > bannerMeta.getPatterns().size())
            i = bannerMeta.getPatterns().size();
        else if (i < 0)
            i = 0;
		
        List<Pattern> patterns = bannerMeta.getPatterns();
        patterns.add(i, pattern);
        bannerMeta.setPatterns(patterns);
    }
	
    public void insertPattern(int i, DyeColor color, PatternType type) {
        insertPattern(i, new Pattern(color, type));
    }
	
    public void clearPatterns() {
        int i = bannerMeta.numberOfPatterns();
        for (int x = 0; x < i; x++) {
            bannerMeta.removePattern(x);
        }
    }
	
    public void setShield() {
        itemStack.setType(Material.SHIELD);
    }
	
    public void setBanner() {
        itemStack.setType(Material.LEGACY_STANDING_BANNER);
    }
	
    public ItemStack asItemStack() {
        itemStack.setItemMeta(bannerMeta);
        return itemStack;
    }
	
}
