package com.brainesgames.snake;
import com.brainesgames.ascii.AsciiCanvas;
public class SnakeCanvas {
	AsciiCanvas canvas;
	SnakeBoard board;
	
	SnakeCanvas(SnakeBoard board){
		this.board=board;
		this.canvas=new AsciiCanvas(board.width*2+1,board.height*2+1);
	}
	
	void draw(){
		canvas.clear();
		//draw edges (top, bottom, left ,right)
		canvas.drawHLine(1, 0, board.width*2-1);
		canvas.drawHLine(1, board.height * 2, board.width * 2 - 1);
		canvas.drawVLine(0, 1, board.height*2);
		canvas.drawVLine(board.width * 2, 1, board.height * 2);

        //draw food
        canvas.set(board.food.getX()*2,board.food.getY()*2+1,'/');
        canvas.set(board.food.getX() * 2, board.food.getY() * 2 + 2, '\\');
        canvas.set(board.food.getX() * 2 + 1, board.food.getY() * 2, '|');
        canvas.set(board.food.getX()*2+1,board.food.getY()*2+1,'*');
        canvas.set(board.food.getX() * 2 + 1, board.food.getY() * 2 + 2, '_');
        canvas.set(board.food.getX() * 2 + 2, board.food.getY() * 2 + 1, '\\');
        canvas.set(board.food.getX()*2+2,board.food.getY()*2+2,'/');
		
		//draw snake
		for(int i=1;i<board.snake.size();i++)drawCell(board.snake.get(i));
        drawHead(board.snake.get(0));
	}

	void drawHead(OrderedPair op){
        int x=op.getX()*2;
        int y=op.getY()*2;

        if(board.direction==0){
            canvas.set(x, y + 1, '|');
            canvas.set(x + 2, y + 1, '>');
            canvas.set(x, y + 2, '|');
            canvas.set(x + 2, y + 2, '>');
            canvas.set(x + 1, y, '_');
            canvas.set(x + 1, y + 2, '_');
        }
        else if(board.direction==1) {
            canvas.set(x, y + 1, '<');
            canvas.set(x + 2, y + 1, '|');
            canvas.set(x, y + 2, '<');
            canvas.set(x + 2, y + 2, '|');
            canvas.set(x + 1, y, '_');
            canvas.set(x + 1, y + 2, '_');
        }
        else if(board.direction==2) {
            canvas.set(x, y + 1, '|');
            canvas.set(x + 2, y + 1, '|');
            canvas.set(x, y + 2, '|');
            canvas.set(x + 2, y + 2, '|');
            canvas.set(x + 1, y, '_');
            canvas.set(x + 1, y + 3, 'v');
        }
        else if(board.direction==3){
            canvas.set(x, y + 1, '|');
            canvas.set(x + 2, y + 1, '|');
            canvas.set(x, y + 2, '|');
            canvas.set(x + 2, y + 2, '|');
            canvas.set(x + 1, y, '^');
            canvas.set(x + 1, y + 2, '_');
        }
    }
	
	void drawCell(OrderedPair op){
		int x=op.getX()*2;
		int y=op.getY()*2;
        canvas.set(x,y+1, '|');
        canvas.set(x+2,y+1, '|');
        canvas.set(x,y+2, '|');
        canvas.set(x+2,y+2, '|');
        canvas.set(x+1,y, '_');
        canvas.set(x+1,y+2, '_');

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
