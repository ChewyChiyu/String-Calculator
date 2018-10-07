
import java.util.ArrayList;
import java.util.Stack;

public class Calculator{

	private final String[] symbols = new String[]{"-","+","/","*","^","sin","cos","tan","(",")"};

	public void calculate(String func){
		compute(parse(func));
	}

	public void compute(ArrayList<String> func){
		Stack<String> input = new Stack<String>();
		Stack<Double> output = new Stack<Double>();
		boolean compress = false;
		String prev = func.get(0);
		for(String symbol : func){
			if(!compress)
				compress = ((isNumber(prev)||prev.equals(")"))&&symbol.equals("("));
			prev = symbol;
			if(isNumber(symbol)){
				output.add(Double.parseDouble(symbol));
			}else if(symbol.equals("(")){
				input.add(symbol);
			}else if(symbol.equals(")")){
				while(!input.peek().equals("(")){
					step(input,output);
				}
				input.pop();
				if(compress){
					printStack(output);
					printStack(input);
					System.out.println("test");
					compress = false;
					if(output.size()>1){
						output.add(output.pop()*output.pop());
					}
				}
				
			}else{
				while(!input.isEmpty()&&!isHigher(symbol,input.peek())){
					step(input,output);
				}
				input.add(symbol);
			}
		}	
		while(!input.isEmpty()||output.size()>1){ step(input,output); }
		System.out.println(output.peek());
	}



	public void printStack(Stack s){
		Object[] arr = s.toArray();
		for(Object d : arr){
			System.out.print( d + " ");
		} 
		System.out.println();
	}

	public void step(Stack<String> input, Stack<Double> output){
		printStack(output);
		printStack(input);
		double d1 = 0, d2 = 0;
		if(input.isEmpty() && output.size() < 2){ return; }
		else if(input.isEmpty()){
			d1 = output.pop();
			d2 = output.pop();
			output.add(d1*d2);
		}else if(input.peek().equals("sin")||input.peek().equals("cos")||input.peek().equals("tan")){
			String symbol = input.pop();
			switch(symbol){
				case "sin": output.add(Math.sin(output.pop()));
				break;
				case "cos": output.add(Math.cos(output.pop()));
				break;
				case "tan": output.add(Math.tan(output.pop()));
				break;
				default: break;
			}
		}
		else{
			String symbol = input.pop();
			d1 = output.pop();
			d2 = output.pop();
			switch(symbol){
				case "-": output.add(d2-d1);
				break;
				case "+": output.add(d2+d1);
				break;
				case "/": output.add(d2/d1);
				break;
				case "*": output.add(d2*d1);
				break;
				default: break;
			}
		}
	}

	public boolean isHigher(String s1, String s2){
		return getValue(s1) > getValue(s2);
	}


	public int getValue(String s1){
		switch(s1){
			case "-": case "+": return 1;
			case "/": case "*": return 2;
			case "^": return 3;
			case "sin": case "cos": case "tan": return 4;
			default: return -1;
		}
	}

	public boolean isNumber(String s){
		return(s.matches("-?\\d+(\\.\\d+)?"));  // numeric
	}

	public ArrayList<String> parse(String func){
		for(String s : symbols){
			 func = func.replace(s," "+s+" ");
		}
		String[] temp = func.split(" ");
		ArrayList<String> arr = new ArrayList<String>(); 
		for(String sym : temp){
			if(!sym.trim().equals("")){
				arr.add(sym);
			}
		}
		return arr;
	}

	public static void main(String[] args){
		new Calculator().calculate("2(2(3)+10(1.5)+(3/4)+(1/4))");
	}
}
