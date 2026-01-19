package rem.fog_monster.common.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;

/**
 * Basic hostile mob. The fog effect is driven on the client by checking for this entity type.
 */
public class FogMonsterEntity extends Monster {

    /**
     * Server-side boss bar (vanilla-style). Players will see it while they track this entity.
     */
    private final ServerBossEvent bossEvent = new ServerBossEvent(
            this.getDisplayName(),
            BossEvent.BossBarColor.PURPLE,
            BossEvent.BossBarOverlay.PROGRESS
    );

    public FogMonsterEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 10;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        // Keep the boss bar in sync with current health.
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        // Ensure the boss bar name matches the mob's display name.
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.15D, true));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hurt = super.doHurtTarget(target);
        if (hurt && target instanceof LivingEntity living) {
            // Extra shove so the hit *feels* heavy
            double dx = target.getX() - this.getX();
            double dz = target.getZ() - this.getZ();
            float f = Mth.sqrt((float)(dx * dx + dz * dz));
            if (f > 0.0001F) {
                living.push(dx / f * 0.7D, 0.25D, dz / f * 0.7D);
            }
        }
        return hurt;
    }

public static AttributeSupplier.Builder createAttributes() {
    // "Hard hitter" tuning: big damage + sturdy + some knockback
    return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 60.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.30D)
            .add(Attributes.ARMOR, 6.0D)
            .add(Attributes.ATTACK_DAMAGE, 14.0D)
            .add(Attributes.ATTACK_KNOCKBACK, 2.0D)
            .add(Attributes.FOLLOW_RANGE, 48.0D);
}

}
