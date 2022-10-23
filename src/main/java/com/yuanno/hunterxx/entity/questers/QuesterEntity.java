package com.yuanno.hunterxx.entity.questers;

import com.yuanno.hunterxx.api.Quest.Quest;
import com.yuanno.hunterxx.data.quest.IQuestData;
import com.yuanno.hunterxx.data.quest.QuestDataCapability;
import com.yuanno.hunterxx.init.ModQuests;
import com.yuanno.hunterxx.networking.PacketHandler;
import com.yuanno.hunterxx.networking.server.SOpenChatPromptScreenPacket;
import com.yuanno.hunterxx.networking.server.SSyncQuestDataPacket;
import com.yuanno.hunterxx.networking.server.SSyncTriggerQuest;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class QuesterEntity extends CreatureEntity {

    public Quest questPreviouslyDone;
    public boolean previousQuestObliged = false;
    public String denialSpeechPreviousQuest;
    public Quest questGiving;
    public String questSpeech;
    public String acceptanceSpeech;
    public String decliningSpeech;
    public String ongoingSpeech;
    public String doneSpeech;
    public List<Quest> questList = new ArrayList<Quest>();
    public QuesterEntity(EntityType type, World world)
    {
        super(type, world);
    }



    @Override
    public boolean removeWhenFarAway(double d)
    {
        return false;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 30)
                .add(Attributes.ARMOR, 20)
                .add(Attributes.MAX_HEALTH, 200)
                .add(Attributes.FOLLOW_RANGE, 100)
                .add(Attributes.MOVEMENT_SPEED, 2);
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand)
    {
        if (hand != Hand.MAIN_HAND)
            return ActionResultType.PASS;
        if (!player.level.isClientSide)
        {
            IQuestData questData = QuestDataCapability.get(player); // TODO issue here, maybe???
            Quest[] quests = questData.getInProgressQuests();
            for (int i = 0; i < quests.length; i++)
            {
                if (questData.getInProgressQuest(i) != null && questData.getInProgressQuest(i).triggerCompleteEvent(player) && questList.contains(questData.getInProgressQuest(i)))
                {
                    questData.addFinishedQuest(quests[i]);
                    questData.removeInProgressQuest(quests[i]);
                    PacketHandler.sendTo(new SSyncQuestDataPacket(player.getId(), questData), player);
                }
            }
            PacketHandler.sendTo(new SOpenChatPromptScreenPacket(this.getId()), player);
        }
        return ActionResultType.PASS;
    }
}
