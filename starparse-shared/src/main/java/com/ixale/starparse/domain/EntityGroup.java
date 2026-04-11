package com.ixale.starparse.domain;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

public enum EntityGroup {
	EFFECT,

	GUARD,
	DROP,
	MITIGATION,
	ABSORPTION,

	NONREDUCED_THREAT,

	GENERIC,
	;

	private static final EnumMap<EntityGroup, Set<Long>> groups = new EnumMap<>(EntityGroup.class);

	public static void addGuid(final EntityGroup group, long guid)
	{
		groups.computeIfAbsent(group, k -> new HashSet<>()).add(guid);
	}

	public static boolean containsGuid(final EntityGroup group, long guid) {
		final Set<Long> set = groups.get(group);
		return set != null && set.contains(guid);
	}
}
