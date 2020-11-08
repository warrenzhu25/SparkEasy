package com.warren.backend.data;

import java.util.Arrays;
import java.util.Locale;

import com.vaadin.flow.shared.util.SharedUtil;

public enum RunState {
	Error, Dead, Killed, Success;

	public static String[] getFinishedState() {
		return Arrays.stream(RunState.values())
				.map(RunState::name)
				.map(String::toLowerCase)
				.toArray(String[]::new);
	}
}
