package com.northeastern.numad.virtualtreasure;

import java.util.ArrayList;
import java.util.List;

public class TreasureList {

	public static ArrayList<Treasure> listGlobal = new ArrayList<Treasure>();
	private ArrayList<Treasure> list = new ArrayList<Treasure>();

	public ArrayList<Treasure> getList() {
		return list;
	}

	public void setList(ArrayList<Treasure> list) {
		this.list = list;
	}
	
}
