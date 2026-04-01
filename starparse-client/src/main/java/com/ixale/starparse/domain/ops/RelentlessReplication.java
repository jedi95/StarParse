package com.ixale.starparse.domain.ops;

import com.ixale.starparse.domain.Combat;
import com.ixale.starparse.domain.Event;
import com.ixale.starparse.domain.NpcType;
import com.ixale.starparse.domain.Raid;
import com.ixale.starparse.domain.RaidBoss;
import com.ixale.starparse.domain.RaidBossName;
import com.ixale.starparse.domain.Raid.Npc;
import com.ixale.starparse.parser.Helpers;
import com.ixale.starparse.timer.BaseTimer;
import com.ixale.starparse.timer.TimerManager;

public class RelentlessReplication extends Raid {

	public RelentlessReplication() {
		super("Relentless Replication");
		
		RaidBoss.add(this, RaidBossName.XR53,
				new long[]{4824927605620736L}, // SM 8m
				null,
				new long[]{4847325860069376L}, // HM 8m
				null,
				null);
		
		npcs.put(4824927605620736L, new Npc(NpcType.boss_raid));
		npcs.put(4847325860069376L, new Npc(NpcType.boss_raid));
	}
}
