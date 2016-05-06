package com.brainesgames.snake;
import com.brainesgames.ascii.AsciiCanvas;
public class SnakeCanvas {
	AsciiCanvas canvas;
	SnakeBoard board;
	
	SnakeCanvas(SnakeBoard board){
		this.board=board;
		this.canvas=new AsciiCanvas(board.width*2+1,board.height+1);
	}
	
	void draw(){
		canvas.clear();
		//draw edges (top, bottom, left ,right)
		canvas.drawHLine(1, 0, board.width*2-1);
		canvas.drawHLine(1, board.height, board.width * 2 - 1);
		canvas.drawVLine(0, 1, board.height);
		canvas.drawVLine(board.width * 2, 1, board.height);
		
		//draw snake
		for(int i=1;i<board.snake.size();i++)drawCell(board.snake.get(i));
        drawHead(board.snake.get(0));
		//draw food
		canvas.set(board.food.getX()*2+1,board.food.getY()+1,'δ');
	}

	void drawHead(OrderedPair op){
        int x=op.getX()*2;
        int y=op.getY();
        canvas.set(x,y+1, '|');
        canvas.set(x+2,y+1, '|');
        canvas.set(x+1,y, '_');
        canvas.set(x+1,y+1, '*');
    }
	
	void drawCell(OrderedPair op){
		int x=op.getX()*2;
		int y=op.getY();
		canvas.set(x,y+1, '|');
		canvas.set(x+2,y+1, '|');
		canvas.set(x+1,y, '_');
		canvas.set(x+1,y+1, '_');

	}
	
/*	void fillCell(OrderedPair op, boolean food){
		if(food){
			canvas.fillRect(op.getX()*3, op.getY()*3, 2, 1,'*');
		}
		else{
			canvas.fillRect(op.getX()*3, op.getY()*3, 2, 1,'~');
		}
	}*/
}
