package com.hrsoftware.comum;

import java.util.List;

@FunctionalInterface
public interface ConsumerList<T> {
	
	void accept(List<T> objeto);

}
