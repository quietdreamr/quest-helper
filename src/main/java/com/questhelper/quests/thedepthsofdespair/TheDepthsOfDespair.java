/*
 * Copyright (c) 2021, Zoinkwiz
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.questhelper.quests.thedepthsofdespair;

import com.questhelper.ItemCollections;
import com.questhelper.QuestDescriptor;
import com.questhelper.QuestHelperQuest;
import com.questhelper.Zone;
import com.questhelper.banktab.BankSlotIcons;
import com.questhelper.panel.PanelDetails;
import com.questhelper.questhelpers.BasicQuestHelper;
import com.questhelper.requirements.FavourRequirement;
import com.questhelper.requirements.ItemRequirement;
import com.questhelper.requirements.QuestRequirement;
import com.questhelper.requirements.Requirement;
import com.questhelper.requirements.SkillRequirement;
import com.questhelper.requirements.conditional.ConditionForStep;
import com.questhelper.requirements.conditional.Conditions;
import com.questhelper.requirements.conditional.InInstanceCondition;
import com.questhelper.requirements.conditional.ItemRequirementCondition;
import com.questhelper.requirements.conditional.ZoneCondition;
import com.questhelper.steps.ConditionalStep;
import com.questhelper.steps.DetailedQuestStep;
import com.questhelper.steps.ItemStep;
import com.questhelper.steps.NpcStep;
import com.questhelper.steps.ObjectStep;
import com.questhelper.steps.QuestStep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.runelite.api.Favour;
import net.runelite.api.ItemID;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.QuestState;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;

@QuestDescriptor(
	quest = QuestHelperQuest.THE_DEPTHS_OF_DESPAIR
)
public class TheDepthsOfDespair extends BasicQuestHelper
{
	// Recommended
	ItemRequirement weapon, dramenStaff, foodIfLowLevel, superEnergyOrStamina, xericsTalisman, skillsNecklace,
		varlamoreEnvoy, royalAccordOfTwill;

	ConditionForStep inVineryHouse, inArceuusLibrary, hasTheVarlamoreEnvoy, inCaves, inFirstAreaOfCaves,
		inSecondAreaOfCaves, inThirdAreaOfCaves, downstairsInCaves;

	QuestStep talkToLordKandur, talkToChefOlivia, talkToGalana, findTheVarlamoreEnvoy, readTheVarlamoreEnvoy,
		enterCrabclawCaves, goThroughCrevice, stepOverSteppingStones, climbPastRocks, enterTunnelEntrance,
		talkToArturHosidius, killSandSnake, searchChest, talkToLordKandurAgain;

	Zone houseWestOfVinery, arceuusLibraryF1, arceuusLibraryF2, arceuusLibraryF3, crabclawCaves, crabclawCavesPart1A,
		crabclawCavesPart1B, crabclawCavesPart2, crabclawCavesPart3, crabclawCavesDownstairs;

	@Override
	public Map<Integer, QuestStep> loadSteps()
	{
		loadZones();
		setupRequirements();
		setupConditions();
		setupSteps();

		Map<Integer, QuestStep> steps = new HashMap<>();

		ConditionalStep goTalkToLordKandur = new ConditionalStep(this, talkToLordKandur, inVineryHouse);
		steps.put(0, goTalkToLordKandur);

		ConditionalStep goTalkToChefOlivia = new ConditionalStep(this, talkToChefOlivia, inVineryHouse);
		steps.put(1, goTalkToChefOlivia);

		ConditionalStep goTalkToGalana = new ConditionalStep(this, talkToGalana, inArceuusLibrary);
		steps.put(2, goTalkToGalana);

		ConditionalStep findAndReadTheVarlamoreEnvoy = new ConditionalStep(this, findTheVarlamoreEnvoy);
		findAndReadTheVarlamoreEnvoy.addStep(hasTheVarlamoreEnvoy, readTheVarlamoreEnvoy);
		findAndReadTheVarlamoreEnvoy.addStep(inArceuusLibrary, talkToGalana);
		steps.put(3, findAndReadTheVarlamoreEnvoy);

		ConditionalStep stepEnterCrabclawCaves = new ConditionalStep(this, enterCrabclawCaves);
		steps.put(4, stepEnterCrabclawCaves);

		ConditionalStep stepEnterLowerCaves = new ConditionalStep(this, enterCrabclawCaves);
		stepEnterLowerCaves.addStep(inFirstAreaOfCaves, goThroughCrevice);
		stepEnterLowerCaves.addStep(inSecondAreaOfCaves, stepOverSteppingStones);
		stepEnterLowerCaves.addStep(inThirdAreaOfCaves, climbPastRocks);
		stepEnterLowerCaves.addStep(inCaves, enterTunnelEntrance);
		steps.put(6, stepEnterLowerCaves);

		ConditionalStep stepTalkToArturHosidius = new ConditionalStep(this, enterCrabclawCaves);
		stepTalkToArturHosidius.addStep(inFirstAreaOfCaves, goThroughCrevice);
		stepTalkToArturHosidius.addStep(inSecondAreaOfCaves, stepOverSteppingStones);
		stepTalkToArturHosidius.addStep(inThirdAreaOfCaves, climbPastRocks);
		stepTalkToArturHosidius.addStep(inCaves, enterTunnelEntrance);
		stepTalkToArturHosidius.addStep(downstairsInCaves, talkToArturHosidius);
		steps.put(7, stepTalkToArturHosidius);

		ConditionalStep stepKillSandSnake = new ConditionalStep(this, enterCrabclawCaves);
		stepKillSandSnake.addStep(inFirstAreaOfCaves, goThroughCrevice);
		stepKillSandSnake.addStep(inSecondAreaOfCaves, stepOverSteppingStones);
		stepKillSandSnake.addStep(inThirdAreaOfCaves, climbPastRocks);
		stepKillSandSnake.addStep(inCaves, enterTunnelEntrance);
		stepKillSandSnake.addStep(downstairsInCaves, killSandSnake);
		steps.put(8, stepKillSandSnake);

		ConditionalStep stepSearchChest = new ConditionalStep(this, enterCrabclawCaves);
		stepSearchChest.addStep(inFirstAreaOfCaves, goThroughCrevice);
		stepSearchChest.addStep(inSecondAreaOfCaves, stepOverSteppingStones);
		stepSearchChest.addStep(inThirdAreaOfCaves, climbPastRocks);
		stepSearchChest.addStep(inCaves, enterTunnelEntrance);
		stepSearchChest.addStep(downstairsInCaves, searchChest);
		steps.put(9, stepSearchChest);

		ConditionalStep stepReturnToLordKandurHosidius = new ConditionalStep(this, talkToLordKandurAgain);
		steps.put(10, stepReturnToLordKandurHosidius);

		return steps;
	}

	public void setupRequirements()
	{
		xericsTalisman = new ItemRequirement("Xeric's Talisman", ItemID.XERICS_TALISMAN);
		skillsNecklace = new ItemRequirement("Skill's necklace", ItemCollections.getSkillsNecklaces());
		superEnergyOrStamina = new ItemRequirement("Super Energy or Stamina potions", -1, -1);

		dramenStaff = new ItemRequirement("Access to Fairy Rings", ItemID.DRAMEN_STAFF);
		dramenStaff.addAlternates(ItemID.LUNAR_STAFF);

		foodIfLowLevel = new ItemRequirement("Food (if low level)", -1, -1);
		foodIfLowLevel.setDisplayItemId(BankSlotIcons.getFood());

		weapon = new ItemRequirement("A Weapon", -1, -1);
		weapon.setDisplayItemId(BankSlotIcons.getCombatGear());

		varlamoreEnvoy = new ItemRequirement("Varlamore Envoy", ItemID.VARLAMORE_ENVOY);
		varlamoreEnvoy.setHighlightInInventory(true);

		royalAccordOfTwill = new ItemRequirement("Royal Accord of Twill", ItemID.ROYAL_ACCORD_OF_TWILL);
	}

	public void loadZones()
	{
		houseWestOfVinery = new Zone(new WorldPoint(1810, 3550, 0), new WorldPoint(1820, 3560, 0));
		arceuusLibraryF1 = new Zone(new WorldPoint(1607, 3831, 0), new WorldPoint(1658, 3784, 0));
		arceuusLibraryF2 = new Zone(new WorldPoint(1607, 3831, 1), new WorldPoint(1658, 3784, 1));
		arceuusLibraryF3 = new Zone(new WorldPoint(1607, 3831, 2), new WorldPoint(1658, 3784, 2));
		crabclawCaves = new Zone(new WorldPoint(1644, 9851, 0), new WorldPoint(1728, 9793, 0));
		crabclawCavesPart1A = new Zone(new WorldPoint(1644, 9851, 0), new WorldPoint(1722, 9823, 0));
		crabclawCavesPart1B = new Zone(new WorldPoint(1665, 9823, 0), new WorldPoint(1707, 9813, 0));
		crabclawCavesPart2 = new Zone(new WorldPoint(1703, 9821, 0), new WorldPoint(1724, 9792, 0));
		crabclawCavesPart3 = new Zone(new WorldPoint(1688, 9804, 0), new WorldPoint(1703, 9794, 0));
		crabclawCavesDownstairs = new Zone(new WorldPoint(1670, 9763, 0), new WorldPoint(1701, 9742, 0));
	}

	public void setupConditions()
	{
		inVineryHouse = new ZoneCondition(houseWestOfVinery);
		inArceuusLibrary = new ZoneCondition(arceuusLibraryF1, arceuusLibraryF2, arceuusLibraryF3);
		inCaves = new ZoneCondition(crabclawCaves);
		inFirstAreaOfCaves = new ZoneCondition(crabclawCavesPart1A, crabclawCavesPart1B);
		inSecondAreaOfCaves = new ZoneCondition(crabclawCavesPart2);
		inThirdAreaOfCaves = new ZoneCondition(crabclawCavesPart3);

		ZoneCondition cavesDownstairs = new ZoneCondition(crabclawCavesDownstairs);
		downstairsInCaves = new Conditions(cavesDownstairs, new InInstanceCondition());

		hasTheVarlamoreEnvoy = new ItemRequirementCondition(varlamoreEnvoy);
	}

	public void setupSteps()
	{
		talkToLordKandur = new NpcStep(this, NpcID.LORD_KANDUR_HOSIDIUS, new WorldPoint(1782, 3572, 0),
			"Talk to Lord Kandur Hosidius in the house west of the vinery.");
		talkToLordKandur.addDialogSteps("Anything I can help you with?", "Yes.");

		talkToChefOlivia = new NpcStep(this, NpcID.CHEF_OLIVIA, new WorldPoint(1776, 3567, 0),
			"Speak to Chef Olivia in Lord Kandur's kitchen.");

		talkToGalana = new NpcStep(this, NpcID.GALANA, new WorldPoint(1649, 3824, 0),
			"Speak to Galana.");

		findTheVarlamoreEnvoy = new ItemStep(this,
			"Find and read the Varlamore Envoy. Ask Galana for the approximate location if you can't find it.",
			varlamoreEnvoy);

		readTheVarlamoreEnvoy = new DetailedQuestStep(this, "Read the Varlamore Envoy.", varlamoreEnvoy);

		enterCrabclawCaves = new ObjectStep(this, ObjectID.CAVE_31690, new WorldPoint(1645, 3450, 0),
			"Enter the Crabclaw Caves.");

		goThroughCrevice = new ObjectStep(this, ObjectID.CREVICE_31695, new WorldPoint(1711, 9823, 0),
			"Enter the crevice. Kill a sandcrab for the easy diary while you're here.");
		stepOverSteppingStones = new ObjectStep(this, ObjectID.STEPPING_STONE_31699, new WorldPoint(1706, 9800, 0),
			"Cross the stepping stones.");
		climbPastRocks = new ObjectStep(this, ObjectID.ROCKS_31697, new WorldPoint(1688, 9801, 0),
			"Climb the rocks.");
		enterTunnelEntrance = new ObjectStep(this, ObjectID.TUNNEL_ENTRANCE_31692, new WorldPoint(1672, 9800, 0),
			"Climb down the tunnel at the end.");

		talkToArturHosidius = new NpcStep(this, NpcID.ARTUR_HOSIDIUS_7899, "Speak to Artur Hosidius.");

		killSandSnake = new NpcStep(this, NpcID.SAND_SNAKE_7903, "Kill the Sand Snake.");

		searchChest = new ObjectStep(this, ObjectID.CHEST_31703, "Search the chest.");

		talkToLordKandurAgain = new NpcStep(this, NpcID.LORD_KANDUR_HOSIDIUS, new WorldPoint(1782, 3572, 0),
			"Return to Lord Kandur Hosidius and talk to him.");
	}

	@Override
	public List<ItemRequirement> getItemRecommended()
	{
		return Arrays.asList(skillsNecklace, xericsTalisman, foodIfLowLevel, superEnergyOrStamina, dramenStaff);
	}

	@Override
	public List<String> getCombatRequirements()
	{
		return Collections.singletonList("Sand Snake (level 36)");
	}

	@Override
	public List<Requirement> getGeneralRequirements()
	{
		ArrayList<Requirement> req = new ArrayList<>();
		req.add(new QuestRequirement(QuestHelperQuest.CLIENT_OF_KOUREND, QuestState.FINISHED));
		req.add(new FavourRequirement(Favour.HOSIDIUS, 20));
		req.add(new SkillRequirement(Skill.AGILITY, 18, false));
		return req;
	}

	@Override
	public List<PanelDetails> getPanels()
	{
		List<PanelDetails> allSteps = new ArrayList<>();

		allSteps.add(new PanelDetails("Starting off",
			Arrays.asList(talkToLordKandur, talkToChefOlivia)));

		allSteps.add(new PanelDetails("The Envoy to Varlamore",
			Arrays.asList(talkToGalana, findTheVarlamoreEnvoy, readTheVarlamoreEnvoy)));

		allSteps.add(new PanelDetails("The Crabclaw Caves",
			Arrays.asList(enterCrabclawCaves, goThroughCrevice, stepOverSteppingStones, climbPastRocks,
				enterTunnelEntrance, talkToArturHosidius, killSandSnake, searchChest)
			, foodIfLowLevel, weapon));

		allSteps.add(new PanelDetails("Finishing up",
			Collections.singletonList(talkToLordKandurAgain)));

		return allSteps;
	}
}