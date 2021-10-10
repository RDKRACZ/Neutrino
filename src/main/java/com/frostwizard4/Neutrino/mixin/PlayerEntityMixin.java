package com.frostwizard4.Neutrino.mixin;

import com.frostwizard4.Neutrino.misc.CheckHolding;
import com.frostwizard4.Neutrino.NeutrinoMain;
import com.frostwizard4.Neutrino.PlayerEntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccess {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    private float neutrino$boomPowerCounter = 0;
    private float neutrino$soulPouchCounter = 0;

    @Override
    public float neutrino$getPowerCount() {
        return neutrino$boomPowerCounter;
    }

    public float neutrino$getSoulPouchCount() { return neutrino$soulPouchCounter; }

    public void neutrino$setPowerCount(float newValue) {
        neutrino$boomPowerCounter = newValue;
    }

    public void neutrino$setSoulPouchCount(float newValue) {
        neutrino$soulPouchCounter = newValue;
    }

    @Shadow
    protected abstract boolean isOnSoulSpeedBlock();

    @Shadow
    public abstract Iterable<ItemStack> getItemsHand();

    @Shadow public abstract void sendMessage(Text message, boolean actionBar);

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void neutrino$checkHolding(CallbackInfo ci) {
        if (getMainHandStack().isOf(NeutrinoMain.HARVESTER) || getMainHandStack().isOf(NeutrinoMain.LIGHTNING_ROD_ARTIFACT)) {
            if (world.isClient()) {
                //CheckHolding.sendChatMessage(neutrino$boomPowerCounter);
                sendMessage(Text.of("Soul Pouch Level: " + (int) neutrino$boomPowerCounter / 10), true);

                //ServerPlayerEntity!!
            }
        }
        if (isOnSoulSpeedBlock()) {
            if (getMainHandStack().isOf(NeutrinoMain.SOUL_POUCH)) {
                displaySoulSpeedEffects();
                if (neutrino$soulPouchCounter >= 3000) {
                    neutrino$soulPouchCounter = 3000;
                    if (world.isClient()) {
                        //CheckHolding.sendBundleChatMessage(neutrino$soulPouchCounter);
                        sendMessage(Text.of("Soul Pouch Level: " + (int) neutrino$soulPouchCounter / 10), true);

                    }
                } else {
                    if (getMainHandStack().isOf(NeutrinoMain.SOUL_POUCH)) {
                        neutrino$soulPouchCounter++;
                        if (world.isClient()) {
                            //CheckHolding.sendBundleChatMessage(neutrino$soulPouchCounter);
                            sendMessage(Text.of("Soul Pouch Level: " + (int) neutrino$soulPouchCounter / 10), true);

                        }
                    }
                }
            }
            if (getMainHandStack().isOf(NeutrinoMain.HARVESTER) || getMainHandStack().isOf(NeutrinoMain.LIGHTNING_ROD_ARTIFACT) || getMainHandStack().isOf(NeutrinoMain.SOUL_HEALER)) {
                displaySoulSpeedEffects();
                if (neutrino$boomPowerCounter >= 1500) {
                    neutrino$boomPowerCounter = 1500;
                    if (world.isClient()) {
                        //CheckHolding.sendChatMessage(neutrino$boomPowerCounter);
                        sendMessage(Text.of("Soul Pouch Level: " + (int) neutrino$boomPowerCounter / 10), true);

                    }
                } else {
                    neutrino$boomPowerCounter++;
                    if (world.isClient()) {
                        //CheckHolding.sendChatMessage(neutrino$boomPowerCounter);
                        sendMessage(Text.of("Soul Pouch Level: " + (int) neutrino$boomPowerCounter / 10), true);
                    }
                }
            }
        }
    }
}





