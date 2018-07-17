
import java.util.Stack;

public class Calculator {


	Stack<Double>outputStack = new Stack<Double>();
	Stack<Symbol> inputStack = new Stack<Symbol>();
	String[] equArr;


	Symbol[] SYMBOLS;

	public static void main(String[] args){
		new Calculator().calculate("5*3-sin(5+6)-12/13");
	}

	public Calculator(){
		loadSymbols();
	}

	private void loadSymbols(){
		SYMBOLS = new Symbol[]{
				new Symbol("-",1),
				new Symbol("+",1),
				new Symbol("/",2),
				new Symbol("*",2),
				new Symbol("^",3),
				new Symbol("sin",4),
				new Symbol("cos",4),
				new Symbol("tan",4),
				new Symbol("(",-1),
				new Symbol(")",-2),
		};
	}

	public void calculate(String equation){
		outputStack.clear();
		inputStack.clear();

		parse(equation);

		System.out.println(compute());

	}

	public double compute(){

		for(String s : equArr){
			if(s.matches("-?\\d+(\\.\\d+)?")){ // numeric
				outputStack.push(Double.parseDouble(s));
			}else{
				Symbol sym = Symbol.getSymbol(SYMBOLS, s);
				if(sym.value>0){ // no special operation
					if(!inputStack.isEmpty() && (inputStack.peek().compareTo(sym) < 0)){ 
						while(!inputStack.isEmpty()){
							step();
						}
					}
					inputStack.push(sym);
				}else{ // special parentheses backtrack 
					if(sym.value==-1){
						inputStack.push(sym);
					}else{
						while(inputStack.peek().value!=-1){
							step();
						}
						inputStack.pop();
					}
				}

			}

		}

		while(!inputStack.isEmpty()){
			step();
		}


		return outputStack.pop();
	}

	public void step(){
		Symbol sym = inputStack.pop();
		double d3 = 0;
		if(sym.value < 4){
			double d1 = outputStack.pop();
			double d2 = outputStack.pop();
			switch(sym.symbol){
			case "+":
				d3 = d2+d1;
				break;
			case "-":
				d3 = d2-d1;
				break;
			case "/":
				d3 = d2/d1;
				break;
			case "*":
				d3 = d2*d1;
				break;
			case "^":
				d3 = Math.pow(d2, d1);
				break;
			}

		}else{
			double d1 = outputStack.pop();
			switch(sym.symbol){
			case "sin":
				d3 = Math.sin(d1);
				break;
			case "cos":
				d3 = Math.cos(d1);
				break;
			case "tan":
				d3 = Math.tan(d1);
				break;
			}
		}
		outputStack.add(d3);
	}

	public void parse(String equation){
		String str = equation;
		final String LIMITER = "~";

		for(Symbol symbol : SYMBOLS){
			str = str.replace(symbol.symbol, LIMITER + symbol.symbol + LIMITER);
		}

		equArr = str.split("~");

		int count = 0;
		for(String s : equArr){
			if(!s.equals("")){
				count++;
			}
		}
		String[] equAlpha = new String[count];
		count = 0;
		for(String s : equArr){
			if(!s.equals("")){
				equAlpha[count++] = s;
			}
		}
		equArr = equAlpha;
	}


}

class Symbol implements Comparable<Symbol>{

	String symbol;
	int value;

	public Symbol(String symbol, int value){
		this.symbol = symbol;
		this.value = value;
	}

	public int compareTo(Symbol symbol) {
		if(symbol.value>value){ return 1; }
		else if(symbol.value<value){ return -1; }
		return 0; //equal
	}

	public static Symbol getSymbol(Symbol[] arr, String s){
		for(Symbol sym : arr){
			if(sym.symbol.equals(s)){
				return sym;
			}
		}
		return null;
	}

}
