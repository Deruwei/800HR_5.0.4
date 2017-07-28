package com.hr.ui.adapter;

public class ArrayWheelAdapter<T> implements WheelAdapter {

	public static final int DEFAULT_LENGTH = -1;
	// items
		private T items[];
		// length
		private int length;
	public ArrayWheelAdapter(T items[] ,int length){
		this.items = items;
		this.length = length;
	}
	public ArrayWheelAdapter(T items[]){
		this(items, DEFAULT_LENGTH);
	}
	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return items.length;
	}
	@Override
	public String getItem(int index) {
		// TODO Auto-generated method stub
		if (index >= 0 && index< items.length) {
			return items[index].toString();
		}
		return null;
	}
	@Override
	public int getMaximumLength() {
		// TODO Auto-generated method stub
		return length;
	}
}
