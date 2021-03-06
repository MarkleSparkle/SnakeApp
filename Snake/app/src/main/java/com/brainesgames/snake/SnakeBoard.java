package com.brainesgames.snake;

import java.util.*;

public class SnakeBoard {
	static Random r=new Random();
	ArrayList<OrderedPair> snake;
	OrderedPair food;
	int width,height;
	boolean done;
	int direction;
	
	SnakeBoard(){
		width=18;
		height=32;
		newBoard();
	}
	
	void newBoard(){
		done=false;
        direction=0;
		snake=new ArrayList<OrderedPair>();
		snake.add(new OrderedPair(width/2,height/2));
		generateFood();
	}
	
	boolean inSnake(OrderedPair op){
		for(int i=0;i<snake.size();i++){
			if(op.equals(snake.get(i)))return true;
		}
		return false;
	}
	
	boolean inBounds(OrderedPair op){
		return op.getX()>=0 && op.getY()>=0 && op.getX()<width && op.getY()<height;
	}
	
	void generateFood(){
        //ensure board isnt full
        if(snake.size()<width*height) {
            do {
                food = new OrderedPair(r.nextInt(width), r.nextInt(height));
                //50% chance of a reroll if food on edge or corner
                if(food.getX()==0 || food.getY()==0 || food.getX()==width-1 || food.getY()==height-1){
                    if(r.nextInt(2)==0)generateFood();
                }
            } while (inSnake(food));
        }
	}
	
	void move(){
		if(!done){
			OrderedPair next;
			OrderedPair head=snake.get(0);
			//0 right 1 left 2 down 3 up
			switch(direction){
				case 0:
					next=new OrderedPair(head.getX()+1,head.getY());
					break;
				case 1:
					next=new OrderedPair(head.getX()-1,head.getY());
					break;
				case 2:
					next=new OrderedPair(head.getX(),head.getY()+1);
					break;
				default:
					next=new OrderedPair(head.getX(),head.getY()-1);
			}
			
			if(!inBounds(next) || inSnake(next)){
				done=true;
			}
			else if(next.equals(food)){
				snake.add(0,next);
				generateFood();
			}
			else{
				snake.remove(snake.size()-1);
				snake.add(0,next);
			}
		}
	}
	
	//makes sure next move wont move right back into self
	public boolean verify(int dir){
		if(snake.size()==1)return true;
		OrderedPair next;
		OrderedPair head=snake.get(0);
		//0 right 1 left 2 down 3 up
		switch(dir){
			case 0:
				next=new OrderedPair(head.getX()+1,head.getY());
				break;
			case 1:
				next=new OrderedPair(head.getX()-1,head.getY());
				break;
			case 2:
				next=new OrderedPair(head.getX(),head.getY()+1);
				break;
			default:
				next=new OrderedPair(head.getX(),head.getY()-1);
		}
		
		return !next.equals(snake.get(1));
	}

    public void setDirection(int dir){
        if(verify(dir))direction=dir;
    }
}
