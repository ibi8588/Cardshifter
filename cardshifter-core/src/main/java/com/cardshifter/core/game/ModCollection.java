package com.cardshifter.core.game;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.zomis.cardshifter.ecs.usage.PhrancisGame;
import net.zomis.cardshifter.ecs.usage.PhrancisGameNewAttackSystem;
import net.zomis.cardshifter.ecs.usage.PhrancisGameWithSpells;

import com.cardshifter.ai.AIs;
import com.cardshifter.ai.ScoringAI;
import com.cardshifter.api.CardshifterConstants;
import com.cardshifter.core.modloader.DirectoryModLoader;
import com.cardshifter.core.modloader.Mod;
import com.cardshifter.core.modloader.ModNotLoadableException;
import com.cardshifter.modapi.ai.CardshifterAI;
import com.cardshifter.modapi.base.ECSMod;

public class ModCollection {

	private final Map<String, CardshifterAI> ais = new LinkedHashMap<>();
	private final Map<String, ECSMod> mods = new HashMap<>();
	
	public ModCollection() {
		ais.put("Loser", new ScoringAI(AIs.loser()));
		ais.put("Idiot", new ScoringAI(AIs.idiot()));
		ais.put("Medium", new ScoringAI(AIs.medium()));
		ais.put("Fighter", new ScoringAI(AIs.fighter()));
		
		mods.put(CardshifterConstants.VANILLA, new PhrancisGame());
		mods.put("New_Attack_Style", new PhrancisGameNewAttackSystem());
		mods.put("With spells", new PhrancisGameWithSpells());
	}
	
	public void loadExternal(Path directory) {
		String MOD_NAME = "cardshifter-mod-examples-java";
		DirectoryModLoader loader = new DirectoryModLoader(directory);
		try {
			Mod mod = loader.load(MOD_NAME);
			mods.put(mod.getName(), mod);
		} catch (ModNotLoadableException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Map<String, CardshifterAI> getAIs() {
		return Collections.unmodifiableMap(ais);
	}
	
	public Map<String, ECSMod> getMods() {
		return Collections.unmodifiableMap(mods);
	}
	
}