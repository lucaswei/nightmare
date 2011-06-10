import java.util.*;

public class Calculator{
	public static void main(String[] argus){
		if(argus.length > 0){
			if(argus.length > 1 && argus[1].equals("rpn"))
				System.out.println(RPN(argus[0]));
			else
				System.out.println(calculate(argus[0]));
		}
		else
			System.out.println("Miss argument");
	}

	private static boolean isNumber(String s){
		return s.matches("-?\\d+(.\\d+)?");
	}

	public static String calculate(String s){
		String rpn = RPN(s);
		Stack<Double> stack = new Stack<Double>();
		String operand = "";
		Double op;
		boolean number = false;
		String[] tokens = rpn.split(" ");
		int length = tokens.length;
		for(String token : tokens){
			if(isNumber(token)){
			
				stack.push(Double.valueOf(token));
			}
			else{
				if(token.equals("+")){
					op = stack.pop();
					stack.push(stack.pop() + op);
				}
				else if(token.equals("-")){
					op = stack.pop();
					stack.push(stack.pop() - op);
				}
				else if(token.equals("*")){
					op = stack.pop();
					stack.push(stack.pop() * op);
				}
				else if(token.equals("/")){
					op = stack.pop();
					stack.push(stack.pop() / op);
				}
			}
		}
		
		Double result = stack.pop();
		String output = "";
		if(result == Math.floor(result))
			output = Integer.toString(result.intValue());
		else
			output = result.toString();
		return output;
	}
	
	
	private static String RPN(String s){
		Stack<Character> stack = new Stack<Character>();
		String result = "";
		char top = 0;
		char[] str = s.toCharArray();
		char c = 0;
		char p = 0;
		boolean number = false;
		for(int i=0;i<str.length;i++){
			p = c;
			c = str[i];
			switch(c){
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
				case '.':
					number = true;
					result += c;
					break;
				case '-':
					if(!number){
						result += ' ';
						number = true;
						result += c;
						break;
					}
				case '+':
					number = false;
					result += ' ';
					while(!(stack.empty()) && ((top = stack.peek()) == '*' || top == '/'))
						result += " "+stack.pop();
					stack.push(c);
					break;
				case '*':
				case '/':
					number = false;
					result += ' ';
					stack.push(c);
					break;
				case '(':
					number = false;
					stack.push(c);
					break;
				case ')': 
					number = false;
					while(!(stack.empty()) && ((top = stack.peek()) != '('))
						result += " "+stack.pop();
					stack.pop();
					break;
				default:
						
			}
		}
		while(!(stack.empty())){
			result += " "+stack.pop();
		}
		return result.trim();
	}
}
