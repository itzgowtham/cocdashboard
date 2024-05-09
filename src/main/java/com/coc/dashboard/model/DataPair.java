package com.coc.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DataPair<A, B, C> {
	private final A first;
	private final B second;
	private final C third;
}