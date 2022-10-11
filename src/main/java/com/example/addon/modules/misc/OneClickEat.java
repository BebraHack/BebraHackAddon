package com.example.addon.modules.misc;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.entity.player.FinishUsingItemEvent;
import meteordevelopment.meteorclient.events.entity.player.StoppedUsingItemEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.ItemListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class OneClickEat extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();


    // General
    private final Setting<List<Item>> foodList = sgGeneral.add(new ItemListSetting.Builder()
        .name("white-list")
        .description("Which items you can one click eat.")
        .filter(Item::isFood)
        .build()
    );

    private final Setting<Boolean> usePotions = sgGeneral.add(new BoolSetting.Builder()
        .name("use-potions")
        .description("Allows you to also use potions.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> onlyGround = sgGeneral.add(new BoolSetting.Builder()
        .name("only-on-ground")
        .description("Only allows you to one click eat on ground.")
        .defaultValue(false)
        .build()
    );


    public OneClickEat() {
        super(Addon.misc, "one-click-eat", "Allows you to eat a consumable with one click.");
    }

    private boolean isUsing;
    private boolean pressed;

    private Item currentItem;

    private boolean isPotato() {
        return mc.player.getMainHandStack().getItem() == Items.POTATO || mc.player.getOffHandStack().getItem() == Items.POTATO;
    }

    private boolean isCarrot() {
        return mc.player.getMainHandStack().getItem() == Items.CARROT || mc.player.getOffHandStack().getItem() == Items.CARROT;
    }

    private boolean canEatFull() {
        return mc.player.getMainHandStack().getItem() == Items.ENCHANTED_GOLDEN_APPLE || mc.player.getOffHandStack().getItem() == Items.ENCHANTED_GOLDEN_APPLE
            || mc.player.getMainHandStack().getItem() == Items.GOLDEN_APPLE || mc.player.getOffHandStack().getItem() == Items.GOLDEN_APPLE
            || mc.player.getMainHandStack().getItem() == Items.CHORUS_FRUIT || mc.player.getOffHandStack().getItem() == Items.CHORUS_FRUIT
            || mc.player.getMainHandStack().getItem() == Items.HONEY_BOTTLE || mc.player.getOffHandStack().getItem() == Items.HONEY_BOTTLE
            || mc.player.getMainHandStack().getItem() == Items.MILK_BUCKET || mc.player.getOffHandStack().getItem() == Items.MILK_BUCKET
            || mc.player.getMainHandStack().getItem() == Items.SUSPICIOUS_STEW || mc.player.getOffHandStack().getItem() == Items.SUSPICIOUS_STEW;

    }

    private boolean canPlantBerry(BlockPos pos){
        return ((mc.player.getMainHandStack().getItem() == Items.GLOW_BERRIES || mc.player.getOffHandStack().getItem() == Items.GLOW_BERRIES)
            && !mc.world.getBlockState(pos).isOf(Blocks.AIR))

            || ((mc.player.getMainHandStack().getItem() == Items.SWEET_BERRIES || mc.player.getOffHandStack().getItem() == Items.SWEET_BERRIES)
            && (mc.world.getBlockState(pos).isOf(Blocks.GRASS_BLOCK)
            || mc.world.getBlockState(pos).isOf(Blocks.DIRT)
            || mc.world.getBlockState(pos).isOf(Blocks.PODZOL)
            || mc.world.getBlockState(pos).isOf(Blocks.COARSE_DIRT)
            || mc.world.getBlockState(pos).isOf(Blocks.FARMLAND)));
    }

    private boolean isGround(BlockPos pos){
        return mc.player.getMainHandStack().getItem() instanceof BlockItem && !mc.world.getBlockState(pos).isOf(Blocks.AIR);
    }

    private boolean isFarmland(BlockPos pos) {
        return mc.world.getBlockState(pos).isOf(Blocks.FARMLAND);
    }

    private boolean mainHandFull(){

        // maybe add armor item?
        return  mc.player.getMainHandStack().getItem() == Items.TRIDENT
            || mc.player.getMainHandStack().getItem() == Items.BOW
            || mc.player.getMainHandStack().getItem() == Items.CROSSBOW
            || mc.player.getMainHandStack().getItem() instanceof GoatHornItem
            || mc.player.getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE
            || mc.player.getMainHandStack().getItem() instanceof BucketItem
            || mc.player.getMainHandStack().getItem() == Items.WRITABLE_BOOK
            || mc.player.getMainHandStack().getItem() == Items.WRITTEN_BOOK
            || mc.player.getMainHandStack().getItem() == Items.FIREWORK_ROCKET
            || mc.player.getMainHandStack().getItem() == Items.LEAD
            || mc.player.getMainHandStack().getItem() == Items.NAME_TAG
            || mc.player.getMainHandStack().getItem() instanceof BoatItem
            || mc.player.getMainHandStack().getItem() instanceof MinecartItem
            || mc.player.getMainHandStack().getItem() == Items.SADDLE
            || mc.player.getMainHandStack().getItem() == Items.ARMOR_STAND
            || mc.player.getMainHandStack().getItem() == Items.SHIELD
            || mc.player.getMainHandStack().getItem() == Items.SHEARS
            || mc.player.getMainHandStack().getItem() == Items.ITEM_FRAME
            || mc.player.getMainHandStack().getItem() instanceof BannerItem
            || mc.player.getMainHandStack().getItem() == Items.PAINTING
            || mc.player.getMainHandStack().getItem() == Items.SPYGLASS
            || mc.player.getMainHandStack().getItem() == Items.FISHING_ROD
            || mc.player.getMainHandStack().getItem() == Items.FLINT_AND_STEEL
            || mc.player.getMainHandStack().getItem() instanceof SpawnEggItem
            || mc.player.getMainHandStack().getItem() == Items.TURTLE_EGG;
    }

    private boolean canChangeBlock(BlockPos pos){
        return ((mc.world.getBlockState(pos).isOf(Blocks.GRASS_BLOCK) // Shovel
            || mc.world.getBlockState(pos).isOf(Blocks.DIRT)
            || mc.world.getBlockState(pos).isOf(Blocks.PODZOL))
            && (mc.player.getMainHandStack().getItem() instanceof ShovelItem
            || mc.player.getOffHandStack().getItem() instanceof ShovelItem))

            || ((mc.world.getBlockState(pos).isOf(Blocks.ACACIA_LOG) // Axe
            || mc.world.getBlockState(pos).isOf(Blocks.BIRCH_LOG)
            || mc.world.getBlockState(pos).isOf(Blocks.DARK_OAK_LOG)
            || mc.world.getBlockState(pos).isOf(Blocks.JUNGLE_LOG)
            || mc.world.getBlockState(pos).isOf(Blocks.MANGROVE_LOG)
            || mc.world.getBlockState(pos).isOf(Blocks.OAK_LOG)
            || mc.world.getBlockState(pos).isOf(Blocks.SPRUCE_LOG)
            || mc.world.getBlockState(pos).isOf(Blocks.ACACIA_WOOD)
            || mc.world.getBlockState(pos).isOf(Blocks.BIRCH_WOOD)
            || mc.world.getBlockState(pos).isOf(Blocks.DARK_OAK_WOOD)
            || mc.world.getBlockState(pos).isOf(Blocks.JUNGLE_WOOD)
            || mc.world.getBlockState(pos).isOf(Blocks.MANGROVE_WOOD)
            || mc.world.getBlockState(pos).isOf(Blocks.OAK_WOOD)
            || mc.world.getBlockState(pos).isOf(Blocks.SPRUCE_WOOD))
            && (mc.player.getMainHandStack().getItem() instanceof AxeItem
            || mc.player.getOffHandStack().getItem() instanceof AxeItem));
    }

    private boolean isRideable(Entity hit) {
        return hit instanceof MinecartEntity
            || hit instanceof BoatEntity
            || hit instanceof HorseEntity
            || hit instanceof DonkeyEntity
            || hit instanceof MuleEntity
            || hit instanceof SkeletonHorseEntity
            || hit instanceof ZombieHorseEntity
            || hit instanceof PigEntity
            || hit instanceof StriderEntity;
    }

    private boolean canFeed(Entity hit) {
        return ((hit instanceof HorseEntity
            || hit instanceof DonkeyEntity
            || hit instanceof MuleEntity)
            && ((mc.player.getMainHandStack().getItem() == Items.APPLE || mc.player.getOffHandStack().getItem() == Items.APPLE)
            || (mc.player.getMainHandStack().getItem() == Items.ENCHANTED_GOLDEN_APPLE || mc.player.getOffHandStack().getItem() == Items.ENCHANTED_GOLDEN_APPLE)
            || (mc.player.getMainHandStack().getItem() == Items.GOLDEN_APPLE || mc.player.getOffHandStack().getItem() == Items.GOLDEN_APPLE)
            || (mc.player.getMainHandStack().getItem() == Items.GOLDEN_CARROT || mc.player.getOffHandStack().getItem() == Items.GOLDEN_CARROT)))
            || ((hit instanceof PigEntity)
            && ((mc.player.getMainHandStack().getItem() == Items.BEETROOT || mc.player.getOffHandStack().getItem() == Items.BEETROOT)
            || (mc.player.getMainHandStack().getItem() == Items.CARROT || mc.player.getOffHandStack().getItem() == Items.CARROT)
            || (mc.player.getMainHandStack().getItem() == Items.POTATO || mc.player.getOffHandStack().getItem() == Items.POTATO)));
    }

    private boolean canPlaceCrystal(BlockPos pos){
        return (mc.world.getBlockState(pos).isOf(Blocks.OBSIDIAN) || mc.world.getBlockState(pos).isOf(Blocks.BEDROCK)) && mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL;
    }

    private boolean isPlayer(Entity hit) {
        return  hit instanceof PlayerEntity;
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        assert mc.player != null;
        assert mc.world != null;

        if (onlyGround.get() && !mc.player.isOnGround()) return;

        if(!mc.options.useKey.isPressed()) {
            pressed = false;
        }

        if (mc.options.useKey.isPressed() && !isUsing && !pressed) {
            pressed = true;

            if (mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                if (BlockUtils.isClickable(mc.world.getBlockState(((BlockHitResult) mc.crosshairTarget).getBlockPos()).getBlock())) return;
                if (canPlantBerry(((BlockHitResult) mc.crosshairTarget).getBlockPos())) return;
                if ((isPotato() || isCarrot()) && isFarmland(((BlockHitResult) mc.crosshairTarget).getBlockPos())) return;
            }

            if (mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
                if (isRideable(((EntityHitResult) mc.crosshairTarget).getEntity()) || canFeed(((EntityHitResult) mc.crosshairTarget).getEntity())) return;
            }

            if (!mc.player.getHungerManager().isNotFull() && !canEatFull()) return;

            //main hand
            if (foodList.get().contains(mc.player.getMainHandStack().getItem()) || (usePotions.get() && mc.player.getMainHandStack().getItem() instanceof PotionItem)){
                eat();
            }

            //offhand
            else if (foodList.get().contains(mc.player.getOffHandStack().getItem()) || (usePotions.get() && mc.player.getOffHandStack().getItem() instanceof PotionItem)){
                if (!mainHandFull()){

                    if (mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                        if (isGround(((BlockHitResult) mc.crosshairTarget).getBlockPos())) return;
                        if (canChangeBlock(((BlockHitResult) mc.crosshairTarget).getBlockPos())) return;
                        if (canPlaceCrystal(((BlockHitResult) mc.crosshairTarget).getBlockPos())) return;
                    }


                    if (mc.player.getOffHandStack().getItem() != currentItem) stopIfUsing();

                    eat();
                }
            }
        }

        if (isUsing) mc.options.useKey.setPressed(true);
    }

    private void eat(){
        mc.options.useKey.setPressed(true);
        isUsing = true;
    }

    @Override
    public void onDeactivate() {
        stopIfUsing();
    }

    @EventHandler
    private void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof UpdateSelectedSlotC2SPacket) {
            stopIfUsing();
        }
    }

    @EventHandler
    private void onFinishUsingItem(FinishUsingItemEvent event) {
        stopIfUsing();
    }

    @EventHandler
    private void onStoppedUsingItem(StoppedUsingItemEvent event) {
        stopIfUsing();
    }

    private void stopIfUsing() {
        if (isUsing) {
            mc.options.useKey.setPressed(false);
            isUsing = false;
        }
    }
}
