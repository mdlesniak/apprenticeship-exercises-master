package model.patterns;

import java.util.ArrayList;

public class GroupOfDirectionalCorridors {
	private ArrayList<DirectionalCorridors> group;

	public GroupOfDirectionalCorridors() {
		group = new ArrayList<DirectionalCorridors>();
	}

	public DirectionalCorridors get(int i) {
		return group.get(i);
	}

	public void add(DirectionalCorridors list) {
		group.add(list);

	}

	public int size() {
		return group.size();
	}

}
