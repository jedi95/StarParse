package com.ixale.starparse.parser;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ixale.starparse.domain.Combat;
import com.ixale.starparse.domain.Event;
import com.ixale.starparse.domain.Raid.Mode;
import com.ixale.starparse.domain.Raid.Size;
import com.ixale.starparse.service.impl.Context;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-context.xml")
public class CombatReviveTest {

	@Test
	public void testBestiaCombat() throws Exception {
		final Context context = new Context();
		final Parser p = new Parser(context);
		// The file name is used by the parser to determine the combat date
		p.setCombatLogFile(new File("combat_2026-04-08_02_34_59_194326.txt"));

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream("/combat_2026-04-08_02_34_59_194326.txt")))) {
			String line;
			while ((line = reader.readLine()) != null) {
				p.parseLogLine(line);
			}
		}

		List<Combat> combats = p.getCombats();
		// The parser might not have closed the final combat if it doesn't see a separator.
		// However, with Bestia's death, it should be closed.
		Combat c = null;
		if (!combats.isEmpty()) {
			c = combats.get(0);
		} else {
			c = p.getCurrentCombat();
		}

		assertNotNull("Combat not found", c);
		assertNotNull("Boss not detected", c.getBoss());
		assertEquals("Dread Master Bestia", c.getBoss().getName());
		assertEquals(Mode.SM, c.getBoss().getMode());
		assertEquals(Size.Eight, c.getBoss().getSize());

		assertNotNull("Combat end time not set", c.getTimeTo());
		long durationMs = c.getTimeTo() - c.getTimeFrom();
		double durationSeconds = durationMs / 1000.0;
		// 6m 20s = 380s (+- 1s)
		assertEquals(380.0, durationSeconds, 1.0);

		long totalDamageDealt = 0;
		long totalDamageTaken = 0;

		for (Event e : p.getEvents()) {
			// Only sum damage within this combat's timeframe
			if (e.getTimestamp() < c.getTimeFrom() || e.getTimestamp() > c.getTimeTo()) {
				continue;
			}
			if (Helpers.isEffectDamage(e) && e.getValue() != null) {
				if ("Auldria".equals(e.getSource().getName())) {
					totalDamageDealt += e.getValue();
				}
				if ("Auldria".equals(e.getTarget().getName())) {
					totalDamageTaken += e.getValue();
				}
			}
		}

		double dps = totalDamageDealt / durationSeconds;
		double dtps = totalDamageTaken / durationSeconds;

		// 42794 (+- 500)
		assertEquals(42794.0, dps, 500.0);
		// 20871 (+- 250)
		assertEquals(20871.0, dtps, 250.0);
	}
}
