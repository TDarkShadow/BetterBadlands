package com.teamaurora.better_badlands.core.other;

import com.teamaurora.better_badlands.common.world.biome.BetterBadlandsBiomeFeatures;
import com.teamaurora.better_badlands.core.BetterBadlands;
import com.teamaurora.better_badlands.core.registry.BetterBadlandsFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = BetterBadlands.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BetterBadlandsEvents {

    @SubscribeEvent
    public void onSaplingGrowTree (SaplingGrowTreeEvent event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) event.getWorld();
            BlockPos pos = event.getPos();
            BlockState state = world.getBlockState(pos);
            Random rand = event.getRand();
            if (state.getBlock() == Blocks.DARK_OAK_SAPLING) {
                if (
                        !(world.getBlockState(pos.north()).getBlock() == Blocks.DARK_OAK_SAPLING && world.getBlockState(pos.east()).getBlock() == Blocks.DARK_OAK_SAPLING && world.getBlockState(pos.north().east()).getBlock() == Blocks.DARK_OAK_SAPLING) &&
                                !(world.getBlockState(pos.north()).getBlock() == Blocks.DARK_OAK_SAPLING && world.getBlockState(pos.west()).getBlock() == Blocks.DARK_OAK_SAPLING && world.getBlockState(pos.north().west()).getBlock() == Blocks.DARK_OAK_SAPLING) &&
                                !(world.getBlockState(pos.south()).getBlock() == Blocks.DARK_OAK_SAPLING && world.getBlockState(pos.east()).getBlock() == Blocks.DARK_OAK_SAPLING && world.getBlockState(pos.south().east()).getBlock() == Blocks.DARK_OAK_SAPLING) &&
                                !(world.getBlockState(pos.south()).getBlock() == Blocks.DARK_OAK_SAPLING && world.getBlockState(pos.west()).getBlock() == Blocks.DARK_OAK_SAPLING && world.getBlockState(pos.south().west()).getBlock() == Blocks.DARK_OAK_SAPLING)
                ) {
                    event.setResult(Event.Result.DENY);
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    ConfiguredFeature<BaseTreeFeatureConfig, ?> configuredFeature = BetterBadlandsFeatures.SMALL_DARK_OAK_TREE.get().withConfiguration(BetterBadlandsBiomeFeatures.SMALL_DARK_OAK_CONFIG);
                    if (!configuredFeature.func_236265_a_(world, world.func_241112_a_(), world.getChunkProvider().getChunkGenerator(), rand, pos)) {
                        world.setBlockState(pos, state);
                    }
                }
            }
        }
    }

    //not subscribe event yet: awaiting approval
    public void onItemRightClick(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stackIn = event.getItemStack();
        BlockPos posIn = event.getPos();
        BlockState stateIn = event.getWorld().getBlockState(posIn);
        if (stackIn.getItem() instanceof AxeItem) {
            if (stateIn.getBlock().getTags().contains(BlockTags.SAPLINGS.getName())) {
                event.getWorld().playSound(null, posIn, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
                event.getWorld().setBlockState(posIn, Blocks.DEAD_BUSH.getDefaultState());
            }
        }
    }
}
