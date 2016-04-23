package com.brainesgames.ascii;
import java.io.*;
import java.util.*;
public class AsciiCanvas {
	private char[] canvas;
	private int width,height;
	
	public AsciiCanvas(int width,int height){
		this.width=width;
		this.height=height;
		canvas=new char[(width+1)*height];
		
		initLines();
		clear();
	}
	
	//sets character at position (x,y) to c
	public void set(int x,int y,char c){
		if(y>=0 && y<height && x>=0 && x<width)canvas[y*(width+1)+x]=c;
	}
	//gets character at position (x,y)
	public char get(int x,int y){
		if(y>=0 && y<height && x>=0 && x<width)return canvas[y*(width+1)+x];
		else return ' ';
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	//adds newlines to the canvas array, at positions ((width+1)*i-1)
	private void initLines(){
		for(int i=1;i<=height;i++){
			canvas[(width+1)*i-1]='\n';
		}
	}
	//clears with spaces
	public void clear(){clear(' ');}
	//sets entire canvas to that character
	public void clear(char c){
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				set(x,y,c);
			}
		}
	}
	//prints the canvas
	public void print(){
		System.out.print(canvas);
	}
	//returns canvas as a String
	public String toString(){
		return new String(canvas);
	}
	//draws the edges rectangle of rectangle starting at with width w and height h
	//vertical edges have x coords (x-1) and (x+w)
	//horizontal edges have y coords (y-1) and (y+h)
	public void drawRect(int x,int y,int w, int h){
		//test to make sure inbounds to optimize
		for(int i=x;i<x+w && i<width;i++){
			set(i,y-1,'_');
			set(i,y+h,'‾');
		}
		for(int i=y;i<y+h && i<height;i++){
			set(x-1,i,'|');
			set(x+w,i,'|');
		}
	}
	
	//draws the edges rectangle of rectangle starting at with width w and height h with char c
	//vertical edges have x coords (x-1) and (x+w)
	//horizontal edges have y coords (y-1) and (y+h)
	public void drawRect(int x,int y,int w, int h,char c){
		//test to make sure inbounds to optimize
		for(int i=x-1;i<=x+w && i<width;i++){
			set(i,y-1,c);
			set(i,y+h,c);
		}
		for(int i=y-1;i<=y+h && i<height;i++){
			set(x-1,i,c);
			set(x+w,i,c);
		}
	}
	
	public void fillRect(int x,int y,int w, int h,char c){
		for(int i=x;i<x+w && i<width;i++){
			for(int j=y;j<y+h && j<height;j++)set(i,j,c);
		}
	}
	
	public void fillEllipse(int cx,int cy,int rx,int ry,char c){
		for(int dx=-(rx-1);dx<=rx-1;dx++){
			int mdy=(int)Math.round(ry*Math.sqrt(1-(double)dx*dx/(rx*rx)));
			for(int dy=-(mdy-1);dy<=mdy-1;dy++)set(cx+dx,cy+dy,c);
		}
	}
	
	public void drawEllipse(int cx,int cy,int rx,int ry,char c){
		for(int dx=-rx;dx<=rx;dx++){
			int dy=(int)Math.round(ry*Math.sqrt(1-(double)dx*dx/(rx*rx)));
			set(cx+dx,cy+dy,c);
			set(cx+dx,cy-dy,c);
			if(dx==-rx){
				int mdy=(int)Math.round(ry*Math.sqrt(1-(double)((dx+1)*(dx+1))/(rx*rx)))-1;
				for(int deltay=-mdy;deltay<=mdy;deltay++)set(cx+dx,cy+deltay,c);
			}
			else if(dx==rx){
				int mdy=(int)Math.round(ry*Math.sqrt(1-(double)((dx+-1)*(dx-1))/(rx*rx)))-1;
				for(int deltay=-mdy;deltay<=mdy;deltay++)set(cx+dx,cy+deltay,c);
			}
		}
	}
	
	public void drawHLine(int x,int y, int l){
		if(y>=0 && y<height){
			for(int i=0;i<l && i+x<width;i++)set(x+i,y,'_');
		}
	}
	
	public void drawHLine(int x,int y, int l,boolean down){
		if(down)drawHLine(x,y,l);
		else drawHLine(x,y,l,'‾');
	}
	
	public void drawHLine(int x,int y, int l,char c){
		if(y>=0 && y<height){
			for(int i=0;i<l && i+x<width;i++)set(x+i,y,c);
		}
	}
	
	public void drawVLine(int x,int y, int l){
		if(x>=0 && x<width){
			for(int i=0;i<l && i+y<height;i++)set(x,y+i,'|');
		}
	}
	
	public void drawVLine(int x,int y, int l,char c){
		if(x>=0 && x<width){
			for(int i=0;i<l && i+y<height;i++)set(x,y+i,c);
		}
	}
	
	public void drawString(int x,int y,String s){
		if(y>=0 && y<height){
			for(int i=0;i<s.length() && i+x<width;i++)set(x+i,y,s.charAt(i));
		}
	}
	
	public void copy(AsciiCanvas ac,int x,int y,boolean includeWhitespace){
		if(includeWhitespace){
			for(int i=0;i<ac.getWidth() && i+x<width;i++){
				for(int j=0;j<ac.getHeight() && j+y<height;j++)set(i+x,j+y,ac.get(i,j));
			}
		}
		else{
			for(int i=0;i<ac.getWidth() && i+x<width;i++){
				for(int j=0;j<ac.getHeight() && j+y<height;j++){
					char val=ac.get(i,j);
					if(!Character.isWhitespace(val))set(i+x,j+y,val);
				}
			}
		}
	}
	
	public static AsciiCanvas load(String filename){
		try {
			return load(new BufferedReader(new FileReader(filename)));
		} catch (FileNotFoundException e) {
			System.out.println("404 File not Found: "+filename);
			e.printStackTrace();
		}
		return null;
	}
	
	public static AsciiCanvas load(File file){
		try {
			return load(new BufferedReader(new FileReader(file)));
		} catch (FileNotFoundException e) {
			System.out.println("404 File not Found: "+file);
			e.printStackTrace();
		}
		return null;
	}
	
	public static AsciiCanvas load(BufferedReader reader){
		ArrayList<String> lines=new ArrayList<String>();
		String line;
		try {
			while((line=reader.readLine())!=null)lines.add(line);
			reader.close();
			
			//height is amount of lines
			int h=lines.size();
			//width is maximum width of lines
			int w=lines.get(0).length();
			for(String s:lines){
				if(s.length()>w)w=s.length();
			}
			
			AsciiCanvas ac=new AsciiCanvas(w,h);
			for(int i=0;i<lines.size();i++){
				String l=lines.get(i);
				for(int j=0;j<l.length();j++){
					ac.set(j, i, l.charAt(j));
				}
			}
			
			return ac;
			
		} catch (IOException e) {
			System.out.println("IOException occured while loading file");
			e.printStackTrace();
		}
		return null;
	}
}