package com.brainesgames.snake;

public class OrderedPair {
	private int x,y;
	OrderedPair(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	public boolean equals(OrderedPair op){
		return op.x==x && op.y==y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
