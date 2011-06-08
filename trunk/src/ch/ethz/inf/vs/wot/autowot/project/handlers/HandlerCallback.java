package ch.ethz.inf.vs.wot.autowot.project.handlers;

/**
 * Class holding information about a Handler method
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */

public class HandlerCallback {

	protected String methodName;
	protected String HTTPMethod;
	protected HandlerCallbackType type;
	
	public HandlerCallback(String methodName, HandlerCallbackType methodType, String HTTPmethod) {
		this.methodName = methodName;
		this.HTTPMethod = HTTPmethod;
		this.type = methodType;
	}
	
	public String getMethodName() {
		return this.methodName;
	}
	
	public String getHTTPMethod() {
		return this.HTTPMethod;
	}
	
	public HandlerCallbackType getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		String callbackStub = "";
		if (getHTTPMethod().equalsIgnoreCase("GET")) {
			if(getType().equals(HandlerCallbackType.PYTHON)) {
				callbackStub += "public static String " + getMethodName() + "(HashMap<String, Object> source) {\n" +
						"\t\torg.python.util.PythonInterpreter pythonInterpreter = new org.python.util.PythonInterpreter();\n" +
						"\t\tpythonInterpreter.exec(\"print 'A GET was called on the dynamic resource'\");\n" +
						"\t\tpythonInterpreter.exec(\"message = 'Hello World!'\");\n" +
						"\t\treturn pythonInterpreter.get(\"message\").toString();\n" +
						"\t}";
			} else {
				callbackStub += "public static String " + getMethodName() + "(HashMap<String, Object> source) {\n" +
						"\t\tSystem.out.println(\"A GET was called on the dynamic resource \\\"\" + source + \"\\\"\");\n" +
						"\t\t{External Call}\n" +
						"\t\treturn \"Hello World!\";\n" +
						"\t}";
			}
		} else if (getHTTPMethod().equalsIgnoreCase("POST")) {
			if(getType().equals(HandlerCallbackType.PYTHON)) {
				callbackStub += "public static void " + getMethodName() + "(HashMap<String, Object> source, Object posterVar) {\n" +
						"\t\torg.python.util.PythonInterpreter pythonInterpreter = new org.python.util.PythonInterpreter();\n" +
						"\t\tpythonInterpreter.exec(\"print 'A POST was called on the dynamic resource'\");\n" +
						"\t\tfor(String key : source.keySet()) System.out.println(\"\\t\\\" + key + \\\": \" + source.get(key));\n" +
						"\t}";
			} else {
				callbackStub += "public static void " + getMethodName() + "(HashMap<String, Object> source, Object posterVar) {\n" +
						"\t\tSystem.out.println(\"A POST was called on the dynamic resource with data: \\\"\" + posterVar + \"\\\"\");\n" +
						"\t\t{External Call}\n" +
						"\t\tfor(String key : source.keySet()) System.out.println(\"\\t\\\" + key + \\\": \" + source.get(key));\n" +
						"\t}";
			}
		} else if (getHTTPMethod().equalsIgnoreCase("PUT")) {
			if(getType().equals(HandlerCallbackType.PYTHON)) {
				callbackStub += "public static void " + getMethodName() + "(HashMap<String, Object> source, Object putterVar) {\n" +
						"\t\torg.python.util.PythonInterpreter pythonInterpreter = new org.python.util.PythonInterpreter();\n" +
						"\t\tpythonInterpreter.exec(\"print 'A PUT was called on the dynamic resource'\");\n" +
						"\t\tfor(String key : source.keySet()) System.out.println(\"\\t\\\" + key + \\\": \" + source.get(key));\n" +
						"\t}";
			} else {
				callbackStub += "public static void " + getMethodName() + "(HashMap<String, Object> source, Object putterVar) {\n" +
						"\t\tSystem.out.println(\"A PUT was called on the dynamic resource \\\"\" + source + \"\\\" with data: \\\"\" + putterVar + \"\\\"\");\n" +
						"\t\t{External Call}\n" +
						"\t\tfor(String key : source.keySet()) System.out.println(\"\\t\\\" + key + \\\": \" + source.get(key));\n" +
						"\t}";
			}
		} else if (getHTTPMethod().equalsIgnoreCase("DELETE")) {
			if(getType().equals(HandlerCallbackType.PYTHON)) {
				callbackStub += "public static void " + getMethodName() + "(HashMap<String, Object> source) {\n" +
						"\t\torg.python.util.PythonInterpreter pythonInterpreter = new org.python.util.PythonInterpreter();\n" +
						"\t\tpythonInterpreter.exec(\"print 'A DELETE was called on the dynamic resource'\");\n" +
						"\t\tfor(String key : source.keySet()) System.out.println(\"\\t\\\" + key + \\\": \" + source.get(key));\n" +
						"\t}";
			} else {
				callbackStub += "public static void " + getMethodName() + "(HashMap<String, Object> source) {\n" +
						"\t\tSystem.out.println(\"A DELETE was called on the dynamic resource \\\"\" + source + \"\\\"\");\n" +
						"\t\t{External Call}\n" +
						"\t\tfor(String key : source.keySet()) System.out.println(\"\\t\\\" + key + \\\": \" + source.get(key));\n" +
						"\t}";
			}
		} else if (getHTTPMethod().equalsIgnoreCase("COLLECT")) {
			if(getType().equals(HandlerCallbackType.PYTHON)) {
				callbackStub += "public static List<String> " + getMethodName() + "(HashMap<String, Object> source) {\n" +
						"\t\tSystem.out.println(\"A reflexive call has been executed on the dynamic resource \\\"\" + source + \"\\\"\");\n" +
						"\t\tList<String> returnList = new ArrayList<String>();\n" +
						"\t\torg.python.util.PythonInterpreter pythonInterpreter = new org.python.util.PythonInterpreter();\n" +
						"\t\tpythonInterpreter.exec(\"children = 'Subresource1', 'Subresource2', 'Subresource3'\");\n" +
						"\t\tpythonInterpreter.exec(\"firstElement = children[0]\");\n" +
						"\t\tpythonInterpreter.exec(\"secondElement = children[1]\");\n" +
						"\t\tpythonInterpreter.exec(\"thirdElement = children[2]\");\n" +
						"\t\treturnList.add(pythonInterpreter.get(\"firstElement\").toString());\n" +
						"\t\treturnList.add(pythonInterpreter.get(\"secondElement\").toString());\n" +
						"\t\treturnList.add(pythonInterpreter.get(\"thirdElement\").toString());\n" +
						"\t\treturn returnList;\n" +
						"\t }";
			} else {
				callbackStub += "public static List<String> " + getMethodName() + "(HashMap<String, Object> source) {\n" +
						"\t\t{External Call}\n" +
						"\t\tSystem.out.println(\"A reflexive call has been executed on the dynamic resource \\\"\" + source + \"\\\"\");\n" +
						"\t\tList<String> returnList = new ArrayList<String>();\n" +
						"\t\treturnList.add(\"Subresource1\");\n" +
						"\t\treturnList.add(\"Subresource2\");\n" +
						"\t\treturnList.add(\"Subresource3\");\n" +
						"\t\treturn returnList;\n" +
						"\t }";
			}
		} else {
			System.err.println("Unknown HTTP Method \"" + getHTTPMethod() + "\" when creating Handler class!");
		}
		
		if(getType().equals(HandlerCallbackType.SHELL)) {
			String shellScript = "try {\n" +
			"\t\t\tSystem.out.println(java.lang.Runtime.getRuntime().exec(\"" + "./" + getMethodName() + "\"));\n" +
			"\t\t} catch (IOException e) {\n" +
			"\t\t\te.printStackTrace();"+
			"\t\t}";
			callbackStub = callbackStub.replace("{External Call}", shellScript);
		} else {
			callbackStub = callbackStub.replace("{External Call}", "");
		}
		return callbackStub;
	}
}
