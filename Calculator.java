import java.util.*;

public class Calculator{
	public static void main(String[] argus){
		if(argus.length > 0){
			System.out.println(calculate(argus[0]));
		}
		else
			System.out.println("Miss argument");
	}

	public static String calculate(String s){
		String rpn = RPN(s);
		Stack<Double> stack = new Stack<Double>();
		String operand = "";
		Double op;
		char[] str = rpn.toCharArray();
		int length = str.length;
		for(int i=0;i<length;i++){
			char c = str[i];
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
				case '9':operand += c;break;
				case ' ':if(Character.isDigit(str[i-1])){
							stack.push(Double.valueOf(operand));
				         	operand = "";
				         }
				         break;
				case '+':op = stack.pop();
				         stack.push(stack.pop() + op);
				         break;
				case '-':op = stack.pop();
				         stack.push(stack.pop() - op);
				         break;
				case '*':op = stack.pop();
				         stack.push(stack.pop() * op);
				         break;
				case '/':op = stack.pop();
				         stack.push(stack.pop() / op);
				         break;
				default:
			}
		}
		
		Double result = stack.pop();
		String output = "";
		if(result == Math.floor(result))
			output = Integer.toString(result.intValue());
		else
			output = result.toString();
			
		System.out.println(result+"=>"+output);
		return output;
	
	}
	private static String RPN(String s){
		Stack<Character> stack = new Stack<Character>();
		String result = "";
		char top = 0;
		char[] str = s.toCharArray();
		char c = 0;
		char p = 0;
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
					result += c;
					break;
				case '+':
				case '-':
					if(Character.isDigit(p))result += ' ';
					while(!(stack.empty()) && ((top = stack.peek()) == '*' || top == '/'))
						result += stack.pop();
					stack.push(c);
					break;
				case '*':
				case '/':
					if(Character.isDigit(p))result += ' ';
					stack.push(c);
					break;
				case '(':
					if(Character.isDigit(p))result += ' ';
					stack.push(c);
					break;
				case ')': 
					if(Character.isDigit(p))result += ' ';
					while(!(stack.empty()) && ((top = stack.peek()) != '('))
						result += stack.pop();
					stack.pop();
					break;
				default:
						
			}
		}
		if(Character.isDigit(c)) result += ' ';
		while(!(stack.empty())){
			result += stack.pop();
		}
		return result;
	}
}
